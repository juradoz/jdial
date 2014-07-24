package al.jdi.web.controller;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Collection;

import javax.inject.Inject;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Definicao;
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
public class DefinicaoController {

  private final Result result;
  private final DaoFactoryRequest daoFactoryRequest;

  @Deprecated
  public DefinicaoController() {
    this(null, null);
  }

  @Inject
  public DefinicaoController(Result result, DaoFactoryRequest daoFactoryRequest) {
    this.result = result;
    this.daoFactoryRequest = daoFactoryRequest;
  }

  @LogAcesso
  public void add(Campanha campanha, Definicao definicao) {
    campanha = daoFactoryRequest.get().getCampanhaDao().procura(campanha.getId());
    definicao.setCampanha(campanha);
    daoFactoryRequest.get().getDefinicaoDao().adiciona(definicao);
    result.use(Results.logic()).redirectTo(DefinicaoController.class).listDefinicao(campanha);
  }

  @LogAcesso
  @Put
  @Path("/definicao/{campanha.id}")
  public void adicionar(Campanha campanha, Definicao definicao) {
    campanha = daoFactoryRequest.get().getCampanhaDao().procura(campanha.getId());
    result.include("campanha", campanha);
    result.use(Results.logic()).forwardTo(DefinicaoController.class)
        .formularioDefinicao("add", definicao);
  }

  @LogAcesso
  @Delete
  @Path("/definicao/{definicao.id}")
  public void delete(Definicao definicao) {
    definicao = daoFactoryRequest.get().getDefinicaoDao().procura(definicao.getId());
    Campanha campanha =
        daoFactoryRequest.get().getCampanhaDao().procura(definicao.getCampanha().getId());
    daoFactoryRequest.get().getDefinicaoDao().remove(definicao);
    result.use(Results.logic()).redirectTo(DefinicaoController.class).listDefinicao(campanha);
  }

  @LogAcesso
  public void edit(Campanha campanha, Definicao definicao) {
    campanha = daoFactoryRequest.get().getCampanhaDao().procura(campanha.getId());
    definicao.setCampanha(campanha);
    daoFactoryRequest.get().getDefinicaoDao().atualiza(definicao);
    result.use(Results.logic()).redirectTo(DefinicaoController.class).listDefinicao(campanha);
  }

  @Get
  @Path("/definicao/{definicao.id}")
  public void editar(Definicao definicao) {
    definicao = daoFactoryRequest.get().getDefinicaoDao().procura(definicao.getId());
    Campanha campanha =
        daoFactoryRequest.get().getCampanhaDao().procura(definicao.getCampanha().getId());
    result.include("campanha", campanha);
    result.use(Results.logic()).forwardTo(DefinicaoController.class)
        .formularioDefinicao("edit", definicao);
  }

  public void formularioDefinicao(String action, Definicao definicao) {
    result.include("definicao", definicao);
    result.include("formAction", action);
  }

  @Get
  @Path("/definicao/campanhas")
  public Collection<Campanha> listCampanhas() {
    return sort(daoFactoryRequest.get().getCampanhaDao().listaTudo(), on(Campanha.class).getNome());
  }

  @Get
  @Path("/definicao/campanhas/{campanha.id}")
  public Collection<Definicao> listDefinicao(Campanha campanha) {
    campanha = daoFactoryRequest.get().getCampanhaDao().procura(campanha.getId());
    result.include("campanha", campanha);
    return daoFactoryRequest.get().getDefinicaoDao().listaTudo(campanha);
  }
}
