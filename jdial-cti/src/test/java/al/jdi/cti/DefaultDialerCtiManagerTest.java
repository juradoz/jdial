package al.jdi.cti;


import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.concurrent.ExecutorService;

import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidStateException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PrivilegeViolationException;
import javax.telephony.Provider;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.callcenter.ACDAddress;
import javax.telephony.callcenter.Agent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import com.avaya.jtapi.tsapi.LucentAddress;
import com.avaya.jtapi.tsapi.LucentCall;
import com.avaya.jtapi.tsapi.LucentTerminal;
import com.avaya.jtapi.tsapi.QueryACDSplitACDAddress;
import com.avaya.jtapi.tsapi.TsapiMethodNotSupportedException;

public class DefaultDialerCtiManagerTest {

  private static final String ORIGEM = "ORIGEM";
  private static final String ACD = "ACD";

  private DefaultDialerCtiManager dialerCtiManagerImpl;

  @Mock
  private CtiManager ctiManager;
  @Mock
  private Provider provider;
  @Mock
  private ACDAddress acdAddress;
  @Mock
  private QueryACDSplitACDAddress queryACDSplitACDAddress;
  @Mock
  private Agent agent;
  @Mock
  private LucentCall call;
  @Mock
  private LucentTerminal terminal;
  @Mock
  private LucentAddress address;
  @Mock
  private ExecutorService executorService;
  @Mock
  private DefaultPredictiveCallListener.Factory predictiveCallListenerFactory;
  @Mock
  private DefaultPredictiveCallRunner.Factory predictiveCallRunnerFactory;
  @Mock
  private Logger logger;

  @Before
  public void setUp() throws InvalidArgumentException, MethodNotSupportedException,
      ResourceUnavailableException, InvalidStateException, PrivilegeViolationException {
    initMocks(this);
    when(ctiManager.getProvider()).thenReturn(provider);
    when(provider.getAddress(ACD)).thenReturn(acdAddress);
    when(acdAddress.getLoggedOnAgents()).thenReturn((Agent[]) asList(agent).toArray());
    when(agent.getState()).thenReturn(Agent.READY);
    when(provider.createCall()).thenReturn(call);
    when(provider.getTerminal(ORIGEM)).thenReturn(terminal);
    when(provider.getAddress(ORIGEM)).thenReturn(address);
    dialerCtiManagerImpl =
        new DefaultDialerCtiManager(logger, ctiManager, executorService,
            predictiveCallListenerFactory, predictiveCallRunnerFactory);
  }

  @Test
  public void getAgentesLivresACDAddressDeveriaRetornar1() {
    assertThat(dialerCtiManagerImpl.getAgentesLivres(ACD), is(equalTo(1)));
  }

  @Test
  public void getAgentesLivresACDAddressDeveriaRetornar0SeAgentNotReady() {
    when(agent.getState()).thenReturn(Agent.NOT_READY);
    assertThat(dialerCtiManagerImpl.getAgentesLivres(ACD), is(equalTo(0)));
  }

  @Test
  public void getAgentesLivresQueryACDSplitACDAddressDeveriaRetornar1()
      throws TsapiMethodNotSupportedException, InvalidArgumentException {
    when(provider.getAddress(ACD)).thenReturn(queryACDSplitACDAddress);
    when(queryACDSplitACDAddress.getAvailableAgents()).thenReturn(1);
    assertThat(dialerCtiManagerImpl.getAgentesLivres(ACD), is(equalTo(1)));
  }

  @Test
  public void getAgentesLivresQueryACDSplitACDAddressDeveriaRetornar0SeAgentNotReady()
      throws TsapiMethodNotSupportedException, InvalidArgumentException {
    when(provider.getAddress(ACD)).thenReturn(queryACDSplitACDAddress);
    when(queryACDSplitACDAddress.getAvailableAgents()).thenReturn(0);
    assertThat(dialerCtiManagerImpl.getAgentesLivres(ACD), is(equalTo(0)));
  }

}
