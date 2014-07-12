package al.jdi.core;

import javax.inject.Inject;

import org.jdial.common.Service;
import org.slf4j.Logger;

class ShutdownHook implements Runnable {

  interface Factory {
    ShutdownHook create(Service... services);
  }

  static class ShutdownHookFactory implements ShutdownHook.Factory {
    @Inject
    private Logger logger;

    @Override
    public ShutdownHook create(Service... services) {
      return new ShutdownHook(logger, services);
    }
  }

  private final Logger logger;
  private final Service[] services;

  ShutdownHook(Logger logger, Service... services) {
    this.logger = logger;
    this.services = services;
  }

  @Override
  public void run() {
    for (Service service : services) {
      logger.info("Parando service {}", service);
      try {
        service.stop();
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
    logger.warn("Aplicacao encerrada.");
  }

}
