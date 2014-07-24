package al.jdi.web.controller;

import java.util.Collection;

import javax.inject.Inject;

import al.jdi.dao.model.Rota;
import al.jdi.dao.model.Usuario.TipoPerfil;
import al.jdi.web.component.DaoFactoryRequest;
import al.jdi.web.interceptor.DBLogInterceptor.LogAcesso;
import al.jdi.web.interceptor.Permissao;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Permissao(TipoPerfil.ADMINISTRADOR)
@Controller
public class RotaController {
  private final DaoFactoryRequest daoFactoryRequest;
  private final Result result;

  @Deprecated
  public RotaController() {
    this(null, null);
  }

  @Inject
  public RotaController(DaoFactoryRequest daoFactoryRequest, Result result) {
    this.daoFactoryRequest = daoFactoryRequest;
    this.result = result;
  }

  @LogAcesso
  public void add(Rota rota) {
    daoFactoryRequest.get().getRotaDao().adiciona(rota);
    result.use(Results.logic()).redirectTo(RotaController.class).list();
  }

  @Put
  @Path("/rota")
  public void adicionar(Rota rota) {
    result.use(Results.logic()).redirectTo(RotaController.class).formularioRota("add", rota);
  }

  @LogAcesso
  @Delete
  @Path("/rota/{rota.id}")
  public void delete(Rota rota) {
    rota = daoFactoryRequest.get().getRotaDao().procura(rota.getId());
    daoFactoryRequest.get().getRotaDao().remove(rota);
    result.use(Results.logic()).redirectTo(RotaController.class).list();
  }

  @LogAcesso
  public void edit(Rota rota) {
    daoFactoryRequest.get().getRotaDao().atualiza(rota);
    result.use(Results.logic()).redirectTo(RotaController.class).list();
  }

  @Get
  @Path("/rota/{rota.id}")
  public void editar(Rota rota) {
    rota = daoFactoryRequest.get().getRotaDao().procura(rota.getId());
    result.use(Results.logic()).forwardTo(RotaController.class).formularioRota("edit", rota);
  }

  public void formularioRota(String action, Rota rota) {
    result.include("formAction", action);
    result.include("rota", rota);
  }

  @Get
  @Path("/rotas")
  public Collection<Rota> list() {
    return daoFactoryRequest.get().getRotaDao().listaTudo();
  }
}
