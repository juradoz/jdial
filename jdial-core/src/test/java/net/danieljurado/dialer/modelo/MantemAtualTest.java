package net.danieljurado.dialer.modelo;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.danieljurado.dialer.filter.TelefoneFilter;
import net.danieljurado.dialer.modelo.Providencia.ClienteSemTelefoneException;
import net.danieljurado.dialer.modelo.Providencia.SomenteCelularException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

public class MantemAtualTest {

  private static final DateTime DATA_BANCO = new DateTime();

  private MantemAtual mantemAtual;
  private List<Telefone> telefones;

  @Mock
  private TelefoneSorter telefoneSorter;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Cliente cliente;
  @Mock
  private Telefone telefone;
  @Mock
  private Telefone telefone2;
  @Mock
  private ClienteDao clienteDao;
  @Mock
  private ProximoTelefone proximoTelefone;
  @Mock
  private TelefoneFilter clienteSemTelefonesFilter;
  @Mock
  private TelefoneFilter somenteCelulareFilter;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    telefones = new LinkedList<Telefone>(asList(telefone, telefone2));

    when(daoFactory.getDataBanco()).thenReturn(DATA_BANCO);
    when(daoFactory.getClienteDao()).thenReturn(clienteDao);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(cliente.getTelefones()).thenReturn(telefones);
    when(telefoneSorter.sort(telefones)).thenReturn(telefones);
    when(proximoTelefone.getTelefone(daoFactory, cliente)).thenReturn(telefone2);
    when(clienteSemTelefonesFilter.filter(telefones)).thenReturn(telefones);
    when(somenteCelulareFilter.filter(telefones)).thenReturn(telefones);
    mantemAtual =
        new MantemAtual(telefoneSorter, proximoTelefone, clienteSemTelefonesFilter,
            somenteCelulareFilter);
  }

  @Test
  public void deveriaRetornarMesmoTelefone() throws Exception {
    assertThat(mantemAtual.getTelefone(daoFactory, cliente), is(sameInstance(telefone)));
  }

  @Test(expected = ClienteSemTelefoneException.class)
  public void deveriaLancarSemTelefonesException() throws Exception {
    telefones.clear();
    mantemAtual.getTelefone(daoFactory, cliente);
  }

  @Test(expected = SomenteCelularException.class)
  public void deveriaLancarSomenteCelularesException() throws Exception {
    when(somenteCelulareFilter.filter(telefones)).thenReturn(Collections.<Telefone>emptyList());
    mantemAtual.getTelefone(daoFactory, cliente);
  }

  @Test
  public void deveriaRetornarProximoTelefoneSeAtualNaoContido() throws Exception {
    telefones.remove(telefone);
    assertThat(mantemAtual.getTelefone(daoFactory, cliente), is(sameInstance(telefone2)));
  }

  @Test
  public void deveriaOrdenar() throws Exception {
    when(telefoneSorter.sort(telefones)).thenReturn(asList(telefone2));
    assertThat(mantemAtual.getTelefone(daoFactory, cliente), is(sameInstance(telefone2)));
  }

  @Test
  public void deveriaSetarHorarioSeTelNull() throws Exception {
    when(cliente.getTelefone()).thenReturn(null);
    mantemAtual.getTelefone(daoFactory, cliente);
    verify(cliente).setUltimoInicioRodadaTelefones(DATA_BANCO);
  }

  @Test
  public void deveriaAtualizarClienteSeTelNull() throws Exception {
    when(cliente.getTelefone()).thenReturn(null);
    mantemAtual.getTelefone(daoFactory, cliente);
    verify(clienteDao).atualiza(cliente);
  }

}
