package al.jdi.web.controller;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import al.jdi.web.interceptor.LogInterceptor.LogAcesso;
import al.jdi.web.session.UsuarioAutenticadoSession;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class MenuController implements ExibidorAcessoNegado {

  private final Logger logger;
  private final HttpServletRequest request;
  private final UsuarioAutenticadoSession usuarioAutenticadoSession;
  private final Result result;

  @Deprecated
  public MenuController() {
    this(null, null, null, null);
  }

  @Inject
  public MenuController(Logger logger, HttpServletRequest request,
      UsuarioAutenticadoSession usuarioAutenticadoSession, Result result) {
    this.logger = logger;
    this.request = request;
    this.usuarioAutenticadoSession = usuarioAutenticadoSession;
    this.result = result;
  }

  @LogAcesso
  @Override
  public void acessoNegado() {
    result.include("errors", "Acesso negado!");
    result.use(Results.logic()).forwardTo(MenuController.class).menu();
  }

  @Path("/menu")
  public void menu() {
    Enumeration<?> attributeNames = request.getSession().getAttributeNames();
    while (attributeNames.hasMoreElements()) {
      logger.info("name: {}", attributeNames.nextElement());
    }
    result.include("usuarioAutenticadoSession", usuarioAutenticadoSession);
  }
}
