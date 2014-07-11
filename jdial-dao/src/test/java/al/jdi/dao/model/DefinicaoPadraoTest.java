package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class DefinicaoPadraoTest {

  private DefinicaoPadrao definicaoPadrao;

  @Test
  public void defaultCriacaoModificacao() {
    assertThat(definicaoPadrao.getCriacaoModificacao(),
        is(not(nullValue(CriacaoModificacao.class))));
  }

  @Before
  public void setUp() {
    definicaoPadrao = new DefinicaoPadrao();
  }

}
