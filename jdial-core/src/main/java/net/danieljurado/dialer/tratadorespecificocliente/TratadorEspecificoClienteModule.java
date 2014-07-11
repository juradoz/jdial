package net.danieljurado.dialer.tratadorespecificocliente;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TratadorEspecificoClienteModule {

  private static final String KEY_TRATADOR_ESPECIFICO = "tratadorEspecifico";

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

    Class<? extends TratadorEspecificoCliente> tratadorEspecifico =
        TratadorEspecificoClienteTsaImpl.class;
    if (properties.containsKey(KEY_TRATADOR_ESPECIFICO))
      try {
        Class<?> clazz = Class.forName(properties.getProperty(KEY_TRATADOR_ESPECIFICO));
        if (TratadorEspecificoCliente.class.isAssignableFrom(clazz))
          tratadorEspecifico = (Class<? extends TratadorEspecificoCliente>) clazz;
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

    bind(TratadorEspecificoCliente.class).to(tratadorEspecifico);
  }

}
