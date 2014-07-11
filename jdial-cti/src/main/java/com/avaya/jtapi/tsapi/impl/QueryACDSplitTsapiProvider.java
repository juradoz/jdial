package com.avaya.jtapi.tsapi.impl;

import java.util.Date;

import javax.telephony.Address;
import javax.telephony.Call;
import javax.telephony.Connection;
import javax.telephony.InvalidArgumentException;
import javax.telephony.PlatformException;
import javax.telephony.ProviderListener;
import javax.telephony.ProviderObserver;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.Terminal;
import javax.telephony.TerminalConnection;
import javax.telephony.callcenter.ACDAddress;
import javax.telephony.callcenter.ACDManagerAddress;
import javax.telephony.callcenter.RouteAddress;
import javax.telephony.capabilities.AddressCapabilities;
import javax.telephony.capabilities.CallCapabilities;
import javax.telephony.capabilities.ConnectionCapabilities;
import javax.telephony.capabilities.ProviderCapabilities;
import javax.telephony.capabilities.TerminalCapabilities;
import javax.telephony.capabilities.TerminalConnectionCapabilities;
import javax.telephony.privatedata.PrivateData;

import com.avaya.jtapi.tsapi.CallClassifierInfo;
import com.avaya.jtapi.tsapi.ConnectionID;
import com.avaya.jtapi.tsapi.ExtendedDeviceID;
import com.avaya.jtapi.tsapi.ITsapiProviderEx;
import com.avaya.jtapi.tsapi.ITsapiProviderPrivate;
import com.avaya.jtapi.tsapi.LucentV7Provider;
import com.avaya.jtapi.tsapi.TrunkGroupInfo;
import com.avaya.jtapi.tsapi.TsapiInvalidArgumentException;
import com.avaya.jtapi.tsapi.TsapiInvalidStateException;
import com.avaya.jtapi.tsapi.TsapiMethodNotSupportedException;
import com.avaya.jtapi.tsapi.TsapiPrivilegeViolationException;
import com.avaya.jtapi.tsapi.TsapiResourceUnavailableException;
import com.avaya.jtapi.tsapi.impl.core.QueryACDSplitACDAddressImpl;

@SuppressWarnings("deprecation")
public class QueryACDSplitTsapiProvider implements ITsapiProviderEx, PrivateData,
    ITsapiProviderPrivate, LucentV7Provider {

  private final TsapiProvider tsapiProvider;

  public QueryACDSplitTsapiProvider(TsapiProvider tsapiProvider) {
    this.tsapiProvider = tsapiProvider;
  }

  @Override
  public void addObserver(ProviderObserver arg0) throws TsapiResourceUnavailableException {
    tsapiProvider.addObserver(arg0);
  }

  @Override
  public void addProviderListener(ProviderListener arg0) throws ResourceUnavailableException {
    tsapiProvider.addProviderListener(arg0);
  }

  @Override
  public final Call createCall() throws TsapiResourceUnavailableException,
      TsapiInvalidStateException, TsapiPrivilegeViolationException,
      TsapiMethodNotSupportedException {
    return tsapiProvider.createCall();
  }

  @Override
  public boolean equals(Object obj) {
    return tsapiProvider.equals(obj);
  }

  @Override
  public final ACDAddress[] getACDAddresses() throws TsapiMethodNotSupportedException {
    return tsapiProvider.getACDAddresses();
  }

  @Override
  public final ACDManagerAddress[] getACDManagerAddresses() throws TsapiMethodNotSupportedException {
    return tsapiProvider.getACDManagerAddresses();
  }

  @Override
  public final AddressCapabilities getAddressCapabilities() {
    return tsapiProvider.getAddressCapabilities();
  }

  @Override
  public final AddressCapabilities getAddressCapabilities(Terminal terminal)
      throws InvalidArgumentException, PlatformException {
    return tsapiProvider.getAddressCapabilities(terminal);
  }

  @Override
  public final Address[] getAddresses() {
    return tsapiProvider.getAddresses();
  }

  @Override
  public String getAdministeredSwitchSoftwareVersion() {
    return tsapiProvider.getAdministeredSwitchSoftwareVersion();
  }

  @Override
  public final Call getCall(int arg0) {
    return tsapiProvider.getCall(arg0);
  }

  @Override
  public final CallCapabilities getCallCapabilities() {
    return tsapiProvider.getCallCapabilities();
  }

  @Override
  public final CallCapabilities getCallCapabilities(Terminal terminal, Address address)
      throws InvalidArgumentException, PlatformException {
    return tsapiProvider.getCallCapabilities(terminal, address);
  }

  @Override
  public final CallClassifierInfo getCallClassifierInfo() throws TsapiMethodNotSupportedException {
    return tsapiProvider.getCallClassifierInfo();
  }

  @Override
  public final Call[] getCalls() {
    return tsapiProvider.getCalls();
  }

  @Override
  public final ProviderCapabilities getCapabilities() {
    return tsapiProvider.getCapabilities();
  }

  @Override
  public final Connection getConnection(ConnectionID arg0, Address arg1) {
    return tsapiProvider.getConnection(arg0, arg1);
  }

  @Override
  public final ConnectionCapabilities getConnectionCapabilities() {
    return tsapiProvider.getConnectionCapabilities();
  }

  @Override
  public final ConnectionCapabilities getConnectionCapabilities(Terminal terminal, Address address)
      throws InvalidArgumentException, PlatformException {
    return tsapiProvider.getConnectionCapabilities(terminal, address);
  }

  @Override
  public int getCurrentStateOfCallByForceQueryOnTelephonyServer(Call tsapiCall) {
    return tsapiProvider.getCurrentStateOfCallByForceQueryOnTelephonyServer(tsapiCall);
  }

  @Override
  public int getCurrentStateOfCallByForceQueryOnTelephonyServer(int arg0) {
    return tsapiProvider.getCurrentStateOfCallByForceQueryOnTelephonyServer(arg0);
  }

  @Override
  public final String getName() {
    return tsapiProvider.getName();
  }

  @Override
  public ProviderObserver[] getObservers() {
    return tsapiProvider.getObservers();
  }

  @Override
  public String getOfferType() {
    return tsapiProvider.getOfferType();
  }

  @Override
  public final Object getPrivateData() {
    return tsapiProvider.getPrivateData();
  }

  @Override
  public final ProviderCapabilities getProviderCapabilities() {
    return tsapiProvider.getProviderCapabilities();
  }

  @Override
  public final ProviderCapabilities getProviderCapabilities(Terminal terminal)
      throws InvalidArgumentException, PlatformException {
    return tsapiProvider.getProviderCapabilities(terminal);
  }

  @Override
  public ProviderListener[] getProviderListeners() {
    return tsapiProvider.getProviderListeners();
  }

  @Override
  public final RouteAddress[] getRouteableAddresses() {
    return tsapiProvider.getRouteableAddresses();
  }

  @Override
  public final String getServerID() {
    return tsapiProvider.getServerID();
  }

  @Override
  public String getServerType() {
    return tsapiProvider.getServerType();
  }

  @Override
  public final int getState() {
    return tsapiProvider.getState();
  }

  @Override
  public final Date getSwitchDateAndTime() throws TsapiMethodNotSupportedException {
    return tsapiProvider.getSwitchDateAndTime();
  }

  @Override
  public String getSwitchSoftwareVersion() {
    return tsapiProvider.getSwitchSoftwareVersion();
  }

  @Override
  public final Terminal getTerminal(ExtendedDeviceID arg0) {
    return tsapiProvider.getTerminal(arg0);
  }

  @Override
  public final Terminal getTerminal(String arg0) throws TsapiInvalidArgumentException {
    return tsapiProvider.getTerminal(arg0);
  }

  @Override
  public final TerminalCapabilities getTerminalCapabilities() {
    return tsapiProvider.getTerminalCapabilities();
  }

  @Override
  public final TerminalCapabilities getTerminalCapabilities(Terminal terminal)
      throws InvalidArgumentException, PlatformException {
    return tsapiProvider.getTerminalCapabilities(terminal);
  }

  @Override
  public final TerminalConnection getTerminalConnection(ConnectionID arg0, Terminal arg1) {
    return tsapiProvider.getTerminalConnection(arg0, arg1);
  }

  @Override
  public final TerminalConnectionCapabilities getTerminalConnectionCapabilities() {
    return tsapiProvider.getTerminalConnectionCapabilities();
  }

  @Override
  public final TerminalConnectionCapabilities getTerminalConnectionCapabilities(Terminal terminal)
      throws InvalidArgumentException, PlatformException {
    return tsapiProvider.getTerminalConnectionCapabilities(terminal);
  }

  @Override
  public final Terminal[] getTerminals() {
    return tsapiProvider.getTerminals();
  }

  @Override
  public final TrunkGroupInfo getTrunkGroupInfo(String trunkAccessCode)
      throws TsapiMethodNotSupportedException {
    return tsapiProvider.getTrunkGroupInfo(trunkAccessCode);
  }

  @Override
  public final int getTsapiState() {
    return tsapiProvider.getTsapiState();
  }

  @Override
  public final String getVendor() {
    return tsapiProvider.getVendor();
  }

  @Override
  public final byte[] getVendorVersion() {
    return tsapiProvider.getVendorVersion();
  }

  @Override
  public final int hashCode() {
    return tsapiProvider.hashCode();
  }

  @Override
  public void removeObserver(ProviderObserver arg0) {
    tsapiProvider.removeObserver(arg0);
  }

  @Override
  public void removeProviderListener(ProviderListener arg0) {
    tsapiProvider.removeProviderListener(arg0);
  }

  @Override
  public final String requestPrivileges() throws TsapiInvalidArgumentException {
    return tsapiProvider.requestPrivileges();
  }

  @Override
  public final Object sendPrivateData(Object arg0) {
    return tsapiProvider.sendPrivateData(arg0);
  }

  @Override
  public final void setAdviceOfCharge(boolean flag) throws TsapiMethodNotSupportedException {
    tsapiProvider.setAdviceOfCharge(flag);
  }

  @Override
  public final void setDebugPrinting(boolean enable) {
    tsapiProvider.setDebugPrinting(enable);
  }

  @Override
  public final void setHeartbeatInterval(short heartbeatInterval)
      throws TsapiInvalidArgumentException {
    tsapiProvider.setHeartbeatInterval(heartbeatInterval);
  }

  @Override
  public final void setPrivateData(Object arg0) {
    tsapiProvider.setPrivateData(arg0);
  }

  @Override
  public final void setPrivileges(String xmlData) throws TsapiInvalidArgumentException {
    tsapiProvider.setPrivileges(xmlData);
  }

  @Override
  public void setSessionTimeout(int timeout) {
    tsapiProvider.setSessionTimeout(timeout);
  }

  @Override
  public final void shutdown() {
    tsapiProvider.shutdown();
  }

  @Override
  public String toString() {
    return tsapiProvider.toString();
  }

  @Override
  public final void updateAddresses() {
    tsapiProvider.updateAddresses();
  }

  @Override
  public Address getAddress(String number) throws InvalidArgumentException {
    Address address = tsapiProvider.getAddress(number);
    if (!(address instanceof ACDAddress))
      return address;

    ACDAddress acdAddress = (ACDAddress) address;

    LucentACDAddressImpl lucentACDAddressImpl = (LucentACDAddressImpl) address;

    return new QueryACDSplitACDAddressImpl(lucentACDAddressImpl.tsDevice, acdAddress);
  }

  @Override
  public Address getAddress(ExtendedDeviceID tsapiDeviceID) {
    Address address = tsapiProvider.getAddress(tsapiDeviceID);
    if (!(address instanceof ACDAddress))
      return address;

    ACDAddress acdAddress = (ACDAddress) address;

    LucentACDAddressImpl lucentACDAddressImpl = (LucentACDAddressImpl) address;

    return new QueryACDSplitACDAddressImpl(lucentACDAddressImpl.tsDevice, acdAddress);
  }

}
