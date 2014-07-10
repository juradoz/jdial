package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class GrupoTest {

	private Grupo grupo;

	@Test
	public void defaultCriacaoModificacao() {
		assertThat(grupo.getCriacaoModificacao(),
				is(not(nullValue(CriacaoModificacao.class))));
	}

	@Test
	public void defaultSemAgentes() {
		assertThat(grupo.isSemAgentes(), is(false));
	}

	@Test
	public void defaultVisivelOperador() {
		assertThat(grupo.isVisivelOperador(), is(true));
	}

	@Before
	public void setUp() {
		grupo = new Grupo();
	}

}
