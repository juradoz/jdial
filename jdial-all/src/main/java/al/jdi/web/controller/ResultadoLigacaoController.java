package al.jdi.web.controller;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Collection;
import java.util.Comparator;

import javax.inject.Inject;

import org.apache.commons.collections.ComparatorUtils;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Usuario.TipoPerfil;
import al.jdi.web.interceptor.LogInterceptor.LogAcesso;
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
  private final DaoFactory daoFactory;
  private final Result result;

  @Deprecated
  public ResultadoLigacaoController() {
    this(null, null);
  }

  @Inject
  public ResultadoLigacaoController(DaoFactory daoFactory, Result result) {
    this.daoFactory = daoFactory;
    this.result = result;
  }

  @LogAcesso
  public void edit(ResultadoLigacao resultadoLigacao) {
    daoFactory.getResultadoLigacaoDao().atualiza(resultadoLigacao);
    result.use(Results.logic()).redirectTo(ResultadoLigacaoController.class)
        .list(resultadoLigacao.getCampanha());
  }

  @Get
  @Path("/resultadoLigacao/{resultadoLigacao.id}")
  public void editar(ResultadoLigacao resultadoLigacao) {
    resultadoLigacao = daoFactory.getResultadoLigacaoDao().procura(resultadoLigacao.getId());
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
    campanha = daoFactory.getCampanhaDao().procura(campanha.getId());
    result.include("campanha", campanha);

    ArgumentComparator byCampanha =
        new ArgumentComparator(Lambda.on(ResultadoLigacao.class).getCampanha().getDescricao());
    ArgumentComparator byCodigo =
        new ArgumentComparator(Lambda.on(ResultadoLigacao.class).getCodigo());

    Comparator comparator = ComparatorUtils.chainedComparator(byCampanha, byCodigo);

    return Lambda.sort(daoFactory.getResultadoLigacaoDao().listaTudo(campanha),
        Lambda.on(ResultadoLigacao.class), comparator);
  }

  public Collection<ResultadoLigacao> listaResultadosLigacao() {
    return daoFactory.getResultadoLigacaoDao().listaTudo();
  }

  public Collection<Campanha> campanhas() {
    return sort(daoFactory.getCampanhaDao().listaTudo(), on(Campanha.class).getNome());
  }
}
