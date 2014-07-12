package al.jdi.core.modelo;

import static ch.lambdaj.Lambda.forEach;
import static org.apache.commons.lang3.StringUtils.repeat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Telefone;

public class DefaultTelefoneSorterTest {

  private DefaultTelefoneSorter defaultTelefoneSorter;
  
  @Mock
  private Configuracoes configuracoes;

  private Telefone t1;
  private Telefone t2;
  private Telefone t3;
  private LinkedList<Telefone> telefones;

  @Before
  public void setUp() throws Exception {
    initMocks(this);

    t1 = new Telefone();
    t1.setId(1l);
    t1.setDdd(repeat("1", 2));
    t1.setTelefone(repeat("1", 8));
    t1.setPrioridade(3);

    t2 = new Telefone();
    t2.setId(2l);
    t2.setDdd(repeat("2", 2));
    t2.setTelefone(repeat("2", 8));
    t2.setPrioridade(2);

    t3 = new Telefone();
    t3.setId(3l);
    t3.setDdd(repeat("3", 2));
    t3.setTelefone(repeat("3", 8));
    t3.setPrioridade(1);

    telefones = new LinkedList<Telefone>(Arrays.asList(t3, t2, t1));

    defaultTelefoneSorter = new DefaultTelefoneSorter(configuracoes);
  }

  @Test
  public void sortDeveriaOrdenarOrdenacaoSimples() throws Exception {
    when(configuracoes.isPriorizaCelular()).thenReturn(true);
    List<Telefone> result = defaultTelefoneSorter.sort(telefones);
    assertThat(result.get(0), is(sameInstance(t1)));
    assertThat(result.get(1), is(sameInstance(t2)));
    assertThat(result.get(2), is(sameInstance(t3)));
  }

  @Test
  public void sortDeveriaOrdenarPrioridade() throws Exception {
    List<Telefone> result = defaultTelefoneSorter.sort(telefones);
    assertThat(result.get(0), is(sameInstance(t3)));
    assertThat(result.get(1), is(sameInstance(t2)));
    assertThat(result.get(2), is(sameInstance(t1)));
  }

  @Test
  public void sortDeveriaOrdenarId() throws Exception {
    forEach(telefones).setPrioridade(0);
    List<Telefone> result = defaultTelefoneSorter.sort(telefones);
    assertThat(result.get(0), is(sameInstance(t1)));
    assertThat(result.get(1), is(sameInstance(t2)));
    assertThat(result.get(2), is(sameInstance(t3)));
  }

}
