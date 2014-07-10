package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Servico;

class DefaultServicoDao implements ServicoDao {

  private final DefaultDao<Servico> dao;

  DefaultServicoDao(Session session) {
    this.dao = new DefaultDao<>(session, Servico.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Servico> monitoraveisQrf() {
    return dao.getSession().createCriteria(Servico.class).add(eq("monitoraQrf", true)).list();
  }

  @Override
  public Servico procura(String s) {
    return dao.procura(s);
  }

  @Override
  public void adiciona(Servico t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(Servico t) {
    dao.atualiza(t);
  }

  @Override
  public List<Servico> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Servico procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Servico u) {
    dao.remove(u);
  }

}
