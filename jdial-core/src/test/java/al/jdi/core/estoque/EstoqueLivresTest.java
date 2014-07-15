package al.jdi.core.estoque;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Provider;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.devolveregistro.DevolveRegistro;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Providencia;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;

public class EstoqueLivresTest {

  private EstoqueImpl estoqueLivres;

  @Mock
  private Registro registro;
  @Mock
  private Cliente cliente;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Provider<DaoFactory> daoFactoryProvider;
  @Mock
  private DevolveRegistro devolveRegistro;
  @Mock
  private TratadorEspecificoCliente tratadorEspecificoCliente;
  @Mock
  private TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
  @Mock
  private Discavel.Factory discavelFactory;
  @Mock
  private Engine.Factory engineFactory;
  @Mock
  private ExtraidorClientes extraidorClientes;
  @Mock
  private Map<Providencia.Codigo, Providencia> providencias;
  @Mock
  private TelefoneFilter telefoneFilter;
  @Mock
  private Logger logger;
  @Mock
  private DaoFactory daoFactory;

  private Period intervaloMonitoracao = Period.ZERO;
  private Collection<Registro> estoque;


  @Test
  public void contemClienteDeveriaEncontrar() {
    assertThat(estoqueLivres.contemCliente(cliente), is(true));
  }

  @Test
  public void contemClienteNaoDeveriaEncontrar() {
    Cliente cliente = mock(Cliente.class);
    assertThat(estoqueLivres.contemCliente(cliente), is(false));
  }

  @Test
  public void obtemRegistrosDeveriaRetornar0SeQuantidade0() {
    assertThat(estoqueLivres.obtemRegistros(0).isEmpty(), is(true));
  }

  @Test
  public void obtemRegistrosDeveriaRetornar2Registros() {
    Collection<Cliente> registros = estoqueLivres.obtemRegistros(2);
    assertThat(registros.size(), is(equalTo(2)));
    assertThat(registros.contains(cliente), is(true));
  }

  @Test
  public void obtemRegistrosDeveriaRetornarRegistro() {
    Collection<Cliente> registros = estoqueLivres.obtemRegistros(1);
    assertThat(registros.size(), is(equalTo(1)));
    assertThat(registros.contains(cliente), is(true));
  }

  @Before
  public void setUp() {
    initMocks(this);
    when(registro.getCliente()).thenReturn(cliente);
    when(tratadorEspecificoClienteFactory.create(configuracoes, daoFactory)).thenReturn(
        tratadorEspecificoCliente);
    estoque = new LinkedList<Registro>(asList(registro, registro));
    estoqueLivres =
        new EstoqueImpl(logger, configuracoes, daoFactoryProvider, devolveRegistro,
            tratadorEspecificoClienteFactory, discavelFactory, engineFactory, estoque,
            extraidorClientes, intervaloMonitoracao, providencias, telefoneFilter);
  }

}
