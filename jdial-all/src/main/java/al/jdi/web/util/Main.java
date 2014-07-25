package al.jdi.web.util;

import static org.slf4j.LoggerFactory.getLogger;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import al.jdi.common.Service;
import al.jdi.core.devolveregistro.DevolveRegistroModule.DevolveRegistroService;
import al.jdi.core.tenant.TenantModule.TenantManagerService;
import al.jdi.cti.DialerCtiManagerModule.DialerCtiManagerService;
import br.com.caelum.vraptor.events.VRaptorInitialized;

class Main {

  private static Logger logger = getLogger(Main.class);

  private final Service devolveRegistroService;
  private final Service dialerCtiManagerService;
  private final Service tenantService;

  @Inject
  Main(@DevolveRegistroService Service devolveRegistroService,
      @DialerCtiManagerService Service dialerCtiManagerService,
      @TenantManagerService Service tenantService) {
    this.devolveRegistroService = devolveRegistroService;
    this.dialerCtiManagerService = dialerCtiManagerService;
    this.tenantService = tenantService;
  }

  @PreDestroy
  public void stop() {
    devolveRegistroService.stop();
    dialerCtiManagerService.stop();
    tenantService.stop();
  }

  public void run(@Observes VRaptorInitialized event) {
    ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
    try {
      devolveRegistroService.start();
      dialerCtiManagerService.start();
      tenantService.start();
      logger.warn("JDial Started");
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

}
