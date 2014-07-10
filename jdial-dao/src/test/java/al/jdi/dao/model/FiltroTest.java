package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class FiltroTest {

	private Filtro filtro;

	@Test
	public void defaultCriacaoModificacao() {
		assertThat(filtro.getCriacaoModificacao(),
				is(not(nullValue(CriacaoModificacao.class))));
	}

	@Test
	public void defaultMailing() {
		new CollectionTest(filtro.getMailing()).assertNotNullAndEmpty();
	}

	@Before
	public void setUp() {
		filtro = new Filtro();
	}

}
