package al.jdi.dao.beans;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.CriacaoModificacao;
import al.jdi.dao.model.Definicao;
import al.jdi.dao.model.DefinicaoPadrao;

public class DefaultCampanhaDaoTest {

  private static final Long ID = 1l;
  private static final String S = "S";

  private DefaultCampanhaDao defaultCampanhaDao;

  @Mock
  private DefaultDao<Campanha> dao;
  @Mock
  private Session session;
  @Mock
  private Criteria criteriaDefinicaoPadrao;
  @Mock
  private DefinicaoPadrao definicaoPadrao;
  @Mock
  private Campanha campanha;
  @Mock
  private List<Definicao> definicaos;
  @Mock
  private CriacaoModificacao criacaoModificacao;
  @Mock
  private List<Campanha> campanhas;

  private List<DefinicaoPadrao> definicaoPadraos;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    definicaoPadraos = new LinkedList<DefinicaoPadrao>(Arrays.asList(definicaoPadrao));
    when(dao.getSession()).thenReturn(session);
    when(session.createCriteria(DefinicaoPadrao.class)).thenReturn(criteriaDefinicaoPadrao);
    when(criteriaDefinicaoPadrao.list()).thenReturn(definicaoPadraos);
    when(campanha.getDefinicao()).thenReturn(definicaos);
    when(campanha.getCriacaoModificacao()).thenReturn(criacaoModificacao);

    defaultCampanhaDao = new DefaultCampanhaDao(dao);
  }

  @Test
  public void adiciona() throws Exception {
    defaultCampanhaDao.adiciona(campanha);
    verify(dao).adiciona(campanha);
  }

  @Test
  public void adicionaDeveriaInserirDefinicao() throws Exception {
    defaultCampanhaDao.adiciona(campanha);
    verify(definicaos).add(Mockito.any(Definicao.class));
  }

  @Test
  public void adicionaDeveriaAtualizar() throws Exception {
    defaultCampanhaDao.adiciona(campanha);
    verify(dao).atualiza(campanha);
  }

  @Test
  public void atualiza() throws Exception {
    defaultCampanhaDao.atualiza(campanha);
    verify(dao).atualiza(campanha);
  }

  @Test
  public void listaTudo() throws Exception {
    when(dao.listaTudo()).thenReturn(campanhas);
    assertThat(defaultCampanhaDao.listaTudo(), is(sameInstance(campanhas)));
  }

  @Test
  public void procuraId() throws Exception {
    when(dao.procura(ID)).thenReturn(campanha);
    assertThat(defaultCampanhaDao.procura(ID), is(sameInstance(campanha)));
  }

  @Test
  public void remove() throws Exception {
    defaultCampanhaDao.remove(campanha);
    verify(dao).remove(campanha);
  }

  @Test
  public void procuraS() throws Exception {
    when(dao.procura(S)).thenReturn(campanha);
    assertThat(defaultCampanhaDao.procura(S), is(sameInstance(campanha)));
  }

}
