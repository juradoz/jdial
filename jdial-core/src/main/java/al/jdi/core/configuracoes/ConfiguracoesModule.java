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

import org.joda.time.Period;

import al.jdi.dao.model.Definicao;

public class ConfiguracoesModule {

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  @interface IntervaloAtualizacao {
  }

  @Produces
  public Map<String, Definicao> get() {
    return Collections.synchronizedMap(new HashMap<String, Definicao>());
  }

  @IntervaloAtualizacao
  @Produces
  public Period getIntervaloAtualizacao() {
    return Period.minutes(5);
  }
}
