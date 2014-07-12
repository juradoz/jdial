package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.HistoricoLigacaoDao;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.HistoricoLigacao;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaInutilizaTelefoneTest {

  @Mock
  private FinalizadorCliente finalizadorCliente;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private HistoricoLigacaoDao historicoLigacaoDao;
  @Mock
  private HistoricoLigacao historicoLigacao;
  @Mock
  private Logger logger;

  private ProcessaInutilizaTelefone processaInutilizaTelefone;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getHistoricoLigacaoDao()).thenReturn(historicoLigacaoDao);
    when(historicoLigacaoDao.procura(cliente, resultadoLigacao)).thenReturn(
        Arrays.asList(historicoLigacao));
    processaInutilizaTelefone = new ProcessaInutilizaTelefone(logger, finalizadorCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaInutilizaTelefone.getOrdem(), is(7));
  }

  @Test
  public void acceptDeveriaRetornarFalsePorResultado() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(false);
    assertThat(processaInutilizaTelefone.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void acceptDeveriaRetornarTruePorResultado() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(true);
    assertThat(processaInutilizaTelefone.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarTruePorQtd() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(false);
    when(resultadoLigacao.getQuantidadeDesteResultadoInutilizaTelefone()).thenReturn(1);
    assertThat(processaInutilizaTelefone.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void executaDeveriaFinalizarPeloResultado() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(true);
    assertThat(processaInutilizaTelefone.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(finalizadorCliente).finalizaPorInutilizacaoSimples(daoFactory, cliente);
  }

  @Test
  public void executaDeveriaFinalizarPeloHistorico() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(false);
    when(resultadoLigacao.getQuantidadeDesteResultadoInutilizaTelefone()).thenReturn(1);
    assertThat(processaInutilizaTelefone.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(finalizadorCliente).finalizaPorInutilizacaoSimples(daoFactory, cliente);
  }

}
