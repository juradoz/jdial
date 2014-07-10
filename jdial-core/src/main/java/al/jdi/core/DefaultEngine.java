package al.jdi.core;

import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.Period;
import org.slf4j.Logger;

class DefaultEngine extends TimerTask implements Engine {

	static class DefaultEngineFactory implements Engine.Factory {
		@Override
		public Engine create(Runnable owner, Period period, boolean isDaemon) {
			return new DefaultEngine(owner, period, isDaemon);
		}
	}

	private Logger logger;

	private final Runnable owner;
	private final Timer timer;
	private final Period period;

	DefaultEngine(Runnable owner, Period period, boolean isDaemon) {
		this(new Timer("Engine-" + owner.getClass().getSimpleName(), isDaemon),
				owner, period);
	}

	DefaultEngine(Timer timer, Runnable owner, Period period) {
		this.owner = owner;
		this.timer = timer;
		this.period = period;
	}

	@Override
	public void start() {
		this.timer.schedule(this, 0,
				period.toStandardSeconds().getSeconds() * 1000);
		logger.debug("started engine to {}", owner.getClass().getSimpleName());
	}

	@Override
	public void stop() {
		this.timer.cancel();
		logger.debug("stop engine to {}", owner);
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
