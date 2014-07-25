package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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

public class ProcessaInsereHistoricoTest {

  private ProcessaInsereHistorico processaInsereHistorico;

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
  private Configuracoes configuracoes;
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getHistoricoLigacaoDao()).thenReturn(historicoLigacaoDao);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    processaInsereHistorico = new ProcessaInsereHistorico();
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaInsereHistorico.getOrdem(), is(0));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isInsereHistorico()).thenReturn(true);
    assertThat(processaInsereHistorico.accept(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isInsereHistorico()).thenReturn(false);
    assertThat(processaInsereHistorico.accept(tenant, ligacao, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void executaDeveriaInserir() throws Exception {
    assertThat(processaInsereHistorico.executa(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
    verify(historicoLigacaoDao).adiciona(any(HistoricoLigacao.class));
  }

  @Test
  public void executaDeveriaLancarException() throws Exception {
    doThrow(new RuntimeException("Teste")).when(historicoLigacaoDao).adiciona(
        any(HistoricoLigacao.class));
    assertThat(processaInsereHistorico.executa(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
  }

}
