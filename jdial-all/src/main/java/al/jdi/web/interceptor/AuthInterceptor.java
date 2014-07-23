package al.jdi.web.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import al.jdi.web.controller.AdminController;
import al.jdi.web.session.UsuarioAutenticadoSession;
import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;

@Intercepts
@RequestScoped
public class AuthInterceptor {

  @Retention(RetentionPolicy.RUNTIME)
  public @interface Public {
  }

  private final UsuarioAutenticadoSession usuarioAutenticado;
  private final Result result;

  @Deprecated
  public AuthInterceptor() {
    this.usuarioAutenticado = null;
    this.result = null;
  }

  @Inject
  public AuthInterceptor(UsuarioAutenticadoSession usuarioAutenticado, Result result) {
    this.usuarioAutenticado = usuarioAutenticado;
    this.result = result;
  }

  @Accepts
  public boolean accepts(ControllerMethod method) {
    return !method.containsAnnotation(Public.class) && usuarioAutenticado.getUsuario() == null;
  }

  @AroundCall
  public void intercept(SimpleInterceptorStack stack) {
    result.use(Results.logic()).redirectTo(AdminController.class).login();
  }
}
