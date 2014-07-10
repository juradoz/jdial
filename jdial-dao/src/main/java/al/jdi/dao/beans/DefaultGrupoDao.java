package al.jdi.dao.beans;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Grupo;

class DefaultGrupoDao implements Dao<Grupo> {

  private final DefaultDao<Grupo> dao;

  DefaultGrupoDao(Session session) {
    this.dao = new DefaultDao<>(session, Grupo.class);
  }

  @Override
  public Grupo procura(String s) {
    return dao.procura(s);
  }

  @Override
  public void adiciona(Grupo t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(Grupo t) {
    dao.atualiza(t);
  }

  @Override
  public List<Grupo> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Grupo procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Grupo u) {
    dao.remove(u);
  }

}
