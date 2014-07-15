package al.jdi.core.modelo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Cliente;

public class DiscavelFactoryTsaTest {

  private DiscavelFactoryTsa discavelFactoryTsaImpl;

  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Cliente cliente;

  @Before
  public void setUp() throws Exception {
    discavelFactoryTsaImpl = new DiscavelFactoryTsa();
  }

  @Test
  public void createDeveriaCriarDiscavel() {
    Discavel discavel = discavelFactoryTsaImpl.create(configuracoes, cliente);
    assertThat(discavel, is(not(nullValue(Discavel.class))));
  }

  @Test
  public void createDeveriaCriarDiscavelComCliente() {
    Discavel discavel = discavelFactoryTsaImpl.create(configuracoes, cliente);
    assertThat(discavel.getCliente(), is(sameInstance(cliente)));
  }

}
