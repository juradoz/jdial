package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ResultadoLigacaoTest {

  private ResultadoLigacao resultadoLigacao;

  @Test
  public void defaultCriacaoModificacao() {
    assertThat(resultadoLigacao.getCriacaoModificacao(),
        is(not(nullValue(CriacaoModificacao.class))));
  }

  @Test
  public void defaultMotivo() {
    assertThat(resultadoLigacao.getMotivo(), is(equalTo(0)));
  }

  @Test
  public void defaultMotivoFinalizacaoPorQuantidadeResultado() {
    assertThat(resultadoLigacao.getMotivoFinalizacaoPorQuantidadeResultado(), is(equalTo(0)));
  }

  // @Test
  public void defaultVisivelRelatorio() {
    assertThat(resultadoLigacao.isVisivelRelatorio(), is(true));
  }

  @Before
  public void setUp() {
    resultadoLigacao = new ResultadoLigacao();
  }

  @Test
  public void defaultLimpaAgendados() {
    assertThat(resultadoLigacao.isLimpaAgendamentos(), is(false));
  }

}
