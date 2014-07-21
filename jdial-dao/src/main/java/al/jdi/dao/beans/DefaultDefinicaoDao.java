package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Definicao;

class DefaultDefinicaoDao implements DefinicaoDao {

  private final DefaultDao<Definicao> dao;

  DefaultDefinicaoDao(Session session) {
    this.dao = new DefaultDao<>(session, Definicao.class);
  }

  @Override
  public void adiciona(Definicao definicao) {
    dao.adiciona(definicao);
    adicionaCampanha(definicao);
  }

  private void adicionaCampanha(Definicao definicao) {
    definicao.getCampanha().getDefinicao().add(definicao);
    new DefaultCampanhaDao(dao.getSession()).atualiza(definicao.getCampanha());
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Definicao> listaTudo(Campanha campanha) {
    return dao.getSession().createCriteria(Definicao.class).add(eq("campanha", campanha)).list();
  }

  @Override
  public Definicao procura(Campanha campanha, String propriedade) {
    return (Definicao) dao.getSession().createCriteria(Definicao.class)
        .add(eq("campanha", campanha)).add(eq("propriedade", propriedade)).uniqueResult();
  }

  @Override
  public void atualiza(Definicao t) {
    dao.atualiza(t);
  }

  @Override
  public List<Definicao> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Definicao procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Definicao u) {
    dao.remove(u);
  }

  @Override
  public Definicao procura(String s) {
    return dao.procura(s);
  }

}
