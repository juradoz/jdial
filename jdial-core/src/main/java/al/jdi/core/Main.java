package al.jdi.core;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.jdi.common.Service;
import al.jdi.core.devolveregistro.DevolveRegistroModule.DevolveRegistroService;
import al.jdi.core.tenant.TenantModule.TenantManagerService;
import al.jdi.cti.DialerCtiManagerModule.DialerCtiManagerService;

@ApplicationScoped
public class Main {

  private final Service devolveRegistroService;
  private final Service dialerCtiManagerService;
  private final Service tenantService;

  @Inject
  Main(@DevolveRegistroService Service devolveRegistroService,
      @DialerCtiManagerService Service dialerCtiManagerService,
      @TenantManagerService Service tenantService, ShutdownHook.Factory shutdownHookFactory) {
    this.devolveRegistroService = devolveRegistroService;
    this.dialerCtiManagerService = dialerCtiManagerService;
    this.tenantService = tenantService;
  }

  @PostConstruct
  public void start() {
    ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
    devolveRegistroService.start();
    dialerCtiManagerService.start();
    tenantService.start();
  }

  @PreDestroy
  public void destroy() {
    devolveRegistroService.stop();
    dialerCtiManagerService.stop();
    tenantService.stop();
  }

  public void run(@Observes ContainerInitialized event) {}

}
