package al.jdi.core.filter;

import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.RestricaoHorario;
import al.jdi.dao.model.RestricaoHorario.DiaSemana;
import al.jdi.dao.model.RestricaoHorario.HoraFinal;
import al.jdi.dao.model.RestricaoHorario.HoraInicio;
import al.jdi.dao.model.Telefone;

class RestricaoHorarioUtil implements TelefoneUtil {

  private static final Logger logger = getLogger(RestricaoHorarioUtil.class);

  private final Provider<DaoFactory> daoFactoryProvider;

  private final Map<Integer, Method> inicios = new HashMap<Integer, Method>();
  private final Map<Integer, Method> finais = new HashMap<Integer, Method>();

  @Inject
  RestricaoHorarioUtil(Provider<DaoFactory> daoFactoryProvider) {
    this.daoFactoryProvider = daoFactoryProvider;
    registraMetodos();
  }

  private void registraMetodos() {
    try {
      for (Method method : RestricaoHorario.class.getMethods()) {
        if (!method.isAnnotationPresent(HoraInicio.class))
          continue;
        if (!method.isAnnotationPresent(DiaSemana.class))
          continue;
        DiaSemana annotation = method.getAnnotation(DiaSemana.class);
        inicios.put(annotation.dayOfWeek(), method);
      }

      for (Method method : RestricaoHorario.class.getMethods()) {
        if (!method.isAnnotationPresent(HoraFinal.class))
          continue;
        if (!method.isAnnotationPresent(DiaSemana.class))
          continue;
        DiaSemana annotation = method.getAnnotation(DiaSemana.class);
        finais.put(annotation.dayOfWeek(), method);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public boolean isUtil(Tenant tenant, Telefone telefone) {
    if (!tenant.getConfiguracoes().isBloqueiaDddPorPeriodo())
      return true;
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      RestricaoHorario restricaoHorario =
          daoFactory.getRestricaoHorarioDao().procura(telefone.getDdd());
      if (restricaoHorario == null)
        return true;

      DateTime agora = daoFactory.getDataBanco();

      DateTime horaInicio = getHoraInicio(agora, restricaoHorario);
      DateTime horaFinal = getHoraFinal(agora, restricaoHorario);

      return !(agora.isAfter(horaInicio) && agora.isBefore(horaFinal));
    } finally {
      daoFactory.close();
    }
  }

  private LocalTime getLocalTimeInicio(int dayOfWeek, RestricaoHorario restricaoHorario) {
    try {
      return (LocalTime) inicios.get(dayOfWeek).invoke(restricaoHorario);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  DateTime getHoraInicio(DateTime agora, RestricaoHorario restricaoHorario) {
    return agora.withFields(getLocalTimeInicio(agora.getDayOfWeek(), restricaoHorario));
  }

  private LocalTime getLocalTimeFinal(int dayOfWeek, RestricaoHorario restricaoHorario) {
    try {
      return (LocalTime) finais.get(dayOfWeek).invoke(restricaoHorario);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  DateTime getHoraFinal(DateTime agora, RestricaoHorario restricaoHorario) {
    return agora.withFields(getLocalTimeFinal(agora.getDayOfWeek(), restricaoHorario));
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }
}
