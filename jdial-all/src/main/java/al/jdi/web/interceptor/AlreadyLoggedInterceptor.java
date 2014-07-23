package al.jdi.web.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import al.jdi.web.controller.AdminController;
import al.jdi.web.controller.MenuController;
import al.jdi.web.interceptor.AuthInterceptor.Public;
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
public class AlreadyLoggedInterceptor {

  private final UsuarioAutenticadoSession usuarioAutenticadoSession;
  private final Result result;

  @Deprecated
  public AlreadyLoggedInterceptor() {
    this(null, null);
  }

  @Inject
  public AlreadyLoggedInterceptor(UsuarioAutenticadoSession usuarioAutenticadoSession, Result result) {
    this.usuarioAutenticadoSession = usuarioAutenticadoSession;
    this.result = result;
  }

  @Accepts
  public boolean accepts(ControllerMethod method) {
    return AdminController.class.isAssignableFrom(method.getController().getType())
        && method.containsAnnotation(Public.class)
        && usuarioAutenticadoSession.getUsuario() != null;
  }

  @AroundCall
  public void aroundCall(SimpleInterceptorStack stack) {
    result.use(Results.logic()).redirectTo(MenuController.class).menu();
  }
}
