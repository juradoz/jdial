package al.jdi.dao;

import al.jdi.dao.model.EstadoCliente;

public interface EstadoClienteDao extends Dao<EstadoCliente> {

  EstadoCliente procura(String nome);

}
