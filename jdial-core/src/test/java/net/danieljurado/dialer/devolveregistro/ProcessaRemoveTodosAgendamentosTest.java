package net.danieljurado.dialer.devolveregistro;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.LinkedList;

import net.danieljurado.dialer.modelo.Ligacao;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.dao.beans.AgendamentoDao;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaRemoveTodosAgendamentosTest {

  @Mock
  private TratadorEspecificoCliente tratadorEspecificoCliente;
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

  private ProcessaRemoveTodosAgendamentos processaRemoveTodosAgendamentos;
  private LinkedList<Agendamento> agendamentos;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    agendamentos = new LinkedList<Agendamento>(asList(agendamento));
    when(cliente.getAgendamento()).thenReturn(agendamentos);
    when(daoFactory.getAgendamentoDao()).thenReturn(agendamentoDao);
    when(tratadorEspecificoCliente.obtemClienteDao(daoFactory)).thenReturn(clienteDao);
    processaRemoveTodosAgendamentos =
        new ProcessaRemoveTodosAgendamentos(tratadorEspecificoCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar3() {
    assertThat(processaRemoveTodosAgendamentos.getOrdem(), is(3));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isLimpaAgendamentos()).thenReturn(true);
    assertThat(
        processaRemoveTodosAgendamentos.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isLimpaAgendamentos()).thenReturn(false);
    assertThat(
        processaRemoveTodosAgendamentos.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void processaDeveriaLimparAgendamentos() throws Exception {
    processaRemoveTodosAgendamentos.executa(ligacao, cliente, resultadoLigacao, daoFactory);
    assertThat(agendamentos.isEmpty(), is(true));
  }

  @Test
  public void processaDeveriaRemoverAgendamento() throws Exception {
    processaRemoveTodosAgendamentos.executa(ligacao, cliente, resultadoLigacao, daoFactory);
    verify(agendamentoDao).remove(agendamento);
  }

  @Test
  public void processaDeveriaRemoverVariosAgendamento() throws Exception {
    agendamentos.add(agendamento);
    processaRemoveTodosAgendamentos.executa(ligacao, cliente, resultadoLigacao, daoFactory);
    verify(agendamentoDao, times(2)).remove(agendamento);
  }

  @Test
  public void processaDeveriaRetornarTrue() throws Exception {
    assertThat(
        processaRemoveTodosAgendamentos.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }
}
