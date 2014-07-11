package net.danieljurado.dialer.gerenciadoragentes;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.danieljurado.dialer.Service;

public class GerenciadorAgentesModule {

  @Retention(RUNTIME)
  @Target({PARAMETER})
  public @interface GerenciadorAgentesService {
  }

  protected void configure() {
    bind(GerenciadorAgentes.class).to(GerenciadorAgentesImpl.class);
    bind(Service.class).annotatedWith(GerenciadorAgentesService.class).to(
        GerenciadorAgentesImpl.class);
  }

}
