package al.jdi.cti;

import javax.telephony.Provider;
import javax.telephony.ProviderListener;

import org.jdial.common.Service;

public interface CtiManager extends Service {

  boolean gotProvider();

  void addListener(ProviderListener listener);

  void removeListener(ProviderListener listener);

  Provider getProvider();

}
