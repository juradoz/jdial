package al.jdi.cti;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

class CtiConfigFileNameProducer {
	@Produces
	@Named("ctiConfigFileName")
	public String getFileName() {
		return "cti.properties";
	}
}
