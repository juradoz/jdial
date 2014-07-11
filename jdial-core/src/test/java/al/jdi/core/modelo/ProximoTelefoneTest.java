package al.jdi.core.modelo;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.MantemAtual;
import al.jdi.core.modelo.ProximoTelefone;
import al.jdi.core.modelo.TelefoneSorter;
import al.jdi.core.modelo.Providencia.ClienteSemTelefoneException;
import al.jdi.core.modelo.Providencia.NaoPodeReiniciarRodadaTelefoneException;
import al.jdi.core.modelo.Providencia.SemProximoTelefoneException;
import al.jdi.core.modelo.Providencia.SomenteCelularException;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

public class ProximoTelefoneTest {

  private static final Period INTERVALO_RODADA_TELEFONE = Period.hours(1);
  private static final DateTime DATA_BANCO = new DateTime();
  private ProximoTelefone proximoTelefone;
  private List<Telefone> telefones;

  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Cliente cliente;
  @Mock
  private Telefone telefone1;
  @Mock
  private Telefone telefone2;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private TelefoneSorter telefoneSorter;
  @Mock
  private MantemAtual mantemAtual;
  @Mock
  private ClienteDao clienteDao;
  @Mock
  private TelefoneFilter clienteSemTelefonesFilter;
  @Mock
  private TelefoneFilter somenteCelularesFilter;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    telefones = new LinkedList<Telefone>(asList(telefone1, telefone2));

    when(mantemAtual.getTelefone(daoFactory, cliente)).thenReturn(telefone1);

    when(cliente.getTelefone()).thenReturn(telefone1);
    when(cliente.getTelefones()).thenReturn(telefones);
    when(cliente.getUltimoInicioRodadaTelefones()).thenReturn(DATA_BANCO.minusHours(1));

    when(configuracoes.getIntervaloMinimoNovaRodadaTelefone())
        .thenReturn(INTERVALO_RODADA_TELEFONE);

    when(daoFactory.getDataBanco()).thenReturn(DATA_BANCO);
    when(daoFactory.getClienteDao()).thenReturn(clienteDao);

    when(telefoneSorter.sort(telefones)).thenReturn(telefones);

    when(clienteSemTelefonesFilter.filter(telefones)).thenReturn(telefones);
    when(somenteCelularesFilter.filter(telefones)).thenReturn(telefones);

    proximoTelefone =
        new ProximoTelefone(configuracoes, telefoneSorter, mantemAtual, clienteSemTelefonesFilter,
            somenteCelularesFilter);
  }

  @Test
  public void deveriaRetornarProximo() throws Exception {
    assertThat(proximoTelefone.getTelefone(daoFactory, cliente), is(sameInstance(telefone2)));
  }

  @Test
  public void deveriaRetornarAtual() throws Exception {
    when(cliente.getTelefone()).thenReturn(null);
    assertThat(proximoTelefone.getTelefone(daoFactory, cliente), is(sameInstance(telefone1)));
  }

  @Test(expected = NaoPodeReiniciarRodadaTelefoneException.class)
  public void deveriaLancarNaoPodeReiniciarRodadaTelefoneException() throws Exception {
    when(cliente.getTelefone()).thenReturn(telefone2);
    when(cliente.getUltimoInicioRodadaTelefones()).thenReturn(
        DATA_BANCO.minus(INTERVALO_RODADA_TELEFONE).plusMinutes(1));
    proximoTelefone.getTelefone(daoFactory, cliente);
  }

  @Test
  public void deveriaRetornarCiclarDeVoltaPrimeiroTelefone() throws Exception {
    when(cliente.getTelefone()).thenReturn(telefone2);
    assertThat(proximoTelefone.getTelefone(daoFactory, cliente), is(sameInstance(telefone1)));
  }

  @Test(expected = SemProximoTelefoneException.class)
  public void deveriaLancarSemProximoSeSomente1Telefone() throws Exception {
    List<Telefone> list = asList(telefone1);
    when(clienteSemTelefonesFilter.filter(telefones)).thenReturn(new LinkedList<Telefone>(list));
    when(somenteCelularesFilter.filter(list)).thenReturn(list);
    when(telefoneSorter.sort(list)).thenReturn(list);
    proximoTelefone.getTelefone(daoFactory, cliente);
  }

  @Test
  public void deveriaNaoAtualizarDataRodadaSePassarProximo() throws Exception {
    assertThat(proximoTelefone.getTelefone(daoFactory, cliente), is(sameInstance(telefone2)));
    verify(cliente, never()).setUltimoInicioRodadaTelefones(DATA_BANCO);
  }

  @Test
  public void deveriaSetarInicioRodadaCiclarDeVoltaPrimeiroTelefone() throws Exception {
    when(cliente.getTelefone()).thenReturn(telefone2);
    assertThat(proximoTelefone.getTelefone(daoFactory, cliente), is(sameInstance(telefone1)));
    verify(cliente).setUltimoInicioRodadaTelefones(DATA_BANCO);
  }

  @Test
  public void deveriaAtualizarInicioRodadaSeDeVoltaPrimeiroTelefone() throws Exception {
    when(cliente.getTelefone()).thenReturn(telefone2);
    assertThat(proximoTelefone.getTelefone(daoFactory, cliente), is(sameInstance(telefone1)));
    verify(cliente).setUltimoInicioRodadaTelefones(DATA_BANCO);
  }

  @Test(expected = ClienteSemTelefoneException.class)
  public void deveriaLancarSemTelefonesException() throws Exception {
    telefones.clear();
    proximoTelefone.getTelefone(daoFactory, cliente);
  }

  @Test(expected = SomenteCelularException.class)
  public void deveriaLancarSomenteCelularesException() throws Exception {
    when(somenteCelularesFilter.filter(telefones)).thenReturn(Collections.<Telefone>emptyList());
    proximoTelefone.getTelefone(daoFactory, cliente);
  }

  @Test
  public void deveriaOrdenar() throws Exception {
    assertThat(proximoTelefone.getTelefone(daoFactory, cliente), is(sameInstance(telefone2)));
    verify(telefoneSorter).sort(telefones);
  }

}
