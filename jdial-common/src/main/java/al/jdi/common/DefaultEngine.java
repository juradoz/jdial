package al.jdi.common;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.Period;
import org.slf4j.Logger;

class DefaultEngine extends TimerTask implements Engine {

  static class DefaultEngineFactory implements Engine.Factory {
    @Override
    public Engine create(Runnable owner, Period period, boolean isDaemon, boolean createStarted) {
      return new DefaultEngine(owner, period, isDaemon, createStarted);
    }
  }

  private static final Logger logger = getLogger(DefaultEngine.class);

  private final Runnable owner;
  private final Timer timer;
  private final Period period;

  DefaultEngine(Runnable owner, Period period, boolean isDaemon, boolean createStarted) {
    this(new Timer("Engine-" + owner.getClass().getSimpleName(), isDaemon), owner, period,
        createStarted);
  }

  DefaultEngine(Timer timer, Runnable owner, Period period, boolean createStarted) {
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
