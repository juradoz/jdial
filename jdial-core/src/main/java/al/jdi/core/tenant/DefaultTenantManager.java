package al.jdi.core.tenant;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.slf4j.Logger;

import al.jdi.core.tenant.Tenant.Factory;
import al.jdi.core.tenant.TenantModule.TenantManagerService;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;

@Singleton
@TenantManagerService
class DefaultTenantManager implements TenantManager {

  private final Logger logger;
  private final Map<Campanha, Tenant> tenants;
  private final Tenant.Factory tenantFactory;
  private final Provider<DaoFactory> daoFactoryProvider;

  @Inject
  DefaultTenantManager(Logger logger, Map<Campanha, Tenant> tenants, Factory tenantFactory,
      Provider<DaoFactory> daoFactoryProvider) {
    this.logger = logger;
    this.tenants = tenants;
    this.tenantFactory = tenantFactory;
    this.daoFactoryProvider = daoFactoryProvider;
  }


  @Override
  public void start() {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      for (Campanha campanha : daoFactory.getCampanhaDao().listaAtivas()) {
        addTenant(campanha);
      }
    } finally {
      daoFactory.close();
    }
  }

  @Override
  public void stop() {
    synchronized (tenants.keySet()) {
      for (Campanha campanha : tenants.keySet()) {
        tenants.get(campanha).stop();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see al.jdi.core.tenant.TenantService#addTenant(al.jdi.dao.model.Campanha)
   */
  @Override
  public void addTenant(Campanha campanha) {
    logger.debug("Trying to add tenant {}", campanha);
    Tenant tenant = tenantFactory.create(campanha);
    tenant.start();
    synchronized (tenants) {
      tenants.put(campanha, tenant);
    }
    logger.debug("Successfuly added tenant {}", campanha);
  }

  /*
   * (non-Javadoc)
   * 
   * @see al.jdi.core.tenant.TenantService#removeTenant(al.jdi.dao.model.Campanha)
   */
  @Override
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
