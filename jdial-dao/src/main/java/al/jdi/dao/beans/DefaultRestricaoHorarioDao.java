package al.jdi.dao.beans;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import al.jdi.dao.model.RestricaoHorario;

class DefaultRestricaoHorarioDao implements Dao<RestricaoHorario> {

  private final DefaultDao<RestricaoHorario> dao;

  DefaultRestricaoHorarioDao(Session session) {
    this.dao = new DefaultDao<>(session, RestricaoHorario.class);
  }

  @Override
  public RestricaoHorario procura(String ddd) {
    return (RestricaoHorario) dao.getSession().createCriteria(RestricaoHorario.class)
        .add(Restrictions.eq("ativo", true)).add(Restrictions.eq("ddd", ddd)).uniqueResult();
  }

  @Override
  public void adiciona(RestricaoHorario t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(RestricaoHorario t) {
    dao.atualiza(t);
  }

  @Override
  public List<RestricaoHorario> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public RestricaoHorario procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(RestricaoHorario u) {
    dao.remove(u);
  }

}
