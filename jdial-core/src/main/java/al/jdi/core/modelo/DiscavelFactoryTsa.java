package al.jdi.core.modelo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Cliente;

class DiscavelFactoryTsa implements Discavel.Factory {

  @Override
  public Discavel create(Tenant tenant, Cliente cliente) {
    return new DiscavelTsa(tenant, cliente);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
