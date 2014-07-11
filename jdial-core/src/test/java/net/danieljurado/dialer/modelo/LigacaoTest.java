package net.danieljurado.dialer.modelo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class LigacaoTest {

  private static final String AGENTE = "AGENTE";

  @Mock
  private Discavel discavel;

  private Ligacao ligacao;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    ligacao = new Ligacao.Builder(discavel).build();
  }

  @Test
  public void isAtendidaDeveriaRetornarTrue() {
    ligacao.setAtendimento(new DateTime());
    assertThat(ligacao.isAtendida(), is(true));
  }

  @Test
  public void isAtendidaDeveriaRetornarFalse() {
    assertThat(ligacao.isAtendida(), is(false));
  }

  @Test
  public void isNoAgenteDeveriaRetornarTrue() throws Exception {
    ligacao.setAgente(AGENTE);
    assertThat(ligacao.isNoAgente(), is(true));
  }

  @Test
  public void isNoAgenteDeveriaRetornarFalse() throws Exception {
    assertThat(ligacao.isNoAgente(), is(false));
  }

}
