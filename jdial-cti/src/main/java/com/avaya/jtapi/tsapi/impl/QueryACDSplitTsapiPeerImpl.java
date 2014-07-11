package com.avaya.jtapi.tsapi.impl;

import javax.telephony.Provider;
import javax.telephony.ProviderUnavailableException;

import com.avaya.jtapi.tsapi.ITsapiPeer;

public class QueryACDSplitTsapiPeerImpl implements ITsapiPeer {

	private final TsapiPeerImpl tsapiPeerImpl = new TsapiPeerImpl();

	@Override
	public String getName() {
		return tsapiPeerImpl.getName();
	}

	@Override
	public Provider getProvider(String providerString)
			throws ProviderUnavailableException {
		Provider provider = tsapiPeerImpl.getProvider(providerString);
		return new QueryACDSplitTsapiProvider((TsapiProvider) provider);
	}

	@Override
	public String[] getServices() {
		return tsapiPeerImpl.getServices();
	}

	@Override
	public void addVendor(String vendorName, String versions) {
		tsapiPeerImpl.addVendor(vendorName, versions);
	}

}
