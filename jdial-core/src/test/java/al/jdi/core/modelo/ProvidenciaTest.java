package al.jdi.core.modelo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ProvidenciaTest {

  @Test
  public void fromValueDeveriaRetornarDefault() {
    assertThat(Providencia.Codigo.fromValue(Integer.MAX_VALUE),
        is(equalTo(Providencia.Codigo.MANTEM_ATUAL)));
  }

  @Test
  public void fromValueDeveriaRetornarINVALIDA_ATUAL_E_PROXIMO_TELEFONE() {
    assertThat(Providencia.Codigo.fromValue(Providencia.Codigo.INVALIDA_ATUAL_E_PROXIMO_TELEFONE
        .getCodigo()), is(equalTo(Providencia.Codigo.INVALIDA_ATUAL_E_PROXIMO_TELEFONE)));
  }

  @Test
  public void fromValueDeveriaRetornarMANTEM_ATUAL() {
    assertThat(Providencia.Codigo.fromValue(Providencia.Codigo.MANTEM_ATUAL.getCodigo()),
        is(equalTo(Providencia.Codigo.MANTEM_ATUAL)));
  }

  @Test
  public void fromValueDeveriaRetornarPROXIMO_TELEFONE() {
    assertThat(Providencia.Codigo.fromValue(Providencia.Codigo.PROXIMO_TELEFONE.getCodigo()),
        is(equalTo(Providencia.Codigo.PROXIMO_TELEFONE)));
  }

}
