package al.jdi.core.estoque;

import java.util.Collection;

import org.joda.time.Period;

import al.jdi.common.Service;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Cliente;

public interface Estoque extends Service {

  public interface Factory {
    Estoque create(Tenant tenant, ExtraidorClientes extraidorClientes, Period intervaloMonitoracao);
  }

  Collection<Cliente> obtemRegistros(int quantidade);
  
}
