package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class DefinicaoTest {

  private Definicao definicao;

  @Test
  public void defaultCriacaoModificacao() {
    assertThat(definicao.getCriacaoModificacao(), is(not(nullValue(CriacaoModificacao.class))));
  }

  @Before
  public void setUp() {
    definicao = new Definicao();
  }

}
