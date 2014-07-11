package al.jdi.dao.beans;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Agente;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.CriacaoModificacao;

public class DefaultAgendamentoDaoTest {

  private static final Long ID = 1l;
  private static final String S = "S";
  @Mock
  private Session session;
  @Mock
  private Agendamento agendamento;
  @Mock
  private CriacaoModificacao criacaoModificacao;
  @Mock
  private Cliente cliente;
  @Mock
  private List<Agendamento> agendamentos;
  @Mock
  private Agente agente;
  @Mock
  private Criteria criteria;
  @Mock
  private DefaultDao<Agendamento> dao;

  private DefaultAgendamentoDao defaultAgendamentoDao;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(dao.getSession()).thenReturn(session);
    when(session.createCriteria(Agendamento.class)).thenReturn(criteria);
    when(criteria.add(Mockito.any(Criterion.class))).thenReturn(criteria);
    when(criteria.uniqueResult()).thenReturn(agendamento);

    when(agendamento.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    when(agendamento.getCliente()).thenReturn(cliente);
    when(cliente.getAgendamento()).thenReturn(agendamentos);
    when(cliente.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    when(agente.getAgendamento()).thenReturn(agendamentos);
    when(agente.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    defaultAgendamentoDao = new DefaultAgendamentoDao(dao);
  }

  @Test
  public void adicionaShouldEmptyPreviousAgendamentos() throws Exception {
    when(agendamentos.isEmpty()).thenReturn(false);
    defaultAgendamentoDao.adiciona(agendamento);
    verify(agendamentos).clear();

  }

  @Test
  public void adicionaShouldAddAgendamento() throws Exception {
    defaultAgendamentoDao.adiciona(agendamento);
    verify(agendamentos).add(agendamento);
  }

  @Test
  public void adicionaShouldUpdateCliente() throws Exception {
    defaultAgendamentoDao.adiciona(agendamento);
    verify(session).update(cliente);
  }

  @Test
  public void adicionaShouldAddAgente() throws Exception {
    when(agendamento.getAgente()).thenReturn(agente);
    defaultAgendamentoDao.adiciona(agendamento);
    verify(dao).adiciona(agendamento);
  }

  @Test
  public void adicionaShouldUpdateAgente() throws Exception {
    when(agendamento.getAgente()).thenReturn(agente);
    defaultAgendamentoDao.adiciona(agendamento);
    verify(session).update(agente);
  }

  @Test
  public void procuraShouldLookForAgendamento() throws Exception {
    assertThat(defaultAgendamentoDao.procura(cliente), is(sameInstance(agendamento)));
  }

  @Test
  public void atualiza() throws Exception {
    defaultAgendamentoDao.atualiza(agendamento);
    verify(dao).atualiza(agendamento);
  }

  @Test
  public void listaTudo() throws Exception {
    when(dao.listaTudo()).thenReturn(agendamentos);
    assertThat(defaultAgendamentoDao.listaTudo(), is(sameInstance(agendamentos)));
  }

  @Test
  public void procuraId() throws Exception {
    when(dao.procura(ID)).thenReturn(agendamento);
    assertThat(defaultAgendamentoDao.procura(ID), is(sameInstance(agendamento)));
  }

  @Test
  public void remove() throws Exception {
    defaultAgendamentoDao.remove(agendamento);
    verify(dao).remove(agendamento);
  }

  @Test
  public void procuraString() throws Exception {
    when(dao.procura(S)).thenReturn(agendamento);
    assertThat(defaultAgendamentoDao.procura(S), is(sameInstance(agendamento)));
  }

}
