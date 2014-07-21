package al.jdi.web.controller;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Collection;

import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Grupo;
import al.jdi.dao.model.Rota;
import al.jdi.dao.model.Servico;
import al.jdi.dao.model.Usuario.TipoPerfil;
import al.jdi.web.component.DaoFactoryRequest;
import al.jdi.web.interceptor.LogInterceptor.LogAcesso;
import al.jdi.web.interceptor.Permissao;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class CampanhaController {

  private final Result result;
  private final DaoFactory daoFactory;

  @Deprecated
  public CampanhaController() {
    this.result = null;
    this.daoFactory = null;
  }

  @Inject
  public CampanhaController(Result result, DaoFactoryRequest daoFactoryRequest) {
    this.result = result;
    this.daoFactory = daoFactoryRequest.get();
  }

  @Permissao(TipoPerfil.ADMINISTRADOR)
  @LogAcesso
  public void add(Campanha campanha) {
    daoFactory.getCampanhaDao().adiciona(campanha);
    result.use(Results.logic()).redirectTo(CampanhaController.class).list();
  }

  @Permissao(TipoPerfil.ADMINISTRADOR)
  @Put
  @Path("/campanha")
  public void adicionar(Campanha campanha) {
    result.use(Results.logic()).redirectTo(CampanhaController.class)
        .formularioCampanha("add", campanha);
  }

  @Permissao(TipoPerfil.ADMINISTRADOR)
  @Delete
  @Path("/campanha/{campanha.id}")
  @LogAcesso
  public void delete(Campanha campanha) {
    campanha = daoFactory.getCampanhaDao().procura(campanha.getId());
    daoFactory.getCampanhaDao().remove(campanha);
    result.use(Results.logic()).redirectTo(CampanhaController.class).list();
  }

  @LogAcesso
  public void edit(Campanha campanha) {
    daoFactory.getCampanhaDao().atualiza(campanha);
    result.use(Results.logic()).redirectTo(CampanhaController.class).list();
  }

  @Get
  @Path("/campanha/{campanha.id}")
  public void editar(Campanha campanha) {
    campanha = daoFactory.getCampanhaDao().procura(campanha.getId());
    result.use(Results.logic()).forwardTo(CampanhaController.class)
        .formularioCampanha("edit", campanha);
  }

  public void formularioCampanha(String action, Campanha campanha) {
    result.include("campanha", campanha);
    result.include("grupos", listGrupos());
    result.include("rotas", listRotas());
    result.include("servicos", listServicos());
    result.include("formAction", action);
  }

  @Get
  @Path("/campanhas")
  public Collection<Campanha> list() {
    return sort(daoFactory.getCampanhaDao().listaTudo(), on(Campanha.class).getNome());
  }

  private Collection<Grupo> listGrupos() {
    return daoFactory.getGrupoDao().listaTudo();
  }

  private Collection<Rota> listRotas() {
    return daoFactory.getRotaDao().listaTudo();
  }

  private Collection<Servico> listServicos() {
    return daoFactory.getServicoDao().listaTudo();
  }
}
