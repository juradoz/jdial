package al.jdi.core.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Telefone;

public class BloqueiaCelularUtilTest {

  private BloqueioCelularUtil bloqueiaCelularUtil;

  @Mock
  private CelularChecker telefoneCelularChecker;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Telefone telefone;
  @Mock
  private Tenant tenant;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(configuracoes.bloqueiaCelular()).thenReturn(true);
    when(telefoneCelularChecker.isCelular(telefone)).thenReturn(true);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    bloqueiaCelularUtil = new BloqueioCelularUtil(telefoneCelularChecker);
  }

  @Test
  public void isUtilDeveriaRetornarFalse() throws Exception {
    assertThat(bloqueiaCelularUtil.isUtil(tenant, telefone), is(false));
  }

  @Test
  public void isUtilDeveriaRetornarTrueConfiguracoes() throws Exception {
    when(configuracoes.bloqueiaCelular()).thenReturn(false);
    when(telefoneCelularChecker.isCelular(telefone)).thenReturn(true);
    assertThat(bloqueiaCelularUtil.isUtil(tenant, telefone), is(true));
  }

  @Test
  public void isUtilDeveriaRetornarTrueChecker() throws Exception {
    when(telefoneCelularChecker.isCelular(telefone)).thenReturn(false);
    assertThat(bloqueiaCelularUtil.isUtil(tenant, telefone), is(true));
  }

}
