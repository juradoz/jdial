package al.jdi.core.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.inject.Provider;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.RestricaoHorario;
import al.jdi.dao.model.Telefone;

public class RestricaoHorarioUtilTest {

  private static final String DDD = "DDD";

  private static final DateTime DATA_SEGUNDA = new DateTime()
      .withDayOfWeek(DateTimeConstants.MONDAY);
  private static final DateTime INICIO_SEGUNDA = DATA_SEGUNDA.withTime(2, 0, 0, 0);
  private static final DateTime FINAL_SEGUNDA = DATA_SEGUNDA.withTime(2, 59, 59, 0);

  private static final DateTime DATA_TERCA = new DateTime()
      .withDayOfWeek(DateTimeConstants.TUESDAY);
  private static final DateTime INICIO_TERCA = DATA_TERCA.withTime(3, 0, 0, 0);
  private static final DateTime FINAL_TERCA = DATA_TERCA.withTime(3, 59, 59, 0);

  private static final DateTime DATA_QUARTA = new DateTime()
      .withDayOfWeek(DateTimeConstants.WEDNESDAY);
  private static final DateTime INICIO_QUARTA = DATA_QUARTA.withTime(4, 0, 0, 0);
  private static final DateTime FINAL_QUARTA = DATA_QUARTA.withTime(4, 59, 59, 0);

  private static final DateTime DATA_QUINTA = new DateTime()
      .withDayOfWeek(DateTimeConstants.THURSDAY);
  private static final DateTime INICIO_QUINTA = DATA_QUINTA.withTime(5, 0, 0, 0);
  private static final DateTime FINAL_QUINTA = DATA_QUINTA.withTime(5, 59, 59, 0);

  private static final DateTime DATA_SEXTA = new DateTime().withDayOfWeek(DateTimeConstants.FRIDAY);
  private static final DateTime INICIO_SEXTA = DATA_SEXTA.withTime(6, 0, 0, 0);
  private static final DateTime FINAL_SEXTA = DATA_SEXTA.withTime(6, 59, 59, 0);

  private static final DateTime DATA_SABADO = new DateTime()
      .withDayOfWeek(DateTimeConstants.SATURDAY);
  private static final DateTime INICIO_SABADO = DATA_SABADO.withTime(7, 0, 0, 0);
  private static final DateTime FINAL_SABADO = DATA_SABADO.withTime(7, 59, 59, 0);

  private static final DateTime DATA_DOMINGO = new DateTime()
      .withDayOfWeek(DateTimeConstants.SUNDAY);
  private static final DateTime INICIO_DOMINGO = DATA_DOMINGO.withTime(8, 0, 0, 0);
  private static final DateTime FINAL_DOMINGO = DATA_DOMINGO.withTime(8, 59, 59, 0);

  private static final DateTime DATA_BANCO = new DateTime();

  private RestricaoHorarioUtil restricaoHorarioUtil;

  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Provider<DaoFactory> daoFactoryProvider;
  @Mock
  private Telefone telefone;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Dao<RestricaoHorario> restricaoHorarioDao;
  @Mock
  private RestricaoHorario restricaoHorario;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(configuracoes.isBloqueiaDddPorPeriodo()).thenReturn(true);

    when(telefone.getDdd()).thenReturn(DDD);

    when(daoFactoryProvider.get()).thenReturn(daoFactory);
    when(daoFactory.getRestricaoHorarioDao()).thenReturn(restricaoHorarioDao);
    when(restricaoHorarioDao.procura(DDD)).thenReturn(restricaoHorario);

    when(restricaoHorario.getHoraInicioSegunda()).thenReturn(INICIO_SEGUNDA.toLocalTime());
    when(restricaoHorario.getHoraFinalSegunda()).thenReturn(FINAL_SEGUNDA.toLocalTime());
    when(restricaoHorario.getHoraInicioTerca()).thenReturn(INICIO_TERCA.toLocalTime());
    when(restricaoHorario.getHoraFinalTerca()).thenReturn(FINAL_TERCA.toLocalTime());
    when(restricaoHorario.getHoraInicioQuarta()).thenReturn(INICIO_QUARTA.toLocalTime());
    when(restricaoHorario.getHoraFinalQuarta()).thenReturn(FINAL_QUARTA.toLocalTime());
    when(restricaoHorario.getHoraInicioQuinta()).thenReturn(INICIO_QUINTA.toLocalTime());
    when(restricaoHorario.getHoraFinalQuinta()).thenReturn(FINAL_QUINTA.toLocalTime());
    when(restricaoHorario.getHoraInicioSexta()).thenReturn(INICIO_SEXTA.toLocalTime());
    when(restricaoHorario.getHoraFinalSexta()).thenReturn(FINAL_SEXTA.toLocalTime());
    when(restricaoHorario.getHoraInicioSabado()).thenReturn(INICIO_SABADO.toLocalTime());
    when(restricaoHorario.getHoraFinalSabado()).thenReturn(FINAL_SABADO.toLocalTime());
    when(restricaoHorario.getHoraInicioDomingo()).thenReturn(INICIO_DOMINGO.toLocalTime());
    when(restricaoHorario.getHoraFinalDomingo()).thenReturn(FINAL_DOMINGO.toLocalTime());

    when(daoFactory.getDataBanco()).thenReturn(DATA_BANCO);

    restricaoHorarioUtil = new RestricaoHorarioUtil(daoFactoryProvider);
  }

  @Test
  public void isUtilDeveriaRetornarTrueConfig() throws Exception {
    when(configuracoes.isBloqueiaDddPorPeriodo()).thenReturn(false);
    assertThat(restricaoHorarioUtil.isUtil(configuracoes, telefone), is(true));
  }

  @Test
  public void isUtilDeveriaRetornarTrueDddForaDaLista() throws Exception {
    when(restricaoHorarioDao.procura(DDD)).thenReturn(null);
    assertThat(restricaoHorarioUtil.isUtil(configuracoes, telefone), is(true));
  }

  @Test
  public void isUtilDeveriaRetornarTrueDddForaDoHorario() throws Exception {
    when(daoFactory.getDataBanco()).thenReturn(DATA_SEGUNDA);
    when(restricaoHorario.getHoraInicioSegunda()).thenReturn(
        DATA_SEGUNDA.plusMinutes(1).toLocalTime());
    when(restricaoHorario.getHoraFinalSegunda()).thenReturn(
        DATA_SEGUNDA.plusMinutes(2).toLocalTime());
    assertThat(restricaoHorarioUtil.isUtil(configuracoes, telefone), is(true));
  }

  @Test
  public void isUtilDeveriaRetornarTrueSeInicioNull() throws Exception {
    when(daoFactory.getDataBanco()).thenReturn(DATA_SEGUNDA);
    when(restricaoHorario.getHoraInicioSegunda()).thenReturn(null);
    when(restricaoHorario.getHoraFinalSegunda()).thenReturn(
        DATA_SEGUNDA.plusMinutes(1).toLocalTime());
    assertThat(restricaoHorarioUtil.isUtil(configuracoes, telefone), is(true));
  }

  @Test
  public void isUtilDeveriaRetornarTrueSeFinalNull() throws Exception {
    when(daoFactory.getDataBanco()).thenReturn(DATA_SEGUNDA);
    when(restricaoHorario.getHoraInicioSegunda()).thenReturn(
        DATA_SEGUNDA.minusHours(1).toLocalTime());
    when(restricaoHorario.getHoraFinalSegunda()).thenReturn(null);
    assertThat(restricaoHorarioUtil.isUtil(configuracoes, telefone), is(true));
  }

  @Test
  public void isUtilDeveriaRetornarFalseDddDentroDoHorario() throws Exception {
    when(daoFactory.getDataBanco()).thenReturn(DATA_SEGUNDA);
    when(restricaoHorario.getHoraInicioSegunda()).thenReturn(
        DATA_SEGUNDA.minusMinutes(1).toLocalTime());
    when(restricaoHorario.getHoraFinalSegunda()).thenReturn(
        DATA_SEGUNDA.plusMinutes(1).toLocalTime());
    assertThat(restricaoHorarioUtil.isUtil(configuracoes, telefone), is(false));
  }

  @Test
  public void getHoraInicioPelaDataDeveriaRetornarSegunda() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraInicio(DATA_SEGUNDA, restricaoHorario),
        is(INICIO_SEGUNDA));
  }

  @Test
  public void getHoraFinalPelaDataDeveriaRetornarSegunda() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraFinal(DATA_SEGUNDA, restricaoHorario), is(FINAL_SEGUNDA));
  }

  @Test
  public void getHoraInicioPelaDataDeveriaRetornarTerca() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraInicio(DATA_TERCA, restricaoHorario), is(INICIO_TERCA));
  }

  @Test
  public void getHoraFinalPelaDataDeveriaRetornarTerca() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraFinal(DATA_TERCA, restricaoHorario), is(FINAL_TERCA));
  }

  @Test
  public void getHoraInicioPelaDataDeveriaRetornarQuarta() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraInicio(DATA_QUARTA, restricaoHorario), is(INICIO_QUARTA));
  }

  @Test
  public void getHoraFinalPelaDataDeveriaRetornarQuarta() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraFinal(DATA_QUARTA, restricaoHorario), is(FINAL_QUARTA));
  }

  @Test
  public void getHoraInicioPelaDataDeveriaRetornarQuinta() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraInicio(DATA_QUINTA, restricaoHorario), is(INICIO_QUINTA));
  }

  @Test
  public void getHoraFinalPelaDataDeveriaRetornarQuinta() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraFinal(DATA_QUINTA, restricaoHorario), is(FINAL_QUINTA));
  }

  @Test
  public void getHoraInicioPelaDataDeveriaRetornarSexta() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraInicio(DATA_SEXTA, restricaoHorario), is(INICIO_SEXTA));
  }

  @Test
  public void getHoraFinalPelaDataDeveriaRetornarSexta() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraFinal(DATA_SEXTA, restricaoHorario), is(FINAL_SEXTA));
  }

  @Test
  public void getHoraInicioPelaDataDeveriaRetornarSabado() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraInicio(DATA_SABADO, restricaoHorario), is(INICIO_SABADO));
  }

  @Test
  public void getHoraFinalPelaDataDeveriaRetornarSabado() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraFinal(DATA_SABADO, restricaoHorario), is(FINAL_SABADO));
  }

  @Test
  public void getHoraInicioPelaDataDeveriaRetornarDomingo() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraInicio(DATA_DOMINGO, restricaoHorario),
        is(INICIO_DOMINGO));
  }

  @Test
  public void getHoraFinalPelaDataDeveriaRetornarDomingo() throws Exception {
    assertThat(restricaoHorarioUtil.getHoraFinal(DATA_DOMINGO, restricaoHorario), is(FINAL_DOMINGO));
  }

}
