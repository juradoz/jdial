package al.jdi.core.tenant;

import al.jdi.common.Service;
import al.jdi.dao.model.Campanha;

public interface TenantService extends Service {

  public abstract void addTenant(Campanha campanha);

  public abstract void removeTenant(Campanha campanha);

}
