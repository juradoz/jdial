package al.jdi.cti;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Qualifier;

public class CtiManagerModule {
  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface CtiManagerService {
  }

  @CtiManagerService
  @Produces
  public ExecutorService get() {
    return Executors.newCachedThreadPool(new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, "PredictiveCallRunner");
        thread.setDaemon(true);
        return thread;
      }
    });
  }

  @Named("ctiConfigFileName")
  @Produces
  public String getFileName() {
    return "cti.properties";
  }
}
