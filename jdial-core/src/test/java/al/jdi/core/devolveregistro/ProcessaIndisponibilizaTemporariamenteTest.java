package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaIndisponibilizaTemporariamenteTest {

  private ProcessaIndisponibilizaTemporariamente processaIndisponibilizaTemporariamente;

  @Mock
  private TratadorEspecificoCliente tratadorEspecificoCliente;
  @Mock
  private TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private ClienteDao clienteDao;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(tratadorEspecificoClienteFactory.create(tenant, daoFactory)).thenReturn(
        tratadorEspecificoCliente);
    when(tratadorEspecificoCliente.obtemClienteDao()).thenReturn(clienteDao);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    processaIndisponibilizaTemporariamente =
        new ProcessaIndisponibilizaTemporariamente(tratadorEspecificoClienteFactory);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaIndisponibilizaTemporariamente.getOrdem(), is(10));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    assertThat(processaIndisponibilizaTemporariamente.accept(tenant, ligacao, resultadoLigacao,
        daoFactory), is(true));
  }

  @Test
  public void executaDeveriaSetarNull() throws Exception {
    assertThat(processaIndisponibilizaTemporariamente.executa(tenant, ligacao, resultadoLigacao,
        daoFactory), is(true));
    verify(cliente).setDisponivelAPartirDe(null);
  }

  @Test
  public void executaDeveriaSetarData() throws Exception {
    when(resultadoLigacao.getIntervaloIndisponivel()).thenReturn(1);
    assertThat(processaIndisponibilizaTemporariamente.executa(tenant, ligacao, resultadoLigacao,
        daoFactory), is(true));
    ArgumentCaptor<DateTime> captor = ArgumentCaptor.forClass(DateTime.class);
    verify(cliente).setDisponivelAPartirDe(captor.capture());
    assertThat(captor.getValue().isAfterNow(), is(true));
  }

  @Test
  public void executaDeveriaAtualizar() throws Exception {
    assertThat(processaIndisponibilizaTemporariamente.executa(tenant, ligacao, resultadoLigacao,
        daoFactory), is(true));
    verify(clienteDao).atualiza(cliente);
  }
}
