package al.jdi.core.devolveregistro;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.AgendamentoDao;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.HistoricoLigacaoDao;
import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Agente;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.EstadoCliente;
import al.jdi.dao.model.HistoricoCliente;
import al.jdi.dao.model.HistoricoLigacao;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaAgendamentoTest {

  private ProcessaAgendamento processaAgendamento;

  @Mock
  private TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
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
  private HistoricoLigacaoDao historicoLigacaoDao;
  @Mock
  private HistoricoLigacao historicoLigacao;
  @Mock
  private List<Agendamento> agendamentos;
  @Mock
  private AgendamentoDao agendamentoDao;
  @Mock
  private Dao<HistoricoCliente> historicoClienteDao;
  @Mock
  private ClienteDao clienteDao;
  @Mock
  private Agendamento agendamento;
  @Mock
  private EstadoCliente estadoCliente;
  @Mock
  private Logger logger;
  @Mock
  private Configuracoes configuracoes;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getHistoricoLigacaoDao()).thenReturn(historicoLigacaoDao);
    when(daoFactory.getAgendamentoDao()).thenReturn(agendamentoDao);
    when(daoFactory.getHistoricoClienteDao()).thenReturn(historicoClienteDao);
    when(tratadorEspecificoClienteFactory.create(configuracoes, daoFactory)).thenReturn(
        tratadorEspecificoCliente);
    when(tratadorEspecificoCliente.obtemClienteDao()).thenReturn(clienteDao);
    when(cliente.getAgendamento()).thenReturn(agendamentos);
    when(cliente.getEstadoCliente()).thenReturn(estadoCliente);
    processaAgendamento = new ProcessaAgendamento(logger, tratadorEspecificoClienteFactory);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaAgendamento.getOrdem(), is(8));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.getIntervaloReagendamento()).thenReturn(10);
    assertThat(
        processaAgendamento.accept(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    assertThat(
        processaAgendamento.accept(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void executaDeveriaLimparAgendamentosPorHistoricoNoIntervalo() throws Exception {
    when(historicoLigacaoDao.procura(eq(cliente), eq(resultadoLigacao), any(DateTime.class)))
        .thenReturn(asList(historicoLigacao, historicoLigacao));
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(agendamentos).clear();
  }

  @Test
  public void executaDeveriaAtualizarPorHistoricoNoIntervalo() throws Exception {
    when(historicoLigacaoDao.procura(eq(cliente), eq(resultadoLigacao), any(DateTime.class)))
        .thenReturn(asList(historicoLigacao, historicoLigacao));
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(clienteDao).atualiza(cliente);
  }

  @Test
  public void executaNaoDeveriaLimparAgendamentosPorHistoricoNoIntervalo() throws Exception {
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(agendamentos, never()).clear();
  }

  @Test
  public void executaDeveriaAdicionarNovoAgendamento() throws Exception {
    when(resultadoLigacao.getIntervaloReagendamento()).thenReturn(10);
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(agendamentoDao).adiciona(any(Agendamento.class));
  }

  @Test
  public void verificaNovoAgendamentoAdicionado() throws Exception {
    when(resultadoLigacao.getIntervaloReagendamento()).thenReturn(10);
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    ArgumentCaptor<Agendamento> captor = ArgumentCaptor.forClass(Agendamento.class);
    verify(agendamentoDao).adiciona(captor.capture());
    Agendamento agendamento = captor.getValue();
    assertThat(agendamento.getCliente(), is(sameInstance(cliente)));
    assertThat(agendamento.getAgendamento().isAfterNow(), is(true));
  }

  @Test
  public void exeutaDeveriaAlterarAgendamento() throws Exception {
    when(agendamentoDao.procura(cliente)).thenReturn(agendamento);
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(agendamento).setAgendamento(any(DateTime.class));
  }

  @Test
  public void executaDeveriaAtualizarAgendamento() throws Exception {
    when(agendamentoDao.procura(cliente)).thenReturn(agendamento);
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(agendamentoDao).atualiza(agendamento);
  }

  @Test
  public void executaDeveriaInserirHistorico() throws Exception {
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    verify(historicoClienteDao).adiciona(any(HistoricoCliente.class));
  }

  @Test
  public void verificaHistoricoInserido() throws Exception {
    when(resultadoLigacao.getIntervaloReagendamento()).thenReturn(10);
    assertThat(
        processaAgendamento.executa(configuracoes, ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
    ArgumentCaptor<HistoricoCliente> captor = ArgumentCaptor.forClass(HistoricoCliente.class);
    verify(historicoClienteDao).adiciona(captor.capture());
    HistoricoCliente historicoCliente = captor.getValue();
    assertThat(historicoCliente.getCliente(), is(sameInstance(cliente)));
    assertThat(historicoCliente.getAgendamento().isAfterNow(), is(true));
    assertThat(historicoCliente.getAgente(), is(nullValue(Agente.class)));
    assertThat(historicoCliente.getEstadoCliente(), is(sameInstance(estadoCliente)));
    assertThat(historicoCliente.getDescricao(), startsWith("Agendamento de registro por"));
  }
}
