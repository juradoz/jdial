package al.jdi.core.modelo;

import java.util.Map;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

import ch.lambdaj.Lambda;

class ProvidenciaModule {

  @Produces
  public Map<Providencia.Codigo, Providencia> getProvidenciaMap(
      @Any Instance<Providencia> providencias) {
    return Lambda.index(providencias, Lambda.on(Providencia.class).getCodigo());
  }

}
