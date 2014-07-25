package al.jdi.core.filter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Telefone;

class CampoUtil implements TelefoneUtil {

  @Override
  public boolean isUtil(Tenant tenant, Telefone telefone) {
    return telefone.isUtil();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
