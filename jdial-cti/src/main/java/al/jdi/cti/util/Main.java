package al.jdi.cti.util;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.jdi.cti.CtiManager;
import al.jdi.cti.CtiManager.CtiManagerService;

public class Main {

	@CtiManagerService
	@Inject
	private CtiManager ctiManager;

	public void teste(@Observes ContainerInitialized event)
			throws InterruptedException {
		ctiManager.start();
		Thread.sleep(10000);
	}

	@PreDestroy
	public void para() {
		ctiManager.stop();
	}

}
