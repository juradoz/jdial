package al.jdi.core.modelo;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

public class ModeloModule {

  @Retention(RUNTIME)
  @Target({PARAMETER})
  public @interface DiscavelTsa {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface ProvidenciaMantemAtual {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface ProvidenciaProximoTelefone {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface ProvidenciaInvalidaAtualEProximoTelefone {
  }

}
