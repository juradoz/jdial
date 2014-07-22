package al.jdi.web.interceptor;

import javax.enterprise.context.SessionScoped;

import al.jdi.dao.model.Usuario;
import al.jdi.dao.model.Usuario.TipoPerfil;
import al.jdi.web.controller.ExibidorAcessoNegado;
import al.jdi.web.controller.MenuController;
import al.jdi.web.session.UsuarioAutenticadoSession;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerInstance;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;

@Intercepts
@AcceptsWithAnnotations(Permissao.class)
@SessionScoped
public class PermissaoInterceptor {

  private final UsuarioAutenticadoSession usuarioAutenticado;
  private final Result result;

  public PermissaoInterceptor(UsuarioAutenticadoSession usuarioAutenticado, Result result) {
    this.usuarioAutenticado = usuarioAutenticado;
    this.result = result;
  }

  @AroundCall
  public void intercept(SimpleInterceptorStack stack, ControllerMethod method,
      ControllerInstance instance) {

    if (isExistePermissao(instance.getClass().getAnnotation(Permissao.class))) {
      stack.next();
      return;
    }


    if (isExistePermissao(method.getMethod().getAnnotation(Permissao.class))) {
      stack.next();
      return;
    }

    try {
      result.use(Results.logic())
          .forwardTo(instance.getController().getClass().asSubclass(ExibidorAcessoNegado.class))
          .acessoNegado();
    } catch (ClassCastException e) {
      result.use(Results.logic()).forwardTo(MenuController.class).acessoNegado();
    }
  }

  private boolean isExistePermissao(Permissao permissaoList) {
    Usuario user = usuarioAutenticado.getUsuario();

    if (user == null)
      return true;

    if (permissaoList == null)
      return true;

    for (TipoPerfil perfil : permissaoList.value())
      if (perfil.equals(user.getTipoPerfil()))
        return true;

    return false;
  }

}
