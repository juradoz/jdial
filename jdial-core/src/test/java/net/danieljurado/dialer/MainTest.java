package net.danieljurado.dialer;

import org.junit.Test;

public class MainTest {

  private static final String CAMPANHA = "CAMPANHA";

  @Test
  public void test() {
    Guice.createInjector(new DialerModule(CAMPANHA));
  }

}
