package al.jdi.core.gerenciadorligacoes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

import al.jdi.core.modelo.Ligacao;
import al.jdi.cti.PredictiveListener;

public class GerenciadorLigacoesModule {

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface GerenciadorLigacoesService {
  }

  public interface PredictiveListenerFactory {
    PredictiveListener create(GerenciadorLigacoesImpl owner);
  }

  @Produces
  public Map<PredictiveListener, Ligacao> get() {
    return Collections.synchronizedMap(new LinkedHashMap<PredictiveListener, Ligacao>());
  }

}
