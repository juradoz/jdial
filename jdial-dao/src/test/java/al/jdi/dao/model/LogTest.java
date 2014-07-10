package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class LogTest {

	private Log log;

	@Test
	public void defaultCriacaoModificacao() {
		assertThat(log.getCriacaoModificacao(),
				is(not(nullValue(CriacaoModificacao.class))));
	}

	@Before
	public void setUp() {
		log = new Log();
	}

}
