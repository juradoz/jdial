package al.jdi.core.gerenciadorfatork;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

public class GerenciadorFatorKModule {

  @Retention(RUNTIME)
  @Target({PARAMETER, FIELD, TYPE})
  @Qualifier
  public @interface GerenciadorFatorKService {
  }

}
