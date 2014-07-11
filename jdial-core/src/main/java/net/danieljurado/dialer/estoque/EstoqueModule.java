package net.danieljurado.dialer.estoque;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.enterprise.inject.Produces;

import net.danieljurado.dialer.Service;

import org.joda.time.Period;

public class EstoqueModule {

  @Retention(RUNTIME)
  @Target({PARAMETER})
  public @interface Agendados {
  }

  @Retention(RUNTIME)
  @Target({PARAMETER})
  public @interface Livres {
  }

  protected void configure() {
    install(new PrivateModule() {
      @Override
      protected void configure() {
        bind(Estoque.class).annotatedWith(Livres.class).to(EstoqueImpl.class);
        bind(Service.class).annotatedWith(Livres.class).to(EstoqueImpl.class);
        expose(Estoque.class).annotatedWith(Livres.class);
        expose(Service.class).annotatedWith(Livres.class);

        bind(ExtraidorClientes.class).to(ClientesLivres.class);
        bind(Period.class).toInstance(Period.seconds(5));
      }
    });

    install(new PrivateModule() {
      @Override
      protected void configure() {
        bind(Estoque.class).annotatedWith(Agendados.class).to(EstoqueImpl.class);
        bind(Service.class).annotatedWith(Agendados.class).to(EstoqueImpl.class);
        expose(Estoque.class).annotatedWith(Agendados.class);
        expose(Service.class).annotatedWith(Agendados.class);

        bind(ExtraidorClientes.class).to(ClientesAgendados.class);
        bind(Period.class).toInstance(Period.minutes(3));
      }
    });
  }

  @Produces
  public Collection<Registro> getEstoqueCollection() {
    return Collections.synchronizedCollection(new HashSet<Registro>());
  }

}
