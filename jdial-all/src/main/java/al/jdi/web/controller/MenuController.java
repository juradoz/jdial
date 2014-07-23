package al.jdi.web.controller;

import javax.inject.Inject;

import al.jdi.web.interceptor.DBLogInterceptor.LogAcesso;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class MenuController implements ExibidorAcessoNegado {

  private final Result result;

  @Deprecated
  public MenuController() {
    this(null);
  }

  @Inject
  public MenuController(Result result) {
    this.result = result;
  }

  @LogAcesso
  @Override
  public void acessoNegado() {
    result.include("errors", "Acesso negado!");
    result.use(Results.logic()).redirectTo(MenuController.class).menu();
  }

  @Path("/menu")
  public void menu() {}
}
