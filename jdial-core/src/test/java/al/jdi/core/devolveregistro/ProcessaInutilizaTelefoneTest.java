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

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.HistoricoLigacaoDao;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.HistoricoLigacao;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaInutilizaTelefoneTest {

  private ProcessaInutilizaTelefone processaInutilizaTelefone;

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
  private Configuracoes configuracoes;
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getHistoricoLigacaoDao()).thenReturn(historicoLigacaoDao);
    when(historicoLigacaoDao.procura(cliente, resultadoLigacao)).thenReturn(
        Arrays.asList(historicoLigacao));
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    processaInutilizaTelefone = new ProcessaInutilizaTelefone(finalizadorCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaInutilizaTelefone.getOrdem(), is(7));
  }

  @Test
  public void acceptDeveriaRetornarFalsePorResultado() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(false);
    assertThat(processaInutilizaTelefone.accept(tenant, ligacao, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void acceptDeveriaRetornarTruePorResultado() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(true);
    assertThat(processaInutilizaTelefone.accept(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarTruePorQtd() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(false);
    when(resultadoLigacao.getQuantidadeDesteResultadoInutilizaTelefone()).thenReturn(1);
    assertThat(processaInutilizaTelefone.accept(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void executaDeveriaFinalizarPeloResultado() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(true);
    assertThat(processaInutilizaTelefone.executa(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
    verify(finalizadorCliente).finalizaPorInutilizacaoSimples(tenant, daoFactory, cliente);
  }

  @Test
  public void executaDeveriaFinalizarPeloHistorico() throws Exception {
    when(resultadoLigacao.isInutilizaTelefone()).thenReturn(false);
    when(resultadoLigacao.getQuantidadeDesteResultadoInutilizaTelefone()).thenReturn(1);
    assertThat(processaInutilizaTelefone.executa(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
    verify(finalizadorCliente).finalizaPorInutilizacaoSimples(tenant, daoFactory, cliente);
  }

}
