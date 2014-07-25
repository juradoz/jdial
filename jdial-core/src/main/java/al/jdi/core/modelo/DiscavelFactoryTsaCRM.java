package al.jdi.core.modelo;

import javax.enterprise.inject.Alternative;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Cliente;

@Alternative
class DiscavelFactoryTsaCRM implements Discavel.Factory {

  @Override
  public Discavel create(Tenant tenant, Cliente cliente) {
    return new DiscavelTsaCRM(tenant, cliente);
  }

}
