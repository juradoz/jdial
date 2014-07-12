package al.jdi.common;

import org.joda.time.Period;

public interface Engine extends Service {
  public interface Factory {
    Engine create(Runnable owner, Period period, boolean isDaemon, boolean createStarted);
  }
}
