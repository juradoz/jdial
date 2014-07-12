package al.jdi.cti;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.concurrent.ExecutorService;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.telephony.Address;
import javax.telephony.InvalidArgumentException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.Provider;
import javax.telephony.ProviderListener;
import javax.telephony.callcenter.ACDAddress;
import javax.telephony.callcenter.Agent;
import javax.telephony.callcenter.CallCenterCall;
import javax.telephony.callcontrol.CallControlConnectionListener;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import al.jdi.cti.CtiManagerModule.CtiManagerService;
import al.jdi.cti.DialerCtiManagerModule.DialerCtiManagerService;

import com.avaya.jtapi.tsapi.LucentAddress;
import com.avaya.jtapi.tsapi.QueryACDSplitACDAddress;
import com.avaya.jtapi.tsapi.TsapiMethodNotSupportedException;

@Singleton
@Default
@DialerCtiManagerService
class DefaultDialerCtiManager implements DialerCtiManager {

  private final Logger logger;
  private final CtiManager ctiManager;
  private final ExecutorService executorService;
  private final DefaultPredictiveCallListener.Factory predictiveCallListenerFactory;
  private final DefaultPredictiveCallRunner.Factory predictiveCallRunnerFactory;

  @Inject
  DefaultDialerCtiManager(Logger logger, @CtiManagerService CtiManager ctiManager,
      @CtiManagerService ExecutorService executorService,
      DefaultPredictiveCallListener.Factory predictiveCallListenerFactory,
      DefaultPredictiveCallRunner.Factory predictiveCallRunnerFactory) {
    this.logger = logger;
    this.ctiManager = ctiManager;
    this.executorService = executorService;
    this.predictiveCallListenerFactory = predictiveCallListenerFactory;
    this.predictiveCallRunnerFactory = predictiveCallRunnerFactory;
    logger.debug("Starting {}...", this);
  }

  @Override
  public int getAgentesLivres(String acd) {
    Address address;
    try {
      address = ctiManager.getProvider().getAddress(acd);
    } catch (InvalidArgumentException e) {
      throw new RuntimeException(e);
    }

    if (address instanceof QueryACDSplitACDAddress) {

      QueryACDSplitACDAddress queryACDSplitAddress = (QueryACDSplitACDAddress) address;
      try {
        return queryACDSplitAddress.getAvailableAgents();
      } catch (TsapiMethodNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }

    ACDAddress acdAddress = (ACDAddress) address;
    Agent[] loggedOnAgents;
    try {
      loggedOnAgents = acdAddress.getLoggedOnAgents();
    } catch (MethodNotSupportedException e) {
      throw new RuntimeException(e);
    }

    if (loggedOnAgents == null)
      return 0;

    return filter(having(on(Agent.class).getState(), equalTo(Agent.READY)), loggedOnAgents).size();
  }

  @Override
  public void makePredictiveCall(String origem, String destino, int maxRings,
      TratamentoSecretariaEletronica tratamentoSecretariaEletronica, String userInfo,
      PredictiveListener predictiveListener) {
    try {
      CallCenterCall call = (CallCenterCall) ctiManager.getProvider().createCall();
      LucentAddress lucentAddress = (LucentAddress) ctiManager.getProvider().getAddress(origem);

      CallControlConnectionListener listener =
          predictiveCallListenerFactory.create(predictiveListener);
      call.addCallListener(listener);

      executorService.execute(predictiveCallRunnerFactory.create(call, lucentAddress, destino,
          userInfo, maxRings, tratamentoSecretariaEletronica.getValor(), predictiveListener));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      predictiveListener.chamadaErro(e);
    }
  }

  @Override
  public boolean gotProvider() {
    return ctiManager.gotProvider();
  }

  @Override
  public void addListener(ProviderListener listener) {
    ctiManager.addListener(listener);
  }

  @Override
  public void removeListener(ProviderListener listener) {
    ctiManager.removeListener(listener);
  }

  @Override
  public Provider getProvider() {
    return ctiManager.getProvider();
  }

  @Override
  public void start() {
    ctiManager.start();
    logger.info("Started successfuly {}", this);
  }

  @Override
  public void stop() {
    ctiManager.stop();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
