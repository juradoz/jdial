package com.avaya.jtapi.tsapi.impl.core;

import javax.telephony.AddressListener;
import javax.telephony.AddressObserver;
import javax.telephony.Call;
import javax.telephony.CallListener;
import javax.telephony.CallObserver;
import javax.telephony.Connection;
import javax.telephony.InvalidArgumentException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PlatformException;
import javax.telephony.PrivilegeViolationException;
import javax.telephony.Provider;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.Terminal;
import javax.telephony.callcenter.ACDAddress;
import javax.telephony.callcenter.ACDManagerAddress;
import javax.telephony.callcenter.Agent;
import javax.telephony.capabilities.AddressCapabilities;

import com.avaya.jtapi.tsapi.QueryACDSplitACDAddress;
import com.avaya.jtapi.tsapi.TsapiMethodNotSupportedException;
import com.avaya.jtapi.tsapi.csta1.LucentQueryAcdSplit;
import com.avaya.jtapi.tsapi.csta1.LucentQueryAcdSplitConfEvent;

@SuppressWarnings("deprecation")
public class QueryACDSplitACDAddressImpl implements QueryACDSplitACDAddress {

  private final TSDevice tsDevice;
  private final ACDAddress acdAddress;

  public QueryACDSplitACDAddressImpl(TSDevice tsDevice, ACDAddress acdAddress) {
    this.tsDevice = tsDevice;
    this.acdAddress = acdAddress;
  }

  public void addAddressListener(AddressListener listener) throws ResourceUnavailableException,
      MethodNotSupportedException {
    acdAddress.addAddressListener(listener);
  }

  public void addCallListener(CallListener listener) throws ResourceUnavailableException,
      MethodNotSupportedException {
    acdAddress.addCallListener(listener);
  }

  public void addCallObserver(CallObserver observer, boolean remain)
      throws ResourceUnavailableException, PrivilegeViolationException, MethodNotSupportedException {
    acdAddress.addCallObserver(observer, remain);
  }

  public void addCallObserver(CallObserver observer) throws ResourceUnavailableException,
      MethodNotSupportedException {
    acdAddress.addCallObserver(observer);
  }

  public void addObserver(AddressObserver observer) throws ResourceUnavailableException,
      MethodNotSupportedException {
    acdAddress.addObserver(observer);
  }

  public ACDManagerAddress getACDManagerAddress() throws MethodNotSupportedException {
    return acdAddress.getACDManagerAddress();
  }

  public AddressCapabilities getAddressCapabilities(Terminal terminal)
      throws InvalidArgumentException, PlatformException {
    return acdAddress.getAddressCapabilities(terminal);
  }

  public AddressListener[] getAddressListeners() {
    return acdAddress.getAddressListeners();
  }

  public CallListener[] getCallListeners() {
    return acdAddress.getCallListeners();
  }

  public CallObserver[] getCallObservers() {
    return acdAddress.getCallObservers();
  }

  public AddressCapabilities getCapabilities() {
    return acdAddress.getCapabilities();
  }

  public Connection[] getConnections() {
    return acdAddress.getConnections();
  }

  public Agent[] getLoggedOnAgents() throws MethodNotSupportedException {
    return acdAddress.getLoggedOnAgents();
  }

  public String getName() {
    return acdAddress.getName();
  }

  public int getNumberQueued() throws MethodNotSupportedException {
    return acdAddress.getNumberQueued();
  }

  public AddressObserver[] getObservers() {
    return acdAddress.getObservers();
  }

  public Call getOldestCallQueued() throws MethodNotSupportedException {
    return acdAddress.getOldestCallQueued();
  }

  public Provider getProvider() {
    return acdAddress.getProvider();
  }

  public int getQueueWaitTime() throws MethodNotSupportedException {
    return acdAddress.getQueueWaitTime();
  }

  public int getRelativeQueueLoad() throws MethodNotSupportedException {
    return acdAddress.getRelativeQueueLoad();
  }

  public Terminal[] getTerminals() {
    return acdAddress.getTerminals();
  }

  public void removeAddressListener(AddressListener listener) {
    acdAddress.removeAddressListener(listener);
  }

  public void removeCallListener(CallListener listener) {
    acdAddress.removeCallListener(listener);
  }

  public void removeCallObserver(CallObserver observer) {
    acdAddress.removeCallObserver(observer);
  }

  public void removeObserver(AddressObserver observer) {
    acdAddress.removeObserver(observer);
  }

  @Override
  public int getAvailableAgents() throws TsapiMethodNotSupportedException {
    tsDevice.recreate();

    if (tsDevice.provider.isLucent())
      try {
        final LucentQueryAcdSplit lqas = new LucentQueryAcdSplit(getName());
        final Object lqasConf = tsDevice.provider.sendPrivateData(lqas.makeTsapiPrivate());
        if (lqasConf instanceof LucentQueryAcdSplitConfEvent)
          return ((LucentQueryAcdSplitConfEvent) lqasConf).getAvailableAgents();
        throw new TsapiMethodNotSupportedException(4, 0, "unsupported by driver");
      } catch (final Exception e) {
        throw new TsapiMethodNotSupportedException(4, 0, "unsupported by driver");
      }

    return 0;
  }

  @Override
  public int getAgentsLoggedIn() throws TsapiMethodNotSupportedException {
    tsDevice.recreate();

    if (tsDevice.provider.isLucent())
      try {
        final LucentQueryAcdSplit lqas = new LucentQueryAcdSplit(getName());
        final Object lqasConf = tsDevice.provider.sendPrivateData(lqas.makeTsapiPrivate());
        if (lqasConf instanceof LucentQueryAcdSplitConfEvent)
          return ((LucentQueryAcdSplitConfEvent) lqasConf).getAgentsLoggedIn();
        throw new TsapiMethodNotSupportedException(4, 0, "unsupported by driver");
      } catch (final Exception e) {
        throw new TsapiMethodNotSupportedException(4, 0, "unsupported by driver");
      }

    return 0;
  }

}
