package net.danieljurado.dialer.gerenciadorfatork;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.danieljurado.dialer.Service;

public class GerenciadorFatorKModule {

  @Retention(RUNTIME)
  @Target({PARAMETER})
  public @interface GerenciadorFatorKService {
  }

  protected void configure() {
    bind(GerenciadorFatorK.class).to(GerenciadorFatorKImpl.class);
    bind(Service.class).annotatedWith(GerenciadorFatorKService.class).to(
        GerenciadorFatorKImpl.class);
  }
}
