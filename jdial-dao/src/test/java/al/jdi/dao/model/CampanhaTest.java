package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class CampanhaTest {

  private Campanha campanha;

  @Test
  public void defaultCriacaoModificacao() {
    assertThat(campanha.getCriacaoModificacao(), is(not(nullValue(CriacaoModificacao.class))));
  }

  @Test
  public void defaultFiltroAtivo() {
    assertThat(campanha.isFiltroAtivo(), is(false));
  }

  @Before
  public void setUp() {
    campanha = new Campanha();
  }

}
