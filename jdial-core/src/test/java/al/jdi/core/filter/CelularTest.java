package al.jdi.core.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.filter.CelularChecker;
import al.jdi.dao.model.Telefone;

public class CelularTest {

  @Mock
  private Telefone telefone;

  private CelularChecker celular;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    celular = new CelularChecker();
  }

  @Test
  public void isCelularDeveriaRetornarTrue9() throws Exception {
    when(telefone.getTelefone()).thenReturn("91111111");
    assertThat(celular.isCelular(telefone), is(true));
  }

  @Test
  public void isCelularDeveriaRetornarTrue9NonoDigito() throws Exception {
    when(telefone.getTelefone()).thenReturn("911111111");
    assertThat(celular.isCelular(telefone), is(true));
  }

  @Test
  public void isCelularDeveriaRetornarTrue8() throws Exception {
    when(telefone.getTelefone()).thenReturn("81111111");
    assertThat(celular.isCelular(telefone), is(true));
  }

  @Test
  public void isCelularDeveriaRetornarTrue7() throws Exception {
    when(telefone.getTelefone()).thenReturn("71111111");
    assertThat(celular.isCelular(telefone), is(true));
  }

  @Test
  public void isCelularDeveriaRetornarTrue6() throws Exception {
    when(telefone.getTelefone()).thenReturn("61111111");
    assertThat(celular.isCelular(telefone), is(true));
  }

  @Test
  public void isCelularDeveriaRetornarFalse5() throws Exception {
    when(telefone.getTelefone()).thenReturn("51111111");
    assertThat(celular.isCelular(telefone), is(false));
  }

  @Test
  public void isCelularDeveriaRetornarFalse4() throws Exception {
    when(telefone.getTelefone()).thenReturn("41111111");
    assertThat(celular.isCelular(telefone), is(false));
  }

  @Test
  public void isCelularDeveriaRetornar3() throws Exception {
    when(telefone.getTelefone()).thenReturn("31111111");
    assertThat(celular.isCelular(telefone), is(false));
  }

  @Test
  public void isCelularDeveriaRetornarFalse2() throws Exception {
    when(telefone.getTelefone()).thenReturn("21111111");
    assertThat(celular.isCelular(telefone), is(false));
  }

  @Test
  public void isCelularDeveriaRetornarFalse1() throws Exception {
    when(telefone.getTelefone()).thenReturn("11111111");
    assertThat(celular.isCelular(telefone), is(false));
  }

}
