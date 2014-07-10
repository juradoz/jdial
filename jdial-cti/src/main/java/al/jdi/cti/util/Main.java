package al.jdi.cti.util;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.jdi.cti.CtiManager;

public class Main {
	@Inject
	private CtiManager.Factory ctiManagerFactory;

	public void teste(@Observes ContainerInitialized event) {
		CtiManager ctiManager = ctiManagerFactory
				.create("172.16.2.100", 450, "AVAYA#AVAYA_ECS#CSTA#THOR",
						"ctiuser", "ctiuser",
						"net.danieljurado.ctimanager.impl.avaya.QueryACDSplitTsapiPeerImpl");
		ctiManager.start();
	}
}
