package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.modelo.Providencia;
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.ResultadoLigacaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.InformacaoCliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaRetornaProvidenciaTest {

  private static final Integer PROVIDENCIA = 0;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private ResultadoLigacaoDao resultadoLigacaoDao;
  @Mock
  private Dao<InformacaoCliente> informacaoClienteDao;
  @Mock
  private Mailing mailing;
  @Mock
  private Campanha campanha;
  @Mock
  private ResultadoLigacao resultadoLigacaoSemProximoTelefone;
  @Mock
  private InformacaoCliente informacaoCliente;
  @Mock
  private Logger logger;

  private ProcessaRetornaProvidencia processaRetornaProvidencia;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getResultadoLigacaoDao()).thenReturn(resultadoLigacaoDao);
    when(daoFactory.getInformacaoClienteDao()).thenReturn(informacaoClienteDao);
    when(cliente.getMailing()).thenReturn(mailing);
    when(mailing.getCampanha()).thenReturn(campanha);
    when(resultadoLigacaoDao.procura(MotivoSistema.SEM_PROXIMO_TELEFONE.getCodigo(), campanha))
        .thenReturn(resultadoLigacaoSemProximoTelefone);
    when(cliente.getInformacaoCliente()).thenReturn(informacaoCliente);
    when(informacaoCliente.getProvidenciaTelefone()).thenReturn(PROVIDENCIA);
    processaRetornaProvidencia = new ProcessaRetornaProvidencia(logger);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaRetornaProvidencia.getOrdem(), is(13));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    assertThat(processaRetornaProvidencia.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    assertThat(processaRetornaProvidencia.accept(ligacao, cliente,
        resultadoLigacaoSemProximoTelefone, daoFactory), is(true));
  }

  @Test
  public void processaDeveriaRetornarProvidencia() throws Exception {
    processaRetornaProvidencia.executa(ligacao, cliente, resultadoLigacao, daoFactory);
    verify(informacaoCliente).setProvidenciaTelefone(Providencia.Codigo.MANTEM_ATUAL.getCodigo());
  }

  @Test
  public void processaDeveriaAtualizarInformacaoCliente() throws Exception {
    processaRetornaProvidencia.executa(ligacao, cliente, resultadoLigacao, daoFactory);
    verify(informacaoClienteDao).atualiza(informacaoCliente);
  }

  @Test
  public void processaDeveriaRetornarTrue() throws Exception {
    assertThat(processaRetornaProvidencia.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }
}
