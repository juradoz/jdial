package al.jdi.web.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.enterprise.util.Nonbinding;

import al.jdi.dao.model.Usuario.TipoPerfil;

@Retention(RetentionPolicy.RUNTIME)
public @interface Permissao {
  @Nonbinding TipoPerfil[] value();
}
