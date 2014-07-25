package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.equalTo;
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
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public class ProcessaSemTelefonesTest {

  private ProcessaSemTelefones processaSemTelefones;

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
  private Mailing mailing;
  @Mock
  private Campanha campanha;
  @Mock
  private Telefone telefone;
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
    when(motivoFinalizacaoDao.procura("Sem telefones")).thenReturn(motivoFinalizacao);
    when(cliente.getMailing()).thenReturn(mailing);
    when(mailing.getCampanha()).thenReturn(campanha);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    processaSemTelefones = new ProcessaSemTelefones(finalizadorCliente, notificadorCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaSemTelefones.getOrdem(), is(equalTo(1)));
  }

  @Test
  public void acceptNaoDeveriaRetornarFalse() {
    when(ligacao.getMotivoFinalizacao()).thenReturn(MotivoSistema.ATENDIDA.getCodigo());
    assertThat(processaSemTelefones.accept(tenant, ligacao, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void acceptNaoDeveriaRetornarTrue() {
    when(ligacao.getMotivoFinalizacao()).thenReturn(MotivoSistema.SEM_TELEFONES.getCodigo());
    assertThat(processaSemTelefones.accept(tenant, ligacao, resultadoLigacao, daoFactory), is(true));
  }

  @Test
  public void processaDeveriaFinalizar() throws Exception {
    processaSemTelefones.executa(tenant, ligacao, resultadoLigacao, daoFactory);
    verify(finalizadorCliente).finaliza(tenant, daoFactory, cliente, motivoFinalizacao);
  }

  @Test
  public void processaDeveriaNotificar() throws Exception {
    processaSemTelefones.executa(tenant, ligacao, resultadoLigacao, daoFactory);
    verify(notificadorCliente).notificaFinalizacao(tenant, daoFactory, ligacao, cliente,
        resultadoLigacao, telefone, false, campanha);
  }

  @Test
  public void processaDeveriaRetornarFalse() throws Exception {
    assertThat(processaSemTelefones.executa(tenant, ligacao, resultadoLigacao, daoFactory),
        is(false));
  }

}
