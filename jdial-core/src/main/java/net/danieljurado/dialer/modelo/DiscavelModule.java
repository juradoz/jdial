package net.danieljurado.dialer.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.danieljurado.dialer.modelo.ModeloModule.DiscavelTsa;

class DiscavelModule {

  private static final String KEY_DISCAVEL_FACTORY = "discavelFactory";

  @SuppressWarnings("unchecked")
  protected void configure() {
    Properties properties = new Properties();
    InputStream stream = getClass().getClassLoader().getResourceAsStream("dialer.properties");
    if (stream != null)
      try {
        properties.load(stream);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    Names.bindProperties(binder(), properties);

    Class<? extends Discavel.Factory> discavelFactory = DiscavelFactoryTsaImpl.class;
    if (properties.containsKey(KEY_DISCAVEL_FACTORY))
      try {
        Class<?> clazz = Class.forName(properties.getProperty(KEY_DISCAVEL_FACTORY));
        if (Discavel.Factory.class.isAssignableFrom(clazz))
          discavelFactory = (Class<? extends Discavel.Factory>) clazz;
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

    bind(Discavel.Factory.class).annotatedWith(DiscavelTsa.class).to(discavelFactory);
  }

}
