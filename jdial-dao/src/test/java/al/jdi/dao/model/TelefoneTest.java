package al.jdi.dao.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class TelefoneTest {

  private Telefone telefone;

  @Test
  public void defaultCriacaoModificacao() {
    assertThat(telefone.getCriacaoModificacao(), is(not(nullValue(CriacaoModificacao.class))));
  }

  @Test
  public void defaultHistoricoLigacao() {
    new CollectionTest(telefone.getHistoricoLigacao()).assertNotNullAndEmpty();
  }

  @Test
  public void defaultTentativa() {
    assertThat(telefone.getTentativa(), is(equalTo(0)));
  }

  @Test
  public void defaultUtil() {
    assertThat(telefone.isUtil(), is(true));
  }

  @Test
  public void isCelularDeveriaRetornarFalseSeTelefoneBlank() {
    telefone.setTelefone(EMPTY);
    assertThat(telefone.isCelular(), is(false));
  }

  @Test
  public void isCelularDeveriaRetornarFalseSeTelefoneNaoComecarComDigitosCel() {
    telefone.setTelefone("1123123123");
    assertThat(telefone.isCelular(), is(false));
    telefone.setTelefone("2123123123");
    assertThat(telefone.isCelular(), is(false));
    telefone.setTelefone("3123123123");
    assertThat(telefone.isCelular(), is(false));
    telefone.setTelefone("4123123123");
    assertThat(telefone.isCelular(), is(false));
    telefone.setTelefone("5123123123");
    assertThat(telefone.isCelular(), is(false));
  }

  @Test
  public void isCelularDeveriaRetornarFalseSeTelefoneNull() {
    assertThat(telefone.isCelular(), is(false));
  }

  @Test
  public void isCelularDeveriaRetornarTrueSeTelefoneComecarComDigitosCel() {
    telefone.setTelefone("9123123123");
    assertThat(telefone.isCelular(), is(true));
    telefone.setTelefone("8123123123");
    assertThat(telefone.isCelular(), is(true));
    telefone.setTelefone("7123123123");
    assertThat(telefone.isCelular(), is(true));
    telefone.setTelefone("8123123123");
    assertThat(telefone.isCelular(), is(true));
  }

  @Before
  public void setUp() {
    telefone = new Telefone();
  }

}
