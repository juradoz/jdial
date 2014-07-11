package al.jdi.core.filter;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Telefone;

class BloqueioCelularUtil implements TelefoneUtil {

  private final CelularChecker telefoneCelularChecker;
  private final Configuracoes configuracoes;

  @Inject
  BloqueioCelularUtil(CelularChecker telefoneCelularChecker, Configuracoes configuracoes) {
    this.telefoneCelularChecker = telefoneCelularChecker;
    this.configuracoes = configuracoes;
  }

  @Override
  public boolean isUtil(Telefone telefone) {
    return !(configuracoes.bloqueiaCelular() && telefoneCelularChecker.isCelular(telefone));
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
