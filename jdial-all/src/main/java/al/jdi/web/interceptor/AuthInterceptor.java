package al.jdi.web.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import al.jdi.web.controller.AdminController;
import al.jdi.web.session.UsuarioAutenticadoSession;
import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerInstance;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;

@Intercepts
@RequestScoped
public class AuthInterceptor {
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
  public boolean accepts(ControllerInstance controller) {
    return usuarioAutenticado.getUsuario() == null
        && !(AdminController.class.isAssignableFrom(controller.getController().getClass()));
  }

  @AroundCall
  public void intercept(SimpleInterceptorStack stack, ControllerInstance controller) {
    result.use(Results.logic()).redirectTo(AdminController.class).login();
  }
}
