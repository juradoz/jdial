package al.jdi.core.devolveregistro;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public class NotificadorClienteTest {

  private NotificadorCliente notificadorCliente;

  private static final boolean INUTILIZA_DIFERENCIADO = false;
  @Mock
  private TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
  @Mock
  private TratadorEspecificoCliente tratadorEspecificoCliente;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private Telefone telefoneOriginal;
  @Mock
  private Campanha campanha;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Tenant tenant;

  private DateTime dataBanco;


  @Before
  public void setUp() throws Exception {
    dataBanco = new DateTime();
    initMocks(this);
    when(daoFactory.getDataBanco()).thenReturn(dataBanco);
    when(ligacao.isInutilizaComMotivoDiferenciado()).thenReturn(INUTILIZA_DIFERENCIADO);
    when(tratadorEspecificoClienteFactory.create(tenant, daoFactory)).thenReturn(
        tratadorEspecificoCliente);
    when(tenant.getCampanha()).thenReturn(campanha);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    notificadorCliente = new NotificadorCliente(tratadorEspecificoClienteFactory);
  }

  @Test
  public void notificaFimTentativaNaoDeveriaNotificar() throws Exception {
    when(resultadoLigacao.isNotificaFimTentativa()).thenReturn(false);
    notificadorCliente.notificaFimTentativa(tenant, daoFactory, ligacao, cliente, resultadoLigacao,
        telefoneOriginal, campanha);
    verify(tratadorEspecificoCliente, never()).notificaFimTentativa(tenant, ligacao, cliente,
        dataBanco, telefoneOriginal, resultadoLigacao, INUTILIZA_DIFERENCIADO);
  }

  @Test
  public void notificaFimTentativaDeveriaNotificar() throws Exception {
    when(resultadoLigacao.isNotificaFimTentativa()).thenReturn(true);
    notificadorCliente.notificaFimTentativa(tenant, daoFactory, ligacao, cliente, resultadoLigacao,
        telefoneOriginal, campanha);
    verify(tratadorEspecificoCliente).notificaFimTentativa(tenant, ligacao, cliente, dataBanco,
        telefoneOriginal, resultadoLigacao, INUTILIZA_DIFERENCIADO);
  }

  @Test
  public void notificaFinalizacaoNaoDeveriaNotificar() throws Exception {
    when(resultadoLigacao.isNotificaFimTentativa()).thenReturn(false);
    notificadorCliente.notificaFinalizacao(tenant, daoFactory, ligacao, cliente, resultadoLigacao,
        telefoneOriginal, INUTILIZA_DIFERENCIADO, campanha);
    verify(tratadorEspecificoCliente, never()).notificaFinalizacao(tenant, ligacao, cliente,
        dataBanco, telefoneOriginal, resultadoLigacao, INUTILIZA_DIFERENCIADO);
  }

  @Test
  public void notificaFinalizacaoDeveriaNotificar() throws Exception {
    when(resultadoLigacao.isNotificaFimTentativa()).thenReturn(true);
    notificadorCliente.notificaFinalizacao(tenant, daoFactory, ligacao, cliente, resultadoLigacao,
        telefoneOriginal, INUTILIZA_DIFERENCIADO, campanha);
    verify(tratadorEspecificoCliente).notificaFinalizacao(tenant, ligacao, cliente, dataBanco,
        telefoneOriginal, resultadoLigacao, INUTILIZA_DIFERENCIADO);
  }

}
