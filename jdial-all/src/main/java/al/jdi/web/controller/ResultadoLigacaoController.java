package al.jdi.web.controller;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Collection;
import java.util.Comparator;

import javax.inject.Inject;

import org.apache.commons.collections.ComparatorUtils;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Usuario.TipoPerfil;
import al.jdi.web.component.DaoFactoryRequest;
import al.jdi.web.interceptor.DBLogInterceptor.LogAcesso;
import al.jdi.web.interceptor.Permissao;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import ch.lambdaj.Lambda;
import ch.lambdaj.function.compare.ArgumentComparator;

@Permissao(TipoPerfil.ADMINISTRADOR)
@Controller
public class ResultadoLigacaoController {
  private final DaoFactoryRequest daoFactoryRequest;
  private final Result result;

  @Deprecated
  public ResultadoLigacaoController() {
    this(null, null);
  }

  @Inject
  public ResultadoLigacaoController(DaoFactoryRequest daoFactoryRequest, Result result) {
    this.daoFactoryRequest = daoFactoryRequest;
    this.result = result;
  }

  @LogAcesso
  public void edit(ResultadoLigacao resultadoLigacao) {
    daoFactoryRequest.get().getResultadoLigacaoDao().atualiza(resultadoLigacao);
    result.use(Results.logic()).redirectTo(ResultadoLigacaoController.class)
        .list(resultadoLigacao.getCampanha());
  }

  @Get
  @Path("/resultadoLigacao/{resultadoLigacao.id}")
  public void editar(ResultadoLigacao resultadoLigacao) {
    resultadoLigacao =
        daoFactoryRequest.get().getResultadoLigacaoDao().procura(resultadoLigacao.getId());
    result.use(Results.logic()).forwardTo(ResultadoLigacaoController.class)
        .formularioResultadoLigacao("edit", resultadoLigacao);
  }

  public void formularioResultadoLigacao(String action, ResultadoLigacao resultadoLigacao) {
    result.include("resultadoLigacaoList", listaResultadosLigacao());
    result.include("formAction", action);
    result.include("resultadoLigacao", resultadoLigacao);
  }

  @Get
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Path("/resultadoLigacao/campanha/{campanha.id}")
  public Collection<ResultadoLigacao> list(Campanha campanha) {
    campanha = daoFactoryRequest.get().getCampanhaDao().procura(campanha.getId());
    result.include("campanha", campanha);

    ArgumentComparator byCampanha =
        new ArgumentComparator(Lambda.on(ResultadoLigacao.class).getCampanha().getDescricao());
    ArgumentComparator byCodigo =
        new ArgumentComparator(Lambda.on(ResultadoLigacao.class).getCodigo());

    Comparator comparator = ComparatorUtils.chainedComparator(byCampanha, byCodigo);

    return Lambda.sort(daoFactoryRequest.get().getResultadoLigacaoDao().listaTudo(campanha),
        Lambda.on(ResultadoLigacao.class), comparator);
  }

  public Collection<ResultadoLigacao> listaResultadosLigacao() {
    return daoFactoryRequest.get().getResultadoLigacaoDao().listaTudo();
  }

  public Collection<Campanha> campanhas() {
    return sort(daoFactoryRequest.get().getCampanhaDao().listaTudo(), on(Campanha.class).getNome());
  }
}
