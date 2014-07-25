package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.MotivoFinalizacao;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public class ProcessaFinalizaClienteTest {

  private ProcessaFinalizaCliente processaFinalizaCliente;

  @Mock
  private FinalizadorCliente finalizadorCliente;
  @Mock
  private NotificadorCliente notificadorCliente;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Dao<MotivoFinalizacao> motivoFinalizacaoDao;
  @Mock
  private MotivoFinalizacao motivoFinalizacao;
  @Mock
  private Telefone telefone;
  @Mock
  private Mailing mailing;
  @Mock
  private Campanha campanha;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getMotivoFinalizacaoDao()).thenReturn(motivoFinalizacaoDao);
    when(motivoFinalizacaoDao.procura("Atendimento")).thenReturn(motivoFinalizacao);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(cliente.getMailing()).thenReturn(mailing);
    when(mailing.getCampanha()).thenReturn(campanha);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    processaFinalizaCliente = new ProcessaFinalizaCliente(finalizadorCliente, notificadorCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaFinalizaCliente.getOrdem(), is(2));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isFinalizaCliente()).thenReturn(true);
    assertThat(processaFinalizaCliente.accept(tenant, daoFactory, ligacao, resultadoLigacao),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isFinalizaCliente()).thenReturn(false);
    assertThat(processaFinalizaCliente.accept(tenant, daoFactory, ligacao, resultadoLigacao),
        is(false));
  }

  @Test
  public void executaDeveriaFinalizar() throws Exception {
    assertThat(processaFinalizaCliente.executa(tenant, daoFactory, ligacao, resultadoLigacao),
        is(false));
    verify(finalizadorCliente).finaliza(tenant, daoFactory, cliente, motivoFinalizacao);
  }

  @Test
  public void executaDeveriaNotificar() throws Exception {
    assertThat(processaFinalizaCliente.executa(tenant, daoFactory, ligacao, resultadoLigacao),
        is(false));
    verify(notificadorCliente).notificaFinalizacao(tenant, daoFactory, ligacao, cliente,
        resultadoLigacao, telefone, false, campanha);
  }
}
