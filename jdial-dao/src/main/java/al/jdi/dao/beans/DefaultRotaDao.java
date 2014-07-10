package al.jdi.dao.beans;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Rota;

class DefaultRotaDao implements Dao<Rota> {

  private final DefaultDao<Rota> dao;

  DefaultRotaDao(Session session) {
    this.dao = new DefaultDao<>(session, Rota.class);
  }

  @Override
  public Rota procura(String s) {
    return dao.procura(s);
  }

  @Override
  public void adiciona(Rota t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(Rota t) {
    dao.atualiza(t);
  }

  @Override
  public List<Rota> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Rota procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Rota u) {
    dao.remove(u);
  }

}
