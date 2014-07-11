package al.jdi.cti;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.telephony.Provider;
import javax.telephony.ProviderListener;

import org.jdial.common.Service;

public interface CtiManager extends Service {

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface CtiManagerService {
  }

  boolean gotProvider();

  void addListener(ProviderListener listener);

  void removeListener(ProviderListener listener);

  Provider getProvider();

}
