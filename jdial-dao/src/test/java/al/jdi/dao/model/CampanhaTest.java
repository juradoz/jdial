package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class CampanhaTest {

	private Campanha campanha;

	@Test
	public void defaultCriacaoModificacao() {
		assertThat(campanha.getCriacaoModificacao(),
				is(not(nullValue(CriacaoModificacao.class))));
	}

	@Test
	public void defaultDefinicao() {
		assertThat(campanha.getDefinicao(),
				is(not(nullValue(Collection.class))));
		assertThat(campanha.getDefinicao().isEmpty(), is(true));
	}

	@Test
	public void defaultFiltro() {
		assertThat(campanha.getFiltro(), is(not(nullValue(Collection.class))));
		assertThat(campanha.getFiltro().isEmpty(), is(true));
	}

	@Test
	public void defaultFiltroAtivo() {
		assertThat(campanha.isFiltroAtivo(), is(false));
	}

	@Test
	public void defaultMailing() {
		assertThat(campanha.getMailing(), is(not(nullValue(Collection.class))));
		assertThat(campanha.getMailing().isEmpty(), is(true));
	}

	@Before
	public void setUp() {
		campanha = new Campanha();
	}

}
