package al.jdi.web.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Specializes;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.jodatime.DateTimeConverter;

@SuppressWarnings("deprecation")
@Specializes
@Convert(DateTime.class)
@RequestScoped
public class MyDateTimeConverter extends DateTimeConverter {

  @Override
  protected DateTimeFormatter getFormatter() {
    return DateTimeFormat.forPattern("dd/MM/yyyy");
  }

}
