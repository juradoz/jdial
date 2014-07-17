package al.jdi.web.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import al.jdi.dao.model.Usuario.TipoPerfil;

@Retention(RetentionPolicy.RUNTIME)
public @interface Permissao {
  TipoPerfil[] value();
}
