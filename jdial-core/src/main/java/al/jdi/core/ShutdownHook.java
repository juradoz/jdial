package al.jdi.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ShutdownHook implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

  private final Service[] services;

  ShutdownHook(Service... services) {
    this.services = services;
  }

  @Override
  public void run() {
    for (Service service : services) {
      logger.info("Parando service {}", service);
      service.stop();
    }
    logger.warn("Aplicacao encerrada.");
  }

}
