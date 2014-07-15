package al.jdi.core.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Telefone;

public class CampoUtilTest {

  private CampoUtil campoUtil;

  @Mock
  private Telefone telefone;
  @Mock
  private Configuracoes configuracoes;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(telefone.isUtil()).thenReturn(true);
    campoUtil = new CampoUtil();
  }

  @Test
  public void isUtilDeveriaRetornarTrue() throws Exception {
    assertThat(campoUtil.isUtil(configuracoes, telefone), is(true));
  }

  @Test
  public void isUtilDeveriaRetornarFalse() throws Exception {
    when(telefone.isUtil()).thenReturn(false);
    assertThat(campoUtil.isUtil(configuracoes, telefone), is(false));
  }

}
