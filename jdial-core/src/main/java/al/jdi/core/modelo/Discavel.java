package al.jdi.core.modelo;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Cliente;

public interface Discavel {

  public interface Factory {
    Discavel create(Tenant tenant, Cliente cliente);
  }

  String getChave();

  Cliente getCliente();

  String getDigitoSaida();

  String getDdd();

  String getTelefone();

  void setCliente(Cliente procura);

  String getDestino();
}
