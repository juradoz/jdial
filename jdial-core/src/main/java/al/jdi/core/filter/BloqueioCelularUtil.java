package al.jdi.core.filter;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Telefone;

class BloqueioCelularUtil implements TelefoneUtil {

  private final CelularChecker telefoneCelularChecker;

  @Inject
  BloqueioCelularUtil(CelularChecker telefoneCelularChecker) {
    this.telefoneCelularChecker = telefoneCelularChecker;
  }

  @Override
  public boolean isUtil(Tenant tenant, Telefone telefone) {
    return !(tenant.getConfiguracoes().bloqueiaCelular() && telefoneCelularChecker
        .isCelular(telefone));
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
