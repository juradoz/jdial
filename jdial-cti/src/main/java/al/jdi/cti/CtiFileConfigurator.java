package al.jdi.cti;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang3.math.NumberUtils;

@Singleton
class CtiFileConfigurator {

	private final String serverIp;
	private final int port;
	private final String service;
	private final String login;
	private final String password;
	private final String jtapiPeerName;

	@Inject
	CtiFileConfigurator(@Named("ctiConfigFileName") String fileName) {
		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(fileName);
		}
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream("/" + fileName);
		}
		if (stream == null) {
			stream = System.class.getResourceAsStream(fileName);
		}
		if (stream == null) {
			stream = System.class.getResourceAsStream("/" + fileName);
		}
		if (stream == null) {
			throw new RuntimeException(new FileNotFoundException(fileName));
		}

		Properties defaults = new Properties();
		defaults.setProperty("jtapiPeerName", "");

		Properties properties = new Properties(defaults);
		try {
			properties.load(stream);
			serverIp = properties.getProperty("serverIp");
			port = NumberUtils.toInt(properties.getProperty("port"), 450);
			service = properties.getProperty("service");
			login = properties.getProperty("login");
			password = properties.getProperty("password");
			jtapiPeerName = properties.getProperty("jtapiPeerName");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Named("serverIp")
	@Produces
	public String getServerIp() {
		return serverIp;
	}

	@Named("port")
	@Produces
	public int getPort() {
		return port;
	}

	@Named("service")
	@Produces
	public String getService() {
		return service;
	}

	@Named("login")
	@Produces
	public String getLogin() {
		return login;
	}

	@Named("password")
	@Produces
	public String getPassword() {
		return password;
	}

	@Named("jtapiPeerName")
	@Produces
	public String getJtapiPeerName() {
		return jtapiPeerName;
	}
}
