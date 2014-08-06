package al.jdi.core;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import al.jdi.common.Service;

public class ShutdownHook implements Runnable {
  interface Factory {
    ShutdownHook create(Service... services);
  }

  static class ShutdownHookFactory implements ShutdownHook.Factory {
    @Override
    public ShutdownHook create(Service... services) {
      return new ShutdownHook(services);
    }
  }

  private static final Logger logger = getLogger(ShutdownHook.class);

  private final Service[] services;

  ShutdownHook(Service... services) {
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
