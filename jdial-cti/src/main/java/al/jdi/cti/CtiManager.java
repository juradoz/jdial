package al.jdi.cti;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.telephony.Provider;
import javax.telephony.ProviderListener;

import org.jdial.common.Service;

public interface CtiManager extends Service {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({TYPE, PARAMETER, FIELD})
  @Qualifier
  public @interface CtiManagerService {
  }

  boolean gotProvider();

  void addListener(ProviderListener listener);

  void removeListener(ProviderListener listener);

  Provider getProvider();

}
