package al.jdi.core.tenant;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

import al.jdi.dao.model.Campanha;

public class TenantModule {

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface TenantService {
  }

  @Produces
  public Map<Campanha, Tenant> getTenantCollection() {
    return Collections.synchronizedMap(new HashMap<Campanha, Tenant>());
  }
}
