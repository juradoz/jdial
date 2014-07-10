package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class CriacaoModificacaoTest {

	private CriacaoModificacao criacaoModificacao;

	@Test
	public void defaultCriacao() {
		assertThat(criacaoModificacao.getCriacao(),
				is(notNullValue(DateTime.class)));
	}

	@Test
	public void defaultModificacao() {
		assertThat(criacaoModificacao.getModificacao(),
				is(notNullValue(DateTime.class)));
	}

	@Before
	public void setUp() {
		criacaoModificacao = new CriacaoModificacao();
	}

}
