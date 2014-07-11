package al.jdi.core.modelo;

import java.util.Collection;
import java.util.Map;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;

import ch.lambdaj.Lambda;

class ProvidenciaModule {

  @Produces
  public Map<Providencia.Codigo, Providencia> getProvidenciaMap(
      @Any Collection<Providencia> providencias) {
    return Lambda.index(providencias, Lambda.on(Providencia.class).getCodigo());
  }

}
