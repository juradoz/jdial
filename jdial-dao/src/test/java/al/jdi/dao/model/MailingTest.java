package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class MailingTest {

	private Mailing mailing;

	@Test
	public void defaultCriacaoModificacao() {
		assertThat(mailing.getCriacaoModificacao(),
				is(not(nullValue(CriacaoModificacao.class))));
	}

	@Test
	public void defaultFiltro() {
		new CollectionTest(mailing.getFiltro()).assertNotNullAndEmpty();
	}

	@Before
	public void setUp() {
		mailing = new Mailing();
	}

}
