package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class AgenteTest {

	private Agente agente;

	@Test
	public void defaultAgendamento() {
		assertThat(agente.getAgendamento(),
				is(not(nullValue(Collection.class))));
		assertThat(agente.getAgendamento().isEmpty(), is(true));
	}

	@Test
	public void defaultCriacaoModificacao() {
		assertThat(agente.getCriacaoModificacao(),
				is(not(nullValue(CriacaoModificacao.class))));
	}

	@Before
	public void setUp() {
		agente = new Agente();
	}

}
