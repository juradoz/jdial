package al.jdi.cti;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;

public class Main {

	@Inject
	private Logger logger;
	@Inject
	private CtiManager.Factory factory;

	private CtiManager ctiManager;

	public void teste(@Observes ContainerInitialized event) {
		logger.info("Start!");
		ctiManager = factory.create("172.16.2.100", 450,
				"AVAYA#AVAYA_ECS#CSTA#THOR", "ctiuser", "ctiuser", null);
	}

	@PostConstruct
	public void inicia() {
		ctiManager.start();
	}

	@PreDestroy
	public void para() {
		ctiManager.stop();
	}

}
