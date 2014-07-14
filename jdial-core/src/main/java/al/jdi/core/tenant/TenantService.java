package al.jdi.core.tenant;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;

import al.jdi.common.Service;
import al.jdi.core.tenant.Tenant.Factory;
import al.jdi.dao.model.Campanha;

@Singleton
public class TenantService implements Service {

  private final Logger logger;
  private final Map<Campanha, Tenant> tenants;
  private final Tenant.Factory tenantFactory;

  @Inject
  TenantService(Logger logger, Map<Campanha, Tenant> tenants, Factory tenantFactory) {
    this.logger = logger;
    this.tenants = tenants;
    this.tenantFactory = tenantFactory;
  }


  @Override
  public void start() {}

  @Override
  public void stop() {
    synchronized (tenants.keySet()) {
      for (Campanha campanha : tenants.keySet()) {
        tenants.get(campanha).stop();
      }
    }
  }

  public void addTenant(Campanha campanha) {
    logger.debug("Trying to add tenant {}", campanha);
    Tenant tenant = tenantFactory.create(campanha);
    tenant.start();
    synchronized (tenants) {
      tenants.put(campanha, tenant);
    }
    logger.debug("Successfuly added tenant {}", campanha);
  }

  public void removeTenant(Campanha campanha) {
    logger.debug("Trying to remove tenant {}", campanha);
    Tenant tenant;
    synchronized (tenants) {
      if (!tenants.containsKey(campanha)) {
        logger.warn("Tried to remove unkown tenant {}", campanha);
        return;
      }
      tenant = tenants.remove(campanha);
    }
    tenant.stop();
    logger.info("Successfuly remove tenant {}", campanha);
  }

}
