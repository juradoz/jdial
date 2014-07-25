package al.jdi.core.devolveregistro;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.AgendamentoDao;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaRemoveTodosAgendamentosTest {

  private ProcessaRemoveTodosAgendamentos processaRemoveTodosAgendamentos;

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
  private Agendamento agendamento;
  @Mock
  private AgendamentoDao agendamentoDao;
  @Mock
  private ClienteDao clienteDao;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  private LinkedList<Agendamento> agendamentos;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    agendamentos = new LinkedList<Agendamento>(asList(agendamento));
    when(cliente.getAgendamento()).thenReturn(agendamentos);
    when(daoFactory.getAgendamentoDao()).thenReturn(agendamentoDao);
    when(tratadorEspecificoClienteFactory.create(tenant, daoFactory)).thenReturn(
        tratadorEspecificoCliente);
    when(tratadorEspecificoCliente.obtemClienteDao()).thenReturn(clienteDao);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    processaRemoveTodosAgendamentos =
        new ProcessaRemoveTodosAgendamentos(tratadorEspecificoClienteFactory);
  }

  @Test
  public void getOrdemDeveriaRetornar3() {
    assertThat(processaRemoveTodosAgendamentos.getOrdem(), is(3));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isLimpaAgendamentos()).thenReturn(true);
    assertThat(
        processaRemoveTodosAgendamentos.accept(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isLimpaAgendamentos()).thenReturn(false);
    assertThat(
        processaRemoveTodosAgendamentos.accept(tenant, ligacao, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void processaDeveriaLimparAgendamentos() throws Exception {
    processaRemoveTodosAgendamentos.executa(tenant, ligacao, resultadoLigacao, daoFactory);
    assertThat(agendamentos.isEmpty(), is(true));
  }

  @Test
  public void processaDeveriaRemoverAgendamento() throws Exception {
    processaRemoveTodosAgendamentos.executa(tenant, ligacao, resultadoLigacao, daoFactory);
    verify(agendamentoDao).remove(agendamento);
  }

  @Test
  public void processaDeveriaRemoverVariosAgendamento() throws Exception {
    agendamentos.add(agendamento);
    processaRemoveTodosAgendamentos.executa(tenant, ligacao, resultadoLigacao, daoFactory);
    verify(agendamentoDao, times(2)).remove(agendamento);
  }

  @Test
  public void processaDeveriaRetornarTrue() throws Exception {
    assertThat(
        processaRemoveTodosAgendamentos.executa(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
  }
}
