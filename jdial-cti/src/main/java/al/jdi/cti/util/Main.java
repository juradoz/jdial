package al.jdi.cti.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.joda.time.Period;

import al.jdi.cti.CtiManager;
import al.jdi.cti.CtiManager.CtiManagerService;

public class Main {

  private final CtiManager ctiManager;

  @Inject
  public Main(@CtiManagerService CtiManager ctiManager) {
    this.ctiManager = ctiManager;
  }

  @PostConstruct
  public void start() {
    ctiManager.start();
  }

  public void main(@Observes ContainerInitialized event) throws InterruptedException {
    Thread.sleep(Period.seconds(10).toStandardSeconds().getSeconds() * 1000);
  }

  @PreDestroy
  public void para() {
    ctiManager.stop();
  }

}
