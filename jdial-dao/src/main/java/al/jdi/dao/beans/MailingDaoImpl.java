package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.MailingDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Mailing;

class MailingDaoImpl extends DaoImpl<Mailing> implements MailingDao {

  MailingDaoImpl(Session session) {
    super(session, Mailing.class);
  }

  @Override
  public void adiciona(Mailing mailing) {
    super.adiciona(mailing);
    mailing.getCampanha().getMailing().add(mailing);
    new CampanhaDaoImpl(getSession()).atualiza(mailing.getCampanha());
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<Mailing> listaAtivos() {
    return getSession().createCriteria(Mailing.class).add(eq("ativo", true)).list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Mailing> listaTudo(Campanha campanha) {
    return getSession().createCriteria(Mailing.class).add(eq("campanha", campanha)).list();
  }

  @Override
  public Mailing procura(Campanha campanha, String nome) {
    return (Mailing) getSession().createCriteria(Mailing.class).add(eq("campanha", campanha))
        .add(eq("nome", nome)).uniqueResult();
  }

  @Override
  public void remove(Mailing u) {
    getSession().createSQLQuery("update Mailing set ativo = 0 where idMailing = :idMailing")
        .setLong("idMailing", u.getIdMailing()).executeUpdate();
    getSession()
        .createSQLQuery(
            "update Cliente inner join InformacaoCliente on Cliente.idCliente = InformacaoCliente.idCliente inner join Telefone on Cliente.idCliente = Telefone.idCliente set InformacaoCliente.idCliente = 0, Telefone.idCliente = 0 where Cliente.idMailing = :idMailing")
        .setLong("idMailing", u.getIdMailing()).executeUpdate();
    getSession().createSQLQuery("delete from InformacaoCliente where idCliente = 0")
        .executeUpdate();
    getSession().createSQLQuery("delete from Telefone where idCliente = 0").executeUpdate();
    getSession().createSQLQuery("delete from Cliente where idMailing = :idMailing")
        .setLong("idMailing", u.getIdMailing()).executeUpdate();
    super.remove(u);
  }

}
