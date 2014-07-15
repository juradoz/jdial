package al.jdi.common;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.common.LogProducer.LogClass;

class DefaultEngine extends TimerTask implements Engine {

  static class DefaultEngineFactory implements Engine.Factory {
    @Inject
    @LogClass(clazz = Engine.class)
    private Logger logger;

    @Override
    public Engine create(Runnable owner, Period period, boolean isDaemon, boolean createStarted) {
      return new DefaultEngine(logger, owner, period, isDaemon, createStarted);
    }
  }

  private final Logger logger;
  private final Runnable owner;
  private final Timer timer;
  private final Period period;

  DefaultEngine(Logger logger, Runnable owner, Period period, boolean isDaemon,
      boolean createStarted) {
    this(logger, new Timer("Engine-" + owner.getClass().getSimpleName(), isDaemon), owner, period,
        createStarted);
  }

  DefaultEngine(Logger logger, Timer timer, Runnable owner, Period period, boolean createStarted) {
    this.logger = logger;
    this.owner = owner;
    this.timer = timer;
    this.period = period;
    if (!createStarted)
      return;
    start();
  }

  @Override
  public void start() {
    timer.schedule(this, 0, period.toStandardSeconds().getSeconds() * 1000);
    logger.info("Iniciado engine para {}", owner);
  }

  @Override
  public void stop() {
    timer.cancel();
    logger.info("Parado engine para {}", owner);
  }

  @Override
  public void run() {
    try {
      owner.run();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

}
