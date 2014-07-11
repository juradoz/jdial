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

import al.jdi.dao.model.AreaArea;
import al.jdi.dao.model.CriacaoModificacao;
import al.jdi.dao.model.Telefone;

public class DefaultAreaAreaDaoTest {

  private static final Long ID = 1l;
  private static final String S = "S";
  @Mock
  private DefaultDao<AreaArea> dao;
  @Mock
  private Session session;
  @Mock
  private Criteria criteria;
  @Mock
  private List<AreaArea> areaAreas;
  @Mock
  private Telefone telefone;
  @Mock
  private AreaArea areaArea;
  @Mock
  private CriacaoModificacao criacaoModificacao;

  private DefaultAreaAreaDao defaultAreaAreaDao;


  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(dao.getSession()).thenReturn(session);
    when(session.createCriteria(AreaArea.class)).thenReturn(criteria);
    when(criteria.add(Mockito.any(Criterion.class))).thenReturn(criteria);
    when(criteria.list()).thenReturn(areaAreas);
    when(areaArea.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    defaultAreaAreaDao = new DefaultAreaAreaDao(dao);
  }

  @Test
  public void isConurbadaDeveriaRetornarFalseCelular() throws Exception {
    when(telefone.isCelular()).thenReturn(true);
    assertThat(defaultAreaAreaDao.isConurbada(telefone), is(false));
  }

  @Test
  public void isConurbadaDeveriaRetornarFalseVazio() throws Exception {
    when(telefone.isCelular()).thenReturn(false);
    when(areaAreas.isEmpty()).thenReturn(true);
    assertThat(defaultAreaAreaDao.isConurbada(telefone), is(false));
  }

  @Test
  public void isConurbadaDeveriaRetornarFalseComResult() throws Exception {
    when(telefone.isCelular()).thenReturn(false);
    when(areaAreas.isEmpty()).thenReturn(false);
    assertThat(defaultAreaAreaDao.isConurbada(telefone), is(true));
  }

  @Test
  public void adiciona() throws Exception {
    defaultAreaAreaDao.adiciona(areaArea);
    verify(dao).adiciona(areaArea);
  }

  @Test
  public void atualiza() throws Exception {
    defaultAreaAreaDao.atualiza(areaArea);
    verify(dao).atualiza(areaArea);
  }

  @Test
  public void listaTudo() throws Exception {
    when(dao.listaTudo()).thenReturn(areaAreas);
    assertThat(defaultAreaAreaDao.listaTudo(), is(sameInstance(areaAreas)));
  }

  @Test
  public void procuraId() throws Exception {
    when(dao.procura(ID)).thenReturn(areaArea);
    assertThat(defaultAreaAreaDao.procura(ID), is(sameInstance(areaArea)));
  }

  @Test
  public void remove() throws Exception {
    defaultAreaAreaDao.remove(areaArea);
    verify(dao).remove(areaArea);
  }

  @Test
  public void procuraS() throws Exception {
    when(dao.procura(S)).thenReturn(areaArea);
    assertThat(defaultAreaAreaDao.procura(S), is(sameInstance(areaArea)));
  }

}
