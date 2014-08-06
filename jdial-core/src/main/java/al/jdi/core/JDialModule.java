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

import org.jboss.weld.environment.se.StartMain;

public class JDialModule {

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface Versao {
  }

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface NomeCampanha {
  }

  @Versao
  @Produces
  public String getVersao() {
    return getClass().getPackage().getImplementationVersion();
  }

  @NomeCampanha
  @Produces
  public String getNomeCampanha() {
    return StartMain.getParameters()[0];
  }
}
