package al.jdi.dao.beans;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.dao.model.Agente;
import al.jdi.dao.model.CriacaoModificacao;
import al.jdi.dao.model.Mailing;

public class DefaultAgenteDaoTest {

  private static final Long ID = 1l;
  private static final String S = "S";

  private DefaultAgenteDao defaultAgenteDao;

  @Mock
  private DefaultDao<Agente> dao;
  @Mock
  private Session session;
  @Mock
  private Criteria criteria;
  @Mock
  private List<Agente> agentes;
  @Mock
  private Collection<Mailing> mailings;
  @Mock
  private Agente agente;
  @Mock
  private CriacaoModificacao criacaoModificacao;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(dao.getSession()).thenReturn(session);
    when(session.createCriteria(Agente.class)).thenReturn(criteria);
    when(criteria.add(Mockito.any(Criterion.class))).thenReturn(criteria);
    when(criteria.createAlias(Mockito.anyString(), Mockito.anyString())).thenReturn(criteria);
    when(criteria.setProjection(Mockito.any(Projection.class))).thenReturn(criteria);
    when(criteria.list()).thenReturn((List<Agente>) agentes);
    when(agente.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    defaultAgenteDao = new DefaultAgenteDao(dao);
  }

  @Test
  public void agentesComAgendaShouldReturn() throws Exception {
    assertThat(defaultAgenteDao.agentesComAgenda(mailings), is(sameInstance(agentes)));
  }

  @Test
  public void adiciona() throws Exception {
    defaultAgenteDao.adiciona(agente);
    verify(dao).adiciona(agente);
  }

  @Test
  public void atualiza() throws Exception {
    defaultAgenteDao.atualiza(agente);
    verify(dao).atualiza(agente);
  }

  @Test
  public void listaTudo() throws Exception {
    when(dao.listaTudo()).thenReturn(agentes);
    assertThat(defaultAgenteDao.listaTudo(), is(sameInstance(agentes)));
  }

  @Test
  public void procuraId() throws Exception {
    when(dao.procura(ID)).thenReturn(agente);
    assertThat(defaultAgenteDao.procura(ID), is(sameInstance(agente)));
  }

  @Test
  public void remove() throws Exception {
    defaultAgenteDao.remove(agente);
    verify(dao).remove(agente);
  }

  @Test
  public void procuraS() throws Exception {
    when(dao.procura(S)).thenReturn(agente);
    assertThat(defaultAgenteDao.procura(S), is(sameInstance(agente)));
  }

}
