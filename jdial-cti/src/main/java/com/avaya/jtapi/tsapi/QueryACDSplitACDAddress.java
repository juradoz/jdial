package com.avaya.jtapi.tsapi;

import javax.telephony.callcenter.ACDAddress;

public interface QueryACDSplitACDAddress extends ACDAddress {

	int getAvailableAgents() throws TsapiMethodNotSupportedException;

	int getAgentsLoggedIn() throws TsapiMethodNotSupportedException;
}
