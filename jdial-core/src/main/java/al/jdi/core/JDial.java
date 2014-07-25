package al.jdi.core;

import al.jdi.common.Service;
import al.jdi.core.tenant.Tenant;

public interface JDial extends Service {
  public interface Factory {
    JDial create(Tenant tenant);
  }
}
