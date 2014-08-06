package al.jdi.cti;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.telephony.JtapiPeer;
import javax.telephony.JtapiPeerFactory;
import javax.telephony.Provider;
import javax.telephony.ProviderEvent;
import javax.telephony.ProviderListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.cti.CtiManagerModule.CtiManagerService;

import com.avaya.jtapi.tsapi.TsapiPlatformException;

@Singleton
@CtiManagerService
class DefaultCtiManager implements CtiManager, ProviderListener, Runnable {

  private final Set<ProviderListener> providerListeners = Collections
      .synchronizedSet(new HashSet<ProviderListener>());

  private static final Logger logger = getLogger(DefaultCtiManager.class);

  private final String login;
  private final String password;
  private final JtapiPeer jtapiPeer;
  private final Period providerTimeout = Period.seconds(10);
  private final Engine.Factory engineFactory;

  private Engine engine;
  private Provider provider;
  private boolean gotProvider = false;
  private boolean shutdown = false;
  private String providerString;

  @Inject
  DefaultCtiManager(Engine.Factory engineFactory, @Named("login") String login,
      @Named("password") String password, @Named("jtapiPeerName") String jtapiPeerName) {
    this.login = login;
    this.password = password;
    this.engineFactory = engineFactory;
    try {
      this.jtapiPeer = JtapiPeerFactory.getJtapiPeer(jtapiPeerName);
      logger.debug("Iniciando {}...", this);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  String getStringConexao(String service, String login, String password) {
    String string = String.format("%s;loginID=%s;passwd=%s", service, login, password);

    logger.debug("Vai conectar em {}", string);

    return string;
  }

  @Override
  public void addListener(ProviderListener listener) {
    try {

      if (gotProvider)
        provider.addProviderListener(listener);
      synchronized (providerListeners) {
        providerListeners.add(listener);
      }
      logger.debug("Adicionado providerListener {}", listener);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  void validaProvider() {
    if (gotProvider || shutdown)
      return;
    logger.debug("provider unavaliable. Trying to acquire...");
    do {
      try {
        provider = jtapiPeer.getProvider(providerString);
        provider.addProviderListener(this);

        synchronized (this) {
          if (!gotProvider)
            try {
              wait(providerTimeout.getMillis());
            } catch (InterruptedException e) {
              logger.error(e.getMessage(), e);
            }

          if (gotProvider) {
            logger.debug("got provider successfully");
            reAddProviderListeners();
          }
        }
      } catch (TsapiPlatformException e) {
        logger.warn("error acquiring provider: {}. Will try again in 10 secs...", e.getMessage());
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e1) {
          throw new RuntimeException(e1);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } while (!gotProvider);
  }

  void reAddProviderListeners() {
    synchronized (providerListeners) {
      for (ProviderListener providerListener : providerListeners)
        addListener(providerListener);
    }
  }

  @Override
  public void removeListener(ProviderListener listener) {
    try {
      provider.removeProviderListener(listener);
      synchronized (providerListeners) {
        providerListeners.remove(listener);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    if (engine == null)
      throw new IllegalStateException("Already stopped");

    logger.debug("shutting down...");
    shutdown = true;
    engine.stop();
    engine = null;
    provider.shutdown();
    logger.warn("shutted down successfully");
  }

  void setGotProvider(boolean gotProvider) {
    this.gotProvider = gotProvider;
  }

  @Override
  public void providerEventTransmissionEnded(ProviderEvent arg0) {
    logger.warn("providerEventTransmissionEnded");
    gotProvider = false;
  }

  @Override
  public void providerInService(ProviderEvent arg0) {
    logger.warn("providerInService");
    synchronized (this) {
      gotProvider = true;
      notifyAll();
    }
  }

  @Override
  public void providerOutOfService(ProviderEvent arg0) {
    logger.warn("providerOutOfService");
    gotProvider = false;
  }

  @Override
  public void providerShutdown(ProviderEvent arg0) {
    logger.warn("providerShutdown");
    gotProvider = false;
  }

  @Override
  public Provider getProvider() {
    if (!gotProvider || shutdown)
      throw new RuntimeException("provider unavailable");
    return provider;
  }

  @Override
  public void run() {
    getServices();
    validaProvider();
  }

  private void getServices() {
    if (StringUtils.isBlank(providerString)) {
      String service = Arrays.asList(jtapiPeer.getServices()).get(0);
      providerString = getStringConexao(service, login, password);
    }
  }

  public boolean gotProvider() {
    return gotProvider;
  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException("Already started");

    engine = engineFactory.create(this, Period.seconds(10), true, true);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
