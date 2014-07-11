package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class MotivoFinalizacaoTest {

  private MotivoFinalizacao motivoFinalizacao;

  @Test
  public void defaultCriacaoModificacao() {
    assertThat(motivoFinalizacao.getCriacaoModificacao(),
        is(not(nullValue(CriacaoModificacao.class))));
  }

  @Before
  public void setUp() {
    motivoFinalizacao = new MotivoFinalizacao();
  }

}
