package al.jdi.core.devolveregistro;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;

public interface DevolveRegistro {

  void devolveLigacao(Tenant tenant, Ligacao ligacao);

}
