package net.danieljurado.dialer.filter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.dao.model.Telefone;

public class CelularCheckerTest {

  @Mock
  private Telefone telefone;

  private CelularChecker celularChecker;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    celularChecker = new CelularChecker();
  }

  @Test
  public void test9() {
    when(telefone.getTelefone()).thenReturn("9");
    assertThat(celularChecker.isCelular(telefone), is(equalTo(true)));
  }

  @Test
  public void test8() {
    when(telefone.getTelefone()).thenReturn("8");
    assertThat(celularChecker.isCelular(telefone), is(equalTo(true)));
  }

  @Test
  public void test7() {
    when(telefone.getTelefone()).thenReturn("7");
    assertThat(celularChecker.isCelular(telefone), is(equalTo(true)));
  }

  @Test
  public void test6() {
    when(telefone.getTelefone()).thenReturn("6");
    assertThat(celularChecker.isCelular(telefone), is(equalTo(true)));
  }

}
