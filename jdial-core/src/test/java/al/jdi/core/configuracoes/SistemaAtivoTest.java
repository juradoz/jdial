package al.jdi.core.configuracoes;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.core.configuracoes.ConfiguracoesImpl;
import al.jdi.core.configuracoes.DefaultSistemaAtivo;
import al.jdi.core.configuracoes.SistemaAtivo;

@RunWith(Parameterized.class)
public class SistemaAtivoTest {

  private final static LocalTime INICIO = new DateTime().withTime(8, 0, 0, 0).toLocalTime();
  private final static LocalTime TERMINO = new DateTime().withTime(17, 59, 59, 0).toLocalTime();

  @Mock
  private ConfiguracoesImpl configuracoes;

  private Integer dayOfWeek;
  private SistemaAtivo sistemaAtivo;

  @Parameters
  public static Collection<Object[]> dayOfWeeks() {
    return asList(new Object[][] { {DateTimeConstants.MONDAY}, {DateTimeConstants.TUESDAY},
        {DateTimeConstants.WEDNESDAY}, {DateTimeConstants.THURSDAY}, {DateTimeConstants.FRIDAY},
        {DateTimeConstants.SATURDAY}, {DateTimeConstants.SUNDAY}});
  }

  public SistemaAtivoTest(int dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(configuracoes.getDiscadorAtivo()).thenReturn(true);

    when(configuracoes.getString(Mockito.anyString(), Mockito.anyString())).thenReturn(
        INICIO.toString(DateTimeFormat.forPattern("HH:mm:ss")),
        TERMINO.toString(DateTimeFormat.forPattern("HH:mm:ss")));

    sistemaAtivo = new DefaultSistemaAtivo(configuracoes);
  }

  @Test
  public void isAtivoDeveriaRetornarTrueInicio() {
    assertThat(sistemaAtivo.isAtivo(new DateTime().withDayOfWeek(dayOfWeek).withFields(INICIO)),
        is(true));
  }

  @Test
  public void isAtivoDeveriaRetornarFalseInicioConfiguracao() {
    when(configuracoes.getDiscadorAtivo()).thenReturn(false);
    assertThat(sistemaAtivo.isAtivo(new DateTime().withDayOfWeek(dayOfWeek).withFields(INICIO)),
        is(false));
  }

  @Test
  public void isAtivoDeveriaRetornarTrueFinal() {
    assertThat(sistemaAtivo.isAtivo(new DateTime().withDayOfWeek(dayOfWeek).withFields(TERMINO)),
        is(true));
  }

  @Test
  public void isAtivoDeveriaRetornarFalseFinalConfiguracao() {
    when(configuracoes.getDiscadorAtivo()).thenReturn(false);
    assertThat(sistemaAtivo.isAtivo(new DateTime().withDayOfWeek(dayOfWeek).withFields(TERMINO)),
        is(false));
  }

  @Test
  public void isAtivoDeveriaRetornarFalseAntesInicio() {
    assertThat(
        sistemaAtivo.isAtivo(new DateTime().withDayOfWeek(dayOfWeek).withFields(INICIO)
            .minusSeconds(1)), is(false));
  }

  @Test
  public void isAtivoDeveriaRetornarFalseDepoisFinal() {
    assertThat(
        sistemaAtivo.isAtivo(new DateTime().withDayOfWeek(dayOfWeek).withFields(TERMINO)
            .plusSeconds(1)), is(false));
  }

}
