package al.jdi.cti;

import javax.telephony.Connection;
import javax.telephony.callcenter.CallCenterCall;

import com.avaya.jtapi.tsapi.LucentAddress;
import com.avaya.jtapi.tsapi.LucentCall;
import com.avaya.jtapi.tsapi.UserToUserInfo;

class DefaultPredictiveCallRunner implements Runnable {

  interface Factory {
    DefaultPredictiveCallRunner create(CallCenterCall call, LucentAddress lucentAddress,
        String destino, String userInfo, int maxRings, int answerTreatment,
        PredictiveListener predictiveListener);
  }

  static class DefaultPredictiveCallRunnerFactory implements DefaultPredictiveCallRunner.Factory {
    @Override
    public DefaultPredictiveCallRunner create(CallCenterCall call, LucentAddress lucentAddress,
        String destino, String userInfo, int maxRings, int answerTreatment,
        PredictiveListener predictiveListener) {
      return new DefaultPredictiveCallRunner(call, lucentAddress, destino, userInfo, maxRings,
          answerTreatment, predictiveListener);
    }
  }

  private final CallCenterCall call;
  private final LucentAddress lucentAddress;
  private final String destino;
  private final String userInfo;
  private final int maxRings;
  private final int answerTreatment;
  private final PredictiveListener predictiveListener;

  DefaultPredictiveCallRunner(CallCenterCall call, LucentAddress lucentAddress, String destino,
      String userInfo, int maxRings, int answerTreatment, PredictiveListener predictiveListener) {
    this.call = call;
    this.lucentAddress = lucentAddress;
    this.destino = destino;
    this.userInfo = userInfo;
    this.maxRings = maxRings;
    this.answerTreatment = answerTreatment;
    this.predictiveListener = predictiveListener;
  }

  @Override
  public void run() {
    try {
      ((LucentCall) call).connectPredictive(null, lucentAddress, destino, Connection.ALERTING,
          maxRings, answerTreatment, CallCenterCall.ENDPOINT_ANY, false, userInfo == null ? null
              : new UserToUserInfo(userInfo));
    } catch (Exception e) {
      predictiveListener.chamadaErro(e);
    }
  }

}
