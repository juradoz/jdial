package al.jdi.cti;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

public interface DialerCtiManager extends CtiManager {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({TYPE, PARAMETER, FIELD})
  @Qualifier
  public @interface DialerCtiManagerService {
  }

  int getAgentesLivres(String acd);

  void makePredictiveCall(String origem, String destino, int maxRings,
      TratamentoSecretariaEletronica tratamentoSecretariaEletronica, String userInfo,
      PredictiveListener predictiveListener);

}
