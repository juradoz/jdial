package al.jdi.web.controller;

import java.util.Collection;

import javax.inject.Inject;

import al.jdi.dao.model.Servico;
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
public class ServicoController {
  private final DaoFactoryRequest daoFactoryRequest;
  private final Result result;

  @Deprecated
  public ServicoController() {
    this(null, null);
  }

  @Inject
  public ServicoController(DaoFactoryRequest daoFactoryRequest, Result result) {
    this.daoFactoryRequest = daoFactoryRequest;
    this.result = result;
  }

  @LogAcesso
  public void add(Servico servico) {
    daoFactoryRequest.get().getServicoDao().adiciona(servico);
    result.use(Results.logic()).redirectTo(ServicoController.class).list();
  }

  @Put
  @Path("/servico")
  public void adicionar(Servico servico) {
    result.use(Results.logic()).redirectTo(ServicoController.class)
        .formularioServico("add", servico);
  }

  @LogAcesso
  @Delete
  @Path("/servico/{servico.id}")
  public void delete(Servico servico) {
    servico = daoFactoryRequest.get().getServicoDao().procura(servico.getId());
    daoFactoryRequest.get().getServicoDao().remove(servico);
    result.use(Results.logic()).redirectTo(ServicoController.class).list();
  }

  @LogAcesso
  public void edit(Servico servico) {
    daoFactoryRequest.get().getServicoDao().atualiza(servico);
    result.use(Results.logic()).redirectTo(ServicoController.class).list();
  }

  @Get
  @Path("/servico/{servico.id}")
  public void editar(Servico servico) {
    servico = daoFactoryRequest.get().getServicoDao().procura(servico.getId());
    result.use(Results.logic()).forwardTo(ServicoController.class)
        .formularioServico("edit", servico);
  }

  public void formularioServico(String action, Servico servico) {
    result.include("formAction", action);
    result.include("servico", servico);
  }

  @Get
  @Path("/servicos")
  public Collection<Servico> list() {
    return daoFactoryRequest.get().getServicoDao().listaTudo();
  }
}
