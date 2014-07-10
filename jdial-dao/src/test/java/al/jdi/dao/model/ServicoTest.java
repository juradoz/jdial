package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ServicoTest {

	private Servico servico;

	@Test
	public void defaultCriacaoModificacao() {
		assertThat(servico.getCriacaoModificacao(),
				is(not(nullValue(CriacaoModificacao.class))));
	}

	@Test
	public void defaultMonitoravelQrf() {
		assertThat(servico.isMonitoravelQrf(), is(false));
	}

	@Before
	public void setUp() {
		servico = new Servico();
	}

}
