package al.jdi.core.gerenciadoragentes;

import al.jdi.common.Service;
import al.jdi.core.tenant.Tenant;

public interface GerenciadorAgentes extends Service {

  public interface Factory {
    GerenciadorAgentes create(Tenant tenant);
  }

  int getLivres();
}
