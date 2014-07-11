package net.danieljurado.dialer.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class FilterModule {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER})
  public @interface ClienteSemTelefoneFilter {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER})
  public @interface SomenteCelularFilter {
  }

  protected void configure() {
    install(new PrivateModule() {
      @Override
      protected void configure() {
        bind(TelefoneFilter.class).to(DefaultTelefoneFilter.class);
        expose(TelefoneFilter.class);
        Multibinder<TelefoneUtil> multibinder =
            Multibinder.newSetBinder(binder(), TelefoneUtil.class);
        multibinder.addBinding().to(CampoUtil.class);
        multibinder.addBinding().to(BloqueioCelularUtil.class);
        multibinder.addBinding().to(RestricaoHorarioUtil.class);
      }
    });

    install(new PrivateModule() {
      @Override
      protected void configure() {
        bind(TelefoneFilter.class).annotatedWith(ClienteSemTelefoneFilter.class).to(
            DefaultTelefoneFilter.class);
        expose(TelefoneFilter.class).annotatedWith(ClienteSemTelefoneFilter.class);
        Multibinder<TelefoneUtil> multibinder =
            Multibinder.newSetBinder(binder(), TelefoneUtil.class);
        multibinder.addBinding().to(CampoUtil.class);
        multibinder.addBinding().to(RestricaoHorarioUtil.class);
      }
    });

    install(new PrivateModule() {
      @Override
      protected void configure() {
        bind(TelefoneFilter.class).annotatedWith(SomenteCelularFilter.class).to(
            DefaultTelefoneFilter.class);
        expose(TelefoneFilter.class).annotatedWith(SomenteCelularFilter.class);
        Multibinder<TelefoneUtil> multibinder =
            Multibinder.newSetBinder(binder(), TelefoneUtil.class);
        multibinder.addBinding().to(BloqueioCelularUtil.class);
      }
    });

    bind(CelularChecker.class);
  }

}
