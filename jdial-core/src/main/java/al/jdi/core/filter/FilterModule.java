package al.jdi.core.filter;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.asList;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashSet;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

public class FilterModule {

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface ClienteSemTelefoneFilter {
  }

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface SomenteCelularFilter {
  }

  @Produces
  public TelefoneFilter getTelefoneFilter(CampoUtil campoUtil,
      BloqueioCelularUtil bloqueioCelularUtil, RestricaoHorarioUtil restricaoHorarioUtil) {
    return new DefaultTelefoneFilter(new HashSet<>(asList((TelefoneUtil) campoUtil,
        (TelefoneUtil) bloqueioCelularUtil, (TelefoneUtil) restricaoHorarioUtil)));
  }

  @Produces
  @ClienteSemTelefoneFilter
  public TelefoneFilter getTelefoneFilterClienteSemTelefoneFilter(CampoUtil campoUtil,
      RestricaoHorarioUtil restricaoHorarioUtil) {
    return new DefaultTelefoneFilter(new HashSet<>(asList((TelefoneUtil) campoUtil,
        (TelefoneUtil) restricaoHorarioUtil)));
  }

  @Produces
  @SomenteCelularFilter
  public TelefoneFilter getTelefoneFilterSomenteCelular(BloqueioCelularUtil bloqueiaCelular,
      RestricaoHorarioUtil restricaoHorarioUtil) {
    return new DefaultTelefoneFilter(new HashSet<>(asList((TelefoneUtil) bloqueiaCelular,
        (TelefoneUtil) restricaoHorarioUtil)));
  }

}
