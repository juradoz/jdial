package al.jdi.core.tenant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;

import al.jdi.dao.model.Campanha;

public class TenantModule {
  @Produces
  public Map<Campanha, Tenant> getTenantCollection() {
    return Collections.synchronizedMap(new HashMap<Campanha, Tenant>());
  }
}
