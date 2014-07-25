package al.jdi.core.configuracoes;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.slf4j.Logger;

class DefaultSistemaAtivo implements SistemaAtivo {

  static class DefaultSistemaAtivoFactory implements SistemaAtivo.Factory {

    @Override
    public SistemaAtivo create(DefaultConfiguracoes configuracoes) {
      return new DefaultSistemaAtivo(configuracoes);
    }
  }

  static final String SISTEMA_HORA_INICIO_SABADO = "sistema.horaInicioSabado";
  static final String SISTEMA_HORA_INICIO_SEXTA = "sistema.horaInicioSexta";
  static final String SISTEMA_HORA_INICIO_QUINTA = "sistema.horaInicioQuinta";
  static final String SISTEMA_HORA_INICIO_QUARTA = "sistema.horaInicioQuarta";
  static final String SISTEMA_HORA_INICIO_TERCA = "sistema.horaInicioTerca";
  static final String SISTEMA_HORA_INICIO_SEGUNDA = "sistema.horaInicioSegunda";
  static final String SISTEMA_HORA_INICIO_DOMINGO = "sistema.horaInicioDomingo";
  static final String SISTEMA_HORA_FINAL_SABADO = "sistema.horaFinalSabado";
  static final String SISTEMA_HORA_FINAL_SEXTA = "sistema.horaFinalSexta";
  static final String SISTEMA_HORA_FINAL_QUINTA = "sistema.horaFinalQuinta";
  static final String SISTEMA_HORA_FINAL_QUARTA = "sistema.horaFinalQuarta";
  static final String SISTEMA_HORA_FINAL_TERCA = "sistema.horaFinalTerca";
  static final String SISTEMA_HORA_FINAL_SEGUNDA = "sistema.horaFinalSegunda";
  static final String SISTEMA_HORA_FINAL_DOMINGO = "sistema.horaFinalDomingo";

  private static final Logger logger = getLogger(DefaultSistemaAtivo.class);

  private final DefaultConfiguracoes configuracoes;
  private final Map<Integer, String> constInicios = new HashMap<Integer, String>();
  private final Map<Integer, String> constTerminos = new HashMap<Integer, String>();

  DefaultSistemaAtivo(DefaultConfiguracoes configuracoes) {
    this.configuracoes = configuracoes;
    registraConstInicios();
    registraConstTerminos();
  }

  private void registraConstInicios() {
    constInicios.put(DateTimeConstants.MONDAY, SISTEMA_HORA_INICIO_SEGUNDA);
    constInicios.put(DateTimeConstants.TUESDAY, SISTEMA_HORA_INICIO_TERCA);
    constInicios.put(DateTimeConstants.WEDNESDAY, SISTEMA_HORA_INICIO_QUARTA);
    constInicios.put(DateTimeConstants.THURSDAY, SISTEMA_HORA_INICIO_QUINTA);
    constInicios.put(DateTimeConstants.FRIDAY, SISTEMA_HORA_INICIO_SEXTA);
    constInicios.put(DateTimeConstants.SATURDAY, SISTEMA_HORA_INICIO_SABADO);
    constInicios.put(DateTimeConstants.SUNDAY, SISTEMA_HORA_INICIO_DOMINGO);
  }

  private void registraConstTerminos() {
    constTerminos.put(DateTimeConstants.MONDAY, SISTEMA_HORA_FINAL_SEGUNDA);
    constTerminos.put(DateTimeConstants.TUESDAY, SISTEMA_HORA_FINAL_TERCA);
    constTerminos.put(DateTimeConstants.WEDNESDAY, SISTEMA_HORA_FINAL_QUARTA);
    constTerminos.put(DateTimeConstants.THURSDAY, SISTEMA_HORA_FINAL_QUINTA);
    constTerminos.put(DateTimeConstants.FRIDAY, SISTEMA_HORA_FINAL_SEXTA);
    constTerminos.put(DateTimeConstants.SATURDAY, SISTEMA_HORA_FINAL_SABADO);
    constTerminos.put(DateTimeConstants.SUNDAY, SISTEMA_HORA_FINAL_DOMINGO);
  }

  @Override
  public boolean isAtivo(DateTime agora) {
    if (!configuracoes.getDiscadorAtivo()) {
      logger.warn("Sistema inativo por solicitação da operação!");
      return false;
    }

    String constInicio = constInicios.get(agora.getDayOfWeek());
    String constTermino = constTerminos.get(agora.getDayOfWeek());

    String horaInicio = configuracoes.getString(constInicio, "00:00:00");
    String horaTermino = configuracoes.getString(constTermino, "00:00:00");

    LocalTime inicio = new LocalTime(horaInicio);
    LocalTime termino = new LocalTime(horaTermino);

    boolean antes = agora.isBefore(agora.withFields(inicio));
    boolean depois = agora.isAfter(agora.withFields(termino));

    return !antes && !depois;
  }
}
