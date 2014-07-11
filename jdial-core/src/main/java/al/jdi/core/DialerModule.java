package al.jdi.core;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

public class DialerModule {

  @Retention(RUNTIME)
  @Target({PARAMETER, FIELD, TYPE})
  @Qualifier
  public @interface DialerService {
  }

  @Retention(RUNTIME)
  @Target({PARAMETER, METHOD})
  public @interface Versao {
  }

  private static final String VERSAO = "4.2.2";

  @Versao
  @Produces
  public String getVersao() {
    return VERSAO;
  }
}
