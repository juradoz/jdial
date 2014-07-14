package al.jdi.core.gerenciadorligacoes;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;

import al.jdi.core.modelo.Ligacao;
import al.jdi.cti.PredictiveListener;

public class GerenciadorLigacoesModule {

  public interface PredictiveListenerFactory {
    PredictiveListener create(GerenciadorLigacoesImpl owner);
  }

  @Produces
  public Map<PredictiveListener, Ligacao> get() {
    return Collections.synchronizedMap(new LinkedHashMap<PredictiveListener, Ligacao>());
  }

}
