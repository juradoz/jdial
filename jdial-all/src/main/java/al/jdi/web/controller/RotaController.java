package al.jdi.web.controller;

import java.util.Collection;

import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Rota;
import al.jdi.dao.model.Usuario.TipoPerfil;
import al.jdi.web.interceptor.LogInterceptor.LogAcesso;
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
  private final DaoFactory daoFactory;
  private final Result result;

  @Deprecated
  public RotaController() {
    this(null, null);
  }

  @Inject
  public RotaController(DaoFactory daoFactory, Result result) {
    this.daoFactory = daoFactory;
    this.result = result;
  }

  @LogAcesso
  public void add(Rota rota) {
    daoFactory.getRotaDao().adiciona(rota);
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
    rota = daoFactory.getRotaDao().procura(rota.getId());
    daoFactory.getRotaDao().remove(rota);
    result.use(Results.logic()).redirectTo(RotaController.class).list();
  }

  @LogAcesso
  public void edit(Rota rota) {
    daoFactory.getRotaDao().atualiza(rota);
    result.use(Results.logic()).redirectTo(RotaController.class).list();
  }

  @Get
  @Path("/rota/{rota.id}")
  public void editar(Rota rota) {
    rota = daoFactory.getRotaDao().procura(rota.getId());
    result.use(Results.logic()).forwardTo(RotaController.class).formularioRota("edit", rota);
  }

  public void formularioRota(String action, Rota rota) {
    result.include("formAction", action);
    result.include("rota", rota);
  }

  @Get
  @Path("/rotas")
  public Collection<Rota> list() {
    return daoFactory.getRotaDao().listaTudo();
  }
}
