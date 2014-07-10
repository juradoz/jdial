package al.jdi.core.util;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.core.Engine;

public class Main implements Runnable {

	@Inject
	private Logger logger;
	@Inject
	private Engine.Factory factory;

	@PostConstruct
	public void inicia() {
		logger.info("iniciando...");
		factory.create(this, Period.seconds(1), false).start();
		logger.info("iniciado!");
	}
	
	public void teste(@Observes ContainerInitialized event){
		logger.info("Start!");
	}

	@Override
	public void run() {
		logger.info("Mensagem...");
	}
}
