package al.jdi.cti;

import javax.inject.Inject;
import javax.telephony.CallEvent;
import javax.telephony.Connection;
import javax.telephony.ConnectionEvent;
import javax.telephony.MetaEvent;
import javax.telephony.TerminalConnection;
import javax.telephony.callcenter.Agent;
import javax.telephony.callcontrol.CallControlConnectionEvent;
import javax.telephony.callcontrol.CallControlConnectionListener;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import com.avaya.jtapi.tsapi.ITsapiCallEvent;
import com.avaya.jtapi.tsapi.ITsapiCallIDPrivate;
import com.avaya.jtapi.tsapi.LucentTerminal;

class DefaultPredictiveCallListener implements CallControlConnectionListener {

	interface Factory {
		CallControlConnectionListener create(PredictiveListener listener);
	}

	static class DefaultFactory implements
			DefaultPredictiveCallListener.Factory {
		@Inject
		private Logger logger;

		@Override
		public CallControlConnectionListener create(PredictiveListener listener) {
			return new DefaultPredictiveCallListener(logger, listener);
		}
	}

	private final Logger logger;
	private final PredictiveListener predictiveListener;

	DefaultPredictiveCallListener(Logger logger,
			PredictiveListener predictiveListener) {
		this.logger = logger;
		this.predictiveListener = predictiveListener;
	}

	private void logaEvento(Object event) {
		logger.debug(getMethodName()
				+ " "
				+ ToStringBuilder.reflectionToString(event,
						ToStringStyle.SHORT_PREFIX_STYLE).toString());
	}

	private String getMethodName() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		return ste[3].getMethodName();
	}

	@Override
	public void connectionEstablished(CallControlConnectionEvent event) {
		logaEvento(event);
		try {
			predictiveListener.chamadaAtendida(extraiCallId(event));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void connectionDisconnected(ConnectionEvent event) {
		logaEvento(event);

		if (isChamadaSaindoDaFila(event))
			return;

		try {
			predictiveListener.chamadaEncerrada(extraiCallId(event),
					((ITsapiCallEvent) event).getCSTACause());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	boolean isChamadaSaindoDaFila(ConnectionEvent event) {
		return (((ITsapiCallEvent) event).getCSTACause() == 28);
	}

	@Override
	public void callActive(CallEvent event) {
		logaEvento(event);
		try {
			predictiveListener.chamadaIniciada(extraiCallId(event));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private int extraiCallId(CallEvent event) {
		return ((ITsapiCallIDPrivate) event.getCall()).getTsapiCallID();
	}

	@Override
	public void callInvalid(CallEvent event) {
		logaEvento(event);
		try {
			predictiveListener.chamadaInvalida(extraiCallId(event),
					((ITsapiCallEvent) event).getCSTACause());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void callEventTransmissionEnded(CallEvent event) {
		logaEvento(event);
		event.getCall().removeCallListener(this);
	}

	@Override
	public void multiCallMetaMergeEnded(MetaEvent event) {
		logaEvento(event);
	}

	@Override
	public void multiCallMetaMergeStarted(MetaEvent event) {
		logaEvento(event);
	}

	@Override
	public void multiCallMetaTransferEnded(MetaEvent event) {
		logaEvento(event);
	}

	@Override
	public void multiCallMetaTransferStarted(MetaEvent event) {
		logaEvento(event);
	}

	@Override
	public void singleCallMetaProgressEnded(MetaEvent event) {
		logaEvento(event);
	}

	@Override
	public void singleCallMetaProgressStarted(MetaEvent event) {
		logaEvento(event);
	}

	@Override
	public void singleCallMetaSnapshotEnded(MetaEvent event) {
		logaEvento(event);
	}

	@Override
	public void singleCallMetaSnapshotStarted(MetaEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionAlerting(ConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionConnected(ConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionCreated(ConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionFailed(ConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionInProgress(ConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionUnknown(ConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionAlerting(CallControlConnectionEvent event) {
		logaEvento(event);
		logger.debug(
				"Alerting calling {} called {} callingTerminal {} connections {}",
				new Object[] {
						event.getCallingAddress() == null ? "null" : event
								.getCallingAddress().getName(),
						event.getCalledAddress() == null ? "null" : event
								.getCalledAddress().getName(),
						event.getCallingTerminal() == null ? "null" : event
								.getCallingTerminal().getName(),
						event.getCall().getConnections() });
		for (Connection connection : event.getCall().getConnections()) {
			logger.debug("connection {}", connection.getAddress().getName());
			if (connection.getTerminalConnections() != null)
				for (TerminalConnection tc : connection
						.getTerminalConnections()) {
					logger.debug("terminalConnection {}", tc.getTerminal()
							.getName());
					if (((LucentTerminal) tc.getTerminal()).getAgents() != null) {
						for (Agent agent : ((LucentTerminal) tc.getTerminal())
								.getAgents()) {
							logger.debug("agent {}", agent.getAgentID());
							try {
								predictiveListener
										.chamadaNoAgente(extraiCallId(event),
												agent.getAgentID());
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
				}
		}
	}

	@Override
	public void connectionDialing(CallControlConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionDisconnected(CallControlConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionFailed(CallControlConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionInitiated(CallControlConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionNetworkAlerting(CallControlConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionNetworkReached(CallControlConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionOffered(CallControlConnectionEvent event) {
		logaEvento(event);
	}

	@Override
	public void connectionQueued(CallControlConnectionEvent event) {
		logaEvento(event);
		try {
			predictiveListener.chamadaEmFila(extraiCallId(event));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void connectionUnknown(CallControlConnectionEvent event) {
		logaEvento(event);
	}
}
