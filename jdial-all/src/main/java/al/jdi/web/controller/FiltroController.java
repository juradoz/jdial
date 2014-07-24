package al.jdi.web.controller;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Filtro;
import al.jdi.dao.model.Mailing;
import al.jdi.web.component.DaoFactoryRequest;
import al.jdi.web.interceptor.DBLogInterceptor.LogAcesso;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class FiltroController {
  private final DaoFactoryRequest daoFactoryRequest;
  private final Result result;

  @Deprecated
  public FiltroController() {
    this(null, null);
  }

  @Inject
  public FiltroController(DaoFactoryRequest daoFactoryRequest, Result result) {
    this.daoFactoryRequest = daoFactoryRequest;
    this.result = result;
  }

  @LogAcesso
  public void add(Filtro filtro) {
    daoFactoryRequest.get().getFiltroDao().adiciona(filtro);
    result.use(Results.logic()).redirectTo(FiltroController.class).list();
  }

  @Post
  @Path("/filtro/{filtro.id}/addMailing")
  public void addMailing(Filtro filtro, Mailing mailing) {
    filtro = daoFactoryRequest.get().getFiltroDao().procura(filtro.getId());
    mailing = daoFactoryRequest.get().getMailingDao().procura(mailing.getId());
    filtro.getMailing().add(mailing);
    daoFactoryRequest.get().getFiltroDao().atualiza(filtro);
    result.use(Results.logic()).redirectTo(FiltroController.class).mailings(filtro);
  }

  @Put
  @Path("/filtro")
  public void adicionar(Filtro filtro) {
    result.use(Results.logic()).redirectTo(FiltroController.class).formularioFiltro("add", filtro);
  }

  @Put
  @Path("/filtro/{filtro.id}/mailing")
  public Collection<Mailing> adicionarMailing(Filtro filtro) {
    filtro = daoFactoryRequest.get().getFiltroDao().procura(filtro.getId());
    result.include("filtro", filtro);
    Collection<Mailing> mailings = daoFactoryRequest.get().getMailingDao().listaAtivos();
    mailings.removeAll(filtro.getMailing());
    return mailings;
  }

  @LogAcesso
  @Delete
  @Path("/filtro/{filtro.id}")
  public void delete(Filtro filtro) {
    filtro = daoFactoryRequest.get().getFiltroDao().procura(filtro.getId());
    daoFactoryRequest.get().getFiltroDao().remove(filtro);
    result.use(Results.logic()).redirectTo(FiltroController.class).list();
  }

  @Delete
  @Path("/filtro/{filtro.id}/mailing/{mailing.id}")
  public void deleteMailing(Filtro filtro, Mailing mailing) {
    filtro = daoFactoryRequest.get().getFiltroDao().procura(filtro.getId());
    filtro.getMailing().remove(mailing);
    daoFactoryRequest.get().getFiltroDao().atualiza(filtro);
    result.use(Results.logic()).redirectTo(FiltroController.class).mailings(filtro);
  }

  @LogAcesso
  public void edit(Filtro filtro) {
    Filtro original = this.daoFactoryRequest.get().getFiltroDao().procura(filtro.getId());
    original.setCampanha(filtro.getCampanha());
    original.setCodigo(filtro.getCodigo());
    original.setDescricao(filtro.getDescricao());
    original.setNome(filtro.getNome());
    daoFactoryRequest.get().getFiltroDao().atualiza(original);
    result.use(Results.logic()).redirectTo(FiltroController.class).list();
  }

  @Get
  @Path("/filtro/{filtro.id}")
  public void editar(Filtro filtro) {
    filtro = daoFactoryRequest.get().getFiltroDao().procura(filtro.getId());
    result.use(Results.logic()).forwardTo(FiltroController.class).formularioFiltro("edit", filtro);
  }

  public void formularioFiltro(String action, Filtro filtro) {
    result.include("filtro", filtro);
    result.include("campanhas", listCampanhas());
    result.include("formAction", action);
  }

  @Get
  @Path("/filtros")
  public Collection<Filtro> list() {
    Set<Filtro> filtrosDistinct = new HashSet<Filtro>();
    filtrosDistinct.addAll(daoFactoryRequest.get().getFiltroDao().listaTudo());
    return sort(filtrosDistinct, on(Filtro.class).getNome());
  }

  private Collection<Campanha> listCampanhas() {
    return daoFactoryRequest.get().getCampanhaDao().listaTudo();
  }

  @Get
  @Path("/filtro/{filtro.id}/mailings")
  public Collection<Mailing> mailings(Filtro filtro) {
    filtro = daoFactoryRequest.get().getFiltroDao().procura(filtro.getId());
    result.include("filtro", filtro);
    return filtro.getMailing();
  }

}
