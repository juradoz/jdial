package al.jdi.core.tenant;

import al.jdi.common.Service;
import al.jdi.dao.model.Campanha;

public interface TenantManager extends Service {

  void addTenant(Campanha campanha);

  void removeTenant(Campanha campanha);

  Tenant get(Campanha campanha);

}
