package al.jdi.web.controller;

import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Usuario;
import al.jdi.web.interceptor.LogInterceptor.LogAcesso;
import al.jdi.web.session.UsuarioAutenticadoSession;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class AdminController {

  private final DaoFactory daoFactory;
  private final Result result;
  private final UsuarioAutenticadoSession usuarioAutenticado;

  @Deprecated
  public AdminController() {
    this(null, null, null);
  }

  @Inject
  public AdminController(Result result, DaoFactory daoFactory,
      UsuarioAutenticadoSession usuarioAutenticado) {
    this.result = result;
    this.daoFactory = daoFactory;
    this.usuarioAutenticado = usuarioAutenticado;
  }

  @LogAcesso
  @Post
  public void logar(Usuario usuario) {
    usuario = daoFactory.getUsuarioDao().obtemAutenticado(usuario);
    if (usuario == null) {
      usuarioAutenticado.setUsuario(null);
      result.include("errors", "Usuario ou senha invalidos!");
      result.use(Results.logic()).redirectTo(AdminController.class).login();
    } else {
      usuario.getTipoPerfil();
      usuarioAutenticado.setUsuario(usuario);
      result.use(Results.logic()).redirectTo(MenuController.class).menu();
    }
  }

  @Path("/")
  public void login() {}

  @LogAcesso
  public void logout() {
    usuarioAutenticado.setUsuario(null);
    result.use(Results.logic()).redirectTo(AdminController.class).login();
  }
}
