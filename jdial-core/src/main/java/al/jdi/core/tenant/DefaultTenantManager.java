package al.jdi.core.tenant;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.jboss.weld.environment.se.StartMain;
import org.slf4j.Logger;

import al.jdi.core.tenant.Tenant.Factory;
import al.jdi.core.tenant.TenantModule.TenantManagerService;
import al.jdi.core.tenant.TenantModule.TenantType;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;

@Singleton
@TenantManagerService
class DefaultTenantManager implements TenantManager {

  private static final Logger logger = getLogger(DefaultTenantManager.class);

  private final TenantType type;
  private final Map<Campanha, Tenant> tenants;
  private final Tenant.Factory tenantFactory;
  private final Provider<DaoFactory> daoFactoryProvider;

  @Inject
  DefaultTenantManager(TenantType type, Map<Campanha, Tenant> tenants, Factory tenantFactory,
      Provider<DaoFactory> daoFactoryProvider) {
    this.type = type;
    this.tenants = tenants;
    this.tenantFactory = tenantFactory;
    this.daoFactoryProvider = daoFactoryProvider;
  }


  @Override
  public void start() {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      if (type.equals(TenantType.STANDALONE)) {
        addTenant(daoFactory.getCampanhaDao().procura(StartMain.getParameters()[0]));
        return;
      }

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
