package al.jdi.cti;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
class ExecutorServiceProvider {

  private final ExecutorService executorService;

  ExecutorServiceProvider() {
    executorService = Executors.newCachedThreadPool(new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, "PredictiveCallRunner");
        thread.setDaemon(true);
        return thread;
      }
    });
  }

  @Produces
  public ExecutorService get() {
    return executorService;
  }

}
