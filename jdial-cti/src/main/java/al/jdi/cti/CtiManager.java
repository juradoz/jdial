package al.jdi.cti;

import javax.telephony.Provider;
import javax.telephony.ProviderListener;

import al.jdi.core.Service;

public interface CtiManager extends Service {

	public interface Factory {
		CtiManager create(String serverIp, int port, String service,
				String login, String password, String jtapiPeerName);
	}

	boolean gotProvider();

	void addListener(ProviderListener listener);

	void removeListener(ProviderListener listener);

	Provider getProvider();

}
