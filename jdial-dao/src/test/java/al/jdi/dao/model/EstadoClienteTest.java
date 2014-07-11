package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class EstadoClienteTest {

  private EstadoCliente estadoCliente;

  @Test
  public void defaultCriacaoModifiacao() {
    assertThat(estadoCliente.getCriacaoModificacao(), is(not(nullValue(CriacaoModificacao.class))));
  }

  @Before
  public void setUp() {
    estadoCliente = new EstadoCliente();
  }

}
