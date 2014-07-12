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
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.dao.model.Agente;
import al.jdi.dao.model.CriacaoModificacao;

public class DefaultDaoTest {

  private static final Long ID = 1l;

  private DefaultDao<Agente> dao;

  @Mock
  private Session session;
  @Mock
  private Agente agente;
  @Mock
  private CriacaoModificacao criacaoModificacao;
  @Mock
  private Criteria criteria;
  @Mock
  private List<Agente> agentes;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(agente.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    when(session.createCriteria(Agente.class)).thenReturn(criteria);
    when(criteria.list()).thenReturn(agentes);
    dao = new DefaultDao<Agente>(session, Agente.class);
  }

  @Test
  public void adiciona() throws Exception {
    dao.adiciona(agente);
    verify(session).save(agente);
  }

  @Test
  public void atualizaDeveriaPreencherModificacao() throws Exception {
    dao.atualiza(agente);
    verify(criacaoModificacao).setModificacao(Mockito.any(DateTime.class));
  }

  @Test
  public void atualiza() throws Exception {
    dao.atualiza(agente);
    verify(session).update(agente);
  }

  @Test
  public void getSession() throws Exception {
    assertThat(dao.getSession(), is(sameInstance(session)));
  }

  @Test
  public void listaTudo() throws Exception {
    assertThat(dao.listaTudo(), is(sameInstance(agentes)));
  }

  @Test
  public void procura() throws Exception {
    when(session.load(Agente.class, ID)).thenReturn(agente);
    assertThat(dao.procura(ID), is(sameInstance(agente)));
  }

  @Test
  public void remove() throws Exception {
    dao.remove(agente);
    verify(session).delete(agente);
  }

  // TODO: Testar procura(S)

}
