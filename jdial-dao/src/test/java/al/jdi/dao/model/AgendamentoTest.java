package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class AgendamentoTest {

  private Agendamento agendamento;

  @Test
  public void defaultCriacao() {
    assertThat(agendamento.getCriacaoModificacao(),
        is(CoreMatchers.not(nullValue(CriacaoModificacao.class))));
  }

  @Before
  public void setUp() {
    agendamento = new Agendamento();
  }

}
