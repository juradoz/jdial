package net.danieljurado.dialer.gerenciadorligacoes;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;

import net.danieljurado.dialer.Service;
import net.danieljurado.dialer.modelo.Ligacao;
import al.jdi.cti.PredictiveListener;

public class GerenciadorLigacoesModule {

  @Retention(RUNTIME)
  @Target({PARAMETER})
  public @interface GerenciadorLigacoesService {
  }

  public interface PredictiveListenerFactory {
    PredictiveListener create(GerenciadorLigacoesImpl owner);
  }

  protected void configure() {
    install(new FactoryModuleBuilder().implement(PredictiveListener.class,
        PredictiveListenerImpl.class).build(PredictiveListenerFactory.class));
    bind(GerenciadorLigacoes.class).to(GerenciadorLigacoesImpl.class);
    bind(Service.class).annotatedWith(GerenciadorLigacoesService.class).to(
        GerenciadorLigacoesImpl.class);
  }

  @Produces
  public Map<PredictiveListener, Ligacao> get() {
    return Collections.synchronizedMap(new LinkedHashMap<PredictiveListener, Ligacao>());
  }

}
