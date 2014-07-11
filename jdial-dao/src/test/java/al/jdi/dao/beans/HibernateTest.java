package al.jdi.dao.beans;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

public class HibernateTest {

  private Configuration configure;

  @Before
  public void setUp() {
    configure = new Configuration().configure();
  }

  @Test
  public void testClearStartup() throws Exception {
    assertThat(configure, is(not(nullValue(Configuration.class))));
  }

}
