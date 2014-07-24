package al.jdi.web.controller;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Collection;

import javax.inject.Inject;

import al.jdi.dao.model.Grupo;
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
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

@Permissao(TipoPerfil.ADMINISTRADOR)
@Controller
public class GrupoController {
  private final DaoFactoryRequest daoFactoryRequest;
  private final Result result;
  private final Validator validator;

  @Deprecated
  public GrupoController() {
    this(null, null, null);
  }

  @Inject
  public GrupoController(DaoFactoryRequest daoFactoryRequest, Result result, Validator validator) {
    this.daoFactoryRequest = daoFactoryRequest;
    this.result = result;
    this.validator = validator;
  }

  @LogAcesso
  public void add(Grupo grupo) {
    if (grupo.getCodigo() == null || grupo.getCodigo().length() == 0)
      validator.add(new SimpleMessage("Código não pode ser vazio!", "codigoInvalido"));
    validator.onErrorUse(Results.page()).of(GrupoController.class).adicionar(grupo);

    daoFactoryRequest.get().getGrupoDao().adiciona(grupo);
    result.use(Results.logic()).redirectTo(GrupoController.class).list();
  }

  @Put
  @Path("/grupo")
  public void adicionar(Grupo grupo) {
    result.use(Results.logic()).redirectTo(GrupoController.class).formularioGrupo("add", grupo);
  }

  @LogAcesso
  @Delete
  @Path("/grupo/{grupo.id}")
  public void delete(Grupo grupo) {
    grupo = daoFactoryRequest.get().getGrupoDao().procura(grupo.getId());
    daoFactoryRequest.get().getGrupoDao().remove(grupo);
    result.use(Results.logic()).redirectTo(GrupoController.class).list();
  }

  @LogAcesso
  public void edit(Grupo grupo) {
    if (grupo.getCodigo() == null || grupo.getCodigo().length() == 0)
      validator.add(new SimpleMessage("Código não pode ser vazio!", "codigoInvalido"));
    validator.onErrorUse(Results.page()).of(GrupoController.class).editar(grupo);
    daoFactoryRequest.get().getGrupoDao().atualiza(grupo);
    result.use(Results.logic()).redirectTo(GrupoController.class).list();
  }

  @Get
  @Path("/grupo/{grupo.id}")
  public void editar(Grupo grupo) {
    grupo = daoFactoryRequest.get().getGrupoDao().procura(grupo.getId());
    result.use(Results.logic()).forwardTo(GrupoController.class).formularioGrupo("edit", grupo);
  }

  public void formularioGrupo(String action, Grupo grupo) {
    result.include("formAction", action);
    result.include("grupo", grupo);
  }

  @Get
  @Path("/grupos")
  public Collection<Grupo> list() {
    return sort(daoFactoryRequest.get().getGrupoDao().listaTudo(), on(Grupo.class).getDescricao());
  }
}
