package al.jdi.cti;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

class CtiFileConfigurator {

  private final String login;
  private final String password;
  private final String jtapiPeerName;

  @Inject
  CtiFileConfigurator(@Named("ctiConfigFileName") String fileName) {
    InputStream stream = null;
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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
      login = properties.getProperty("login");
      password = properties.getProperty("password");
      jtapiPeerName = properties.getProperty("jtapiPeerName");

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
