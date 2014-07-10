package al.jdi.core.util;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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
		factory.create(this, Period.seconds(1), false);
	}

	@Override
	public void run() {
		logger.info("Mensagem...");
	}
}
