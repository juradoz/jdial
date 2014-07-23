package al.jdi.web.interceptor;

import static java.util.Arrays.asList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import al.jdi.dao.model.Usuario;
import al.jdi.web.controller.ExibidorAcessoNegado;
import al.jdi.web.controller.MenuController;
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
public class PermissaoInterceptor {

  private final UsuarioAutenticadoSession usuarioAutenticado;
  private final Result result;

  @Deprecated
  public PermissaoInterceptor() {
    this(null, null);
  }

  @Inject
  public PermissaoInterceptor(UsuarioAutenticadoSession usuarioAutenticado, Result result) {
    this.usuarioAutenticado = usuarioAutenticado;
    this.result = result;
  }

  @Accepts
  public boolean accepts(ControllerMethod method) {
    return method.getController().getType().isAnnotationPresent(Permissao.class)
        || method.containsAnnotation(Permissao.class);
  }

  @AroundCall
  public void intercept(SimpleInterceptorStack stack, ControllerMethod method) {
    Permissao controllerPermission =
        method.getController().getType().getAnnotation(Permissao.class);
    Permissao methodPermission = method.getMethod().getAnnotation(Permissao.class);

    if (hasPermission(controllerPermission) && hasPermission(methodPermission)) {
      stack.next();
      return;
    }

    try {
      result.use(Results.logic())
          .redirectTo(method.getController().getType().asSubclass(ExibidorAcessoNegado.class))
          .acessoNegado();
    } catch (ClassCastException e) {
      result.use(Results.logic()).redirectTo(MenuController.class).acessoNegado();
    }
  }

  private boolean hasPermission(Permissao permissao) {
    Usuario user = usuarioAutenticado.getUsuario();
    if (user == null)
      return false;

    if (permissao == null)
      return true;

    return asList(permissao.value()).contains(user.getTipoPerfil());
  }

}
