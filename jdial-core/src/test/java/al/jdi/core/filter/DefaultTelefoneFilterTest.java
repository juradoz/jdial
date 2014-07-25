package al.jdi.core.filter;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Telefone;

public class DefaultTelefoneFilterTest {

  private DefaultTelefoneFilter defaultTelefoneFilter;

  @Mock
  private TelefoneUtil checker1;
  @Mock
  private TelefoneUtil checker2;
  @Mock
  private Telefone t1;
  @Mock
  private Telefone t2;
  @Mock
  private Telefone t3;
  @Mock
  private Configuracoes configuracoes;

  private HashSet<TelefoneUtil> checkers;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    checkers = new HashSet<TelefoneUtil>(asList(checker1, checker2));
    when(checker1.isUtil(configuracoes, t1)).thenReturn(true);
    when(checker1.isUtil(configuracoes, t2)).thenReturn(true);
    when(checker1.isUtil(configuracoes, t3)).thenReturn(true);
    when(checker2.isUtil(configuracoes, t1)).thenReturn(true);
    when(checker2.isUtil(configuracoes, t2)).thenReturn(true);
    when(checker2.isUtil(configuracoes, t3)).thenReturn(true);
    defaultTelefoneFilter = new DefaultTelefoneFilter(checkers);
  }

  @Test
  public void filterNaoDeveriaRemoverNenhum() throws Exception {
    List<Telefone> list = defaultTelefoneFilter.filter(configuracoes, asList(t1, t2, t3));
    assertThat(list.containsAll(asList(t1, t2, t3)), is(true));
  }

  @Test
  public void filterDeveriaFiltrart1() throws Exception {
    when(checker1.isUtil(configuracoes, t1)).thenReturn(false);
    List<Telefone> list = defaultTelefoneFilter.filter(configuracoes, asList(t1, t2, t3));
    assertThat(list.contains(t1), is(false));
    assertThat(list.contains(t2), is(true));
    assertThat(list.contains(t3), is(true));
  }

  @Test
  public void filterDeveriaFiltrart2() throws Exception {
    when(checker1.isUtil(configuracoes, t2)).thenReturn(false);
    List<Telefone> list = defaultTelefoneFilter.filter(configuracoes, asList(t1, t2, t3));
    assertThat(list.contains(t1), is(true));
    assertThat(list.contains(t2), is(false));
    assertThat(list.contains(t3), is(true));
  }

  @Test
  public void filterDeveriaFiltrart3() throws Exception {
    when(checker1.isUtil(configuracoes, t3)).thenReturn(false);
    List<Telefone> list = defaultTelefoneFilter.filter(configuracoes, asList(t1, t2, t3));
    assertThat(list.contains(t1), is(true));
    assertThat(list.contains(t2), is(true));
    assertThat(list.contains(t3), is(false));
  }

  @Test
  public void filterDeveriaFiltrarTudo() throws Exception {
    when(checker1.isUtil(Mockito.eq(configuracoes), Mockito.any(Telefone.class))).thenReturn(false);
    List<Telefone> list = defaultTelefoneFilter.filter(configuracoes, asList(t1, t2, t3));
    assertThat(list.isEmpty(), is(true));
  }

}
