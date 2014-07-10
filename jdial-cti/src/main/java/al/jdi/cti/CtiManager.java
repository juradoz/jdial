package al.jdi.cti;

import javax.telephony.Provider;
import javax.telephony.ProviderListener;

public interface CtiManager {
	
	boolean gotProvider();

	void addListener(ProviderListener listener);

	void removeListener(ProviderListener listener);

	Provider getProvider();

	String getVersion();
	
}
