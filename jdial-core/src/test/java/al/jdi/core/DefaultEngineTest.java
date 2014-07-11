package al.jdi.core;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Timer;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

public class DefaultEngineTest {

  private static final Period period = Period.seconds(10);
  @Mock
  private Logger logger;
  @Mock
  private Timer timer;
  @Mock
  private Runnable owner;

  private DefaultEngine defaultEngine;

  @Before
  public void setUp() {
    initMocks(this);
    defaultEngine = new DefaultEngine(logger, timer, owner, period);
  }

  @Test
  public void startShouldSchedule() throws Exception {
    defaultEngine.start();
    verify(timer).schedule(defaultEngine, 0, period.toStandardSeconds().getSeconds() * 1000);
  }

  @Test
  public void stopShouldCancel() throws Exception {
    defaultEngine.stop();
    verify(timer).cancel();
  }

  @Test
  public void runShouldCallOwner() throws Exception {
    defaultEngine.run();
    verify(owner).run();
  }

  @Test
  public void runShouldHandleOwnerException() throws Exception {
    doThrow(new RuntimeException()).when(owner).run();
    defaultEngine.run();
    verify(owner).run();
  }

}
