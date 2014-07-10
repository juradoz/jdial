package al.jdi.dao.beans;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.EstadoCliente;

class DefaultEstadoClienteDao implements Dao<EstadoCliente> {

  private final DefaultDao<EstadoCliente> dao;

  DefaultEstadoClienteDao(Session session) {
    this.dao = new DefaultDao<>(session, EstadoCliente.class);
  }

  @Override
  public EstadoCliente procura(String s) {
    return dao.procura(s);
  }

  @Override
  public void adiciona(EstadoCliente t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(EstadoCliente t) {
    dao.atualiza(t);
  }

  @Override
  public List<EstadoCliente> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public EstadoCliente procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(EstadoCliente u) {
    dao.remove(u);
  }

}
