package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Mailing;

class DefaultMailingDao implements MailingDao {

  private final DefaultDao<Mailing> dao;

  DefaultMailingDao(Session session) {
    this.dao = new DefaultDao<>(session, Mailing.class);
  }

  @Override
  public void adiciona(Mailing mailing) {
    dao.adiciona(mailing);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<Mailing> listaAtivos() {
    return dao.getSession().createCriteria(Mailing.class).add(eq("ativo", true)).list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Mailing> listaTudo(Campanha campanha) {
    return dao.getSession().createCriteria(Mailing.class).add(eq("campanha", campanha)).list();
  }

  @Override
  public void remove(Mailing t) {
    dao.getSession().createSQLQuery("update Mailing set ativo = 0 where idMailing = :idMailing")
        .setLong("idMailing", t.getId()).executeUpdate();
    dao.getSession()
        .createSQLQuery(
            "update Cliente inner join InformacaoCliente on Cliente.idCliente = InformacaoCliente.idCliente inner join Telefone on Cliente.idCliente = Telefone.idCliente set InformacaoCliente.idCliente = 0, Telefone.idCliente = 0 where Cliente.idMailing = :idMailing")
        .setLong("idMailing", t.getId()).executeUpdate();
    dao.getSession().createSQLQuery("delete from InformacaoCliente where idCliente = 0")
        .executeUpdate();
    dao.getSession().createSQLQuery("delete from Telefone where idCliente = 0").executeUpdate();
    dao.getSession().createSQLQuery("delete from Cliente where idMailing = :idMailing")
        .setLong("idMailing", t.getId()).executeUpdate();

    DefaultCampanhaDao campanhaDao = new DefaultCampanhaDao(dao.getSession());
    Campanha campanha = campanhaDao.procura(t.getCampanha().getId());
    campanha.setLimpaMemoria(true);
    campanhaDao.atualiza(campanha);
    dao.remove(t);
  }

  @Override
  public void atualiza(Mailing t) {
    DefaultCampanhaDao campanhaDao = new DefaultCampanhaDao(dao.getSession());
    Campanha campanha = campanhaDao.procura(t.getCampanha().getId());
    campanha.setLimpaMemoria(true);
    campanhaDao.atualiza(campanha);
    dao.atualiza(t);
  }

  @Override
  public List<Mailing> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Mailing procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public Mailing procura(String s) {
    return dao.procura(s);
  }

}
