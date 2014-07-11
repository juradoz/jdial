package net.danieljurado.dialer.configuracoes;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;

import net.danieljurado.dialer.Service;

import org.joda.time.Period;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Definicao;

public class ConfiguracoesModule {

  @Retention(RUNTIME)
  @Target({PARAMETER})
  public @interface NomeCampanhaParameter {
  }

  @Retention(RUNTIME)
  @Target({PARAMETER})
  @interface IntervaloAtualizacaoParameter {
  }

  @Retention(RUNTIME)
  @Target({PARAMETER})
  public @interface ConfiguracoesService {
  }

  private final String nomeCampanha;

  public ConfiguracoesModule(String nomeCampanha) {
    this.nomeCampanha = nomeCampanha;
  }

  protected void configure() {
    bindConstant().annotatedWith(NomeCampanhaParameter.class).to(nomeCampanha);
    bind(Period.class).annotatedWith(IntervaloAtualizacaoParameter.class).toInstance(
        Period.minutes(5));

    install(new FactoryModuleBuilder().implement(SistemaAtivo.class, DefaultSistemaAtivo.class)
        .build(SistemaAtivo.Factory.class));

    bind(Configuracoes.class).to(ConfiguracoesImpl.class);
    bind(Service.class).annotatedWith(ConfiguracoesService.class).to(ConfiguracoesImpl.class);
  }

  @Produces
  public Map<String, Definicao> get() {
    return Collections.synchronizedMap(new HashMap<String, Definicao>());
  }
}
