package al.jdi.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogProducer {

  @Retention(RUNTIME)
  @Target({METHOD, FIELD, PARAMETER, TYPE})
  @Qualifier
  public @interface LogClass {
    @Nonbinding
    Class<?> clazz();
  }

  @Default
  @LogClass(clazz = Object.class)
  @Produces
  public Logger produce(InjectionPoint ip) {
    if (!ip.getAnnotated().isAnnotationPresent(LogClass.class))
      return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());

    return LoggerFactory.getLogger(ip.getAnnotated().getAnnotation(LogClass.class).clazz());
  }
}
