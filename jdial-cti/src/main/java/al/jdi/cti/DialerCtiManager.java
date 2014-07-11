package al.jdi.cti;


public interface DialerCtiManager extends CtiManager {

  int getAgentesLivres(String acd);

  void makePredictiveCall(String origem, String destino, int maxRings,
      TratamentoSecretariaEletronica tratamentoSecretariaEletronica, String userInfo,
      PredictiveListener predictiveListener);

}
