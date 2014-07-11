package net.danieljurado.dialer.estoque;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

import org.joda.time.Period;

public class EstoqueModule {

  @Retention(RUNTIME)
  @Target({PARAMETER, FIELD, METHOD, TYPE})
  @Qualifier
  public @interface Agendados {
  }

  @Retention(RUNTIME)
  @Target({PARAMETER, FIELD, METHOD, TYPE})
  @Qualifier
  public @interface Livres {
  }

  @Produces
  public Collection<Registro> getEstoqueCollection() {
    return Collections.synchronizedCollection(new HashSet<Registro>());
  }

  @Livres
  @Produces
  public Period getIntervaloMonitoracaoLivres() {
    return Period.seconds(5);
  }

  @Agendados
  @Produces
  public Period getIntervaloMonitoracaoAgendados() {
    return Period.minutes(3);
  }

}
