package al.jdi.dao.beans;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.HistoricoCliente;

class DefaultHistoricoClienteDao implements Dao<HistoricoCliente> {

  private final DefaultDao<HistoricoCliente> dao;

  DefaultHistoricoClienteDao(Session session) {
    this.dao = new DefaultDao<>(session, HistoricoCliente.class);
  }

  @Override
  public void adiciona(HistoricoCliente historicoCliente) {
    dao.adiciona(historicoCliente);
    historicoCliente.getCliente().getHistoricoCliente().add(historicoCliente);
    new DefaultClienteDao(dao.getSession()).atualiza(historicoCliente.getCliente());
  }

  @Override
  public void atualiza(HistoricoCliente t) {
    dao.atualiza(t);
  }

  @Override
  public List<HistoricoCliente> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public HistoricoCliente procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(HistoricoCliente u) {
    dao.remove(u);
  }

  @Override
  public HistoricoCliente procura(String s) {
    return dao.procura(s);
  }

}
