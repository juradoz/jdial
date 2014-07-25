package al.jdi.core.modelo;

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

import javax.enterprise.inject.Instance;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.Providencia.ClienteSemTelefoneException;
import al.jdi.core.modelo.Providencia.SomenteCelularException;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

public class MantemAtualTest {

  private static final DateTime DATA_BANCO = new DateTime();

  private MantemAtual mantemAtual;

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
  private Instance<Providencia> iProximoTelefone;
  @Mock
  private Providencia proximoTelefone;
  @Mock
  private TelefoneFilter clienteSemTelefonesFilter;
  @Mock
  private TelefoneFilter somenteCelulareFilter;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Tenant tenant;

  private List<Telefone> telefones;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    telefones = new LinkedList<Telefone>(asList(telefone, telefone2));

    when(daoFactory.getDataBanco()).thenReturn(DATA_BANCO);
    when(daoFactory.getClienteDao()).thenReturn(clienteDao);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(cliente.getTelefones()).thenReturn(telefones);
    when(telefoneSorter.sort(tenant, telefones)).thenReturn(telefones);
    when(iProximoTelefone.get()).thenReturn(proximoTelefone);
    when(proximoTelefone.getTelefone(tenant, daoFactory, cliente)).thenReturn(telefone2);
    when(clienteSemTelefonesFilter.filter(tenant, telefones)).thenReturn(telefones);
    when(somenteCelulareFilter.filter(tenant, telefones)).thenReturn(telefones);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    mantemAtual =
        new MantemAtual(telefoneSorter, iProximoTelefone, clienteSemTelefonesFilter,
            somenteCelulareFilter);
  }

  @Test
  public void deveriaRetornarMesmoTelefone() throws Exception {
    assertThat(mantemAtual.getTelefone(tenant, daoFactory, cliente), is(sameInstance(telefone)));
  }

  @Test(expected = ClienteSemTelefoneException.class)
  public void deveriaLancarSemTelefonesException() throws Exception {
    telefones.clear();
    mantemAtual.getTelefone(tenant, daoFactory, cliente);
  }

  @Test(expected = SomenteCelularException.class)
  public void deveriaLancarSomenteCelularesException() throws Exception {
    when(somenteCelulareFilter.filter(tenant, telefones)).thenReturn(
        Collections.<Telefone>emptyList());
    mantemAtual.getTelefone(tenant, daoFactory, cliente);
  }

  @Test
  public void deveriaRetornarProximoTelefoneSeAtualNaoContido() throws Exception {
    telefones.remove(telefone);
    assertThat(mantemAtual.getTelefone(tenant, daoFactory, cliente), is(sameInstance(telefone2)));
  }

  @Test
  public void deveriaOrdenar() throws Exception {
    when(telefoneSorter.sort(tenant, telefones)).thenReturn(asList(telefone2));
    assertThat(mantemAtual.getTelefone(tenant, daoFactory, cliente), is(sameInstance(telefone2)));
  }

  @Test
  public void deveriaSetarHorarioSeTelNull() throws Exception {
    when(cliente.getTelefone()).thenReturn(null);
    mantemAtual.getTelefone(tenant, daoFactory, cliente);
    verify(cliente).setUltimoInicioRodadaTelefones(DATA_BANCO);
  }

  @Test
  public void deveriaAtualizarClienteSeTelNull() throws Exception {
    when(cliente.getTelefone()).thenReturn(null);
    mantemAtual.getTelefone(tenant, daoFactory, cliente);
    verify(clienteDao).atualiza(cliente);
  }

}
