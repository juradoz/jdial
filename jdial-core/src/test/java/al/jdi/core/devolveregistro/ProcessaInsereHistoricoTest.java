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
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
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
  private Logger logger;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getHistoricoLigacaoDao()).thenReturn(historicoLigacaoDao);
    processaInsereHistorico = new ProcessaInsereHistorico(logger);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaInsereHistorico.getOrdem(), is(0));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isInsereHistorico()).thenReturn(true);
    assertThat(processaInsereHistorico.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isInsereHistorico()).thenReturn(false);
    assertThat(processaInsereHistorico.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void executaDeveriaInserir() throws Exception {
    assertThat(processaInsereHistorico.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(historicoLigacaoDao).adiciona(any(HistoricoLigacao.class));
  }

  @Test
  public void executaDeveriaLancarException() throws Exception {
    doThrow(new RuntimeException("Teste")).when(historicoLigacaoDao).adiciona(
        any(HistoricoLigacao.class));
    assertThat(processaInsereHistorico.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

}
