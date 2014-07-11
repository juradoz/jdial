package al.jdi.core.configuracoes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

import org.jboss.weld.environment.se.StartMain;
import org.joda.time.Period;

import al.jdi.dao.model.Definicao;

public class ConfiguracoesModule {

  @Retention(RUNTIME)
  @Target({PARAMETER, METHOD})
  public @interface NomeCampanha {
  }

  @Retention(RUNTIME)
  @Target({PARAMETER, METHOD})
  @interface IntervaloAtualizacao {
  }

  @Retention(RUNTIME)
  @Target({PARAMETER, FIELD, TYPE})
  @Qualifier
  public @interface ConfiguracoesService {
  }

  @Produces
  public Map<String, Definicao> get() {
    return Collections.synchronizedMap(new HashMap<String, Definicao>());
  }

  @NomeCampanha
  @Produces
  public String getCampanha() {
    return StartMain.getParameters()[0];
  }

  @IntervaloAtualizacao
  @Produces
  public Period getIntervaloAtualizacao() {
    return Period.minutes(5);
  }
}
