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

import al.jdi.dao.model.CriacaoModificacao;
import al.jdi.dao.model.Telefone;

public class DefaultDaoTest {

  private static final Long ID = 1l;

  private DefaultDao<Telefone> dao;

  @Mock
  private Session session;
  @Mock
  private Telefone telefone;
  @Mock
  private CriacaoModificacao criacaoModificacao;
  @Mock
  private Criteria criteria;
  @Mock
  private List<Telefone> telefones;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(telefone.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    when(session.createCriteria(Telefone.class)).thenReturn(criteria);
    when(criteria.list()).thenReturn(telefones);
    dao = new DefaultDao<Telefone>(session, Telefone.class);
  }

  @Test
  public void adiciona() throws Exception {
    dao.adiciona(telefone);
    verify(session).save(telefone);
  }

  @Test
  public void atualizaDeveriaPreencherModificacao() throws Exception {
    dao.atualiza(telefone);
    verify(criacaoModificacao).setModificacao(Mockito.any(DateTime.class));
  }

  @Test
  public void atualiza() throws Exception {
    dao.atualiza(telefone);
    verify(session).update(telefone);
  }

  @Test
  public void getSession() throws Exception {
    assertThat(dao.getSession(), is(sameInstance(session)));
  }

  @Test
  public void listaTudo() throws Exception {
    assertThat(dao.listaTudo(), is(sameInstance(telefones)));
  }

  @Test
  public void procura() throws Exception {
    when(session.load(Telefone.class, ID)).thenReturn(telefone);
    assertThat(dao.procura(ID), is(sameInstance(telefone)));
  }

  @Test
  public void remove() throws Exception {
    dao.remove(telefone);
    verify(session).delete(telefone);
  }

  // TODO: Testar procura(S)

}
