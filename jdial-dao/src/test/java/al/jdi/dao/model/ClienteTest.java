package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ClienteTest {

  private Cliente cliente;

  @Mock
  private EstadoCliente estadoCliente;

  @Test
  public void defaultAgendamento() {
    assertThat(cliente.getAgendamento(), is(not(nullValue(Collection.class))));
    assertThat(cliente.getAgendamento().isEmpty(), is(true));
  }

  @Test
  public void defaultCriacaoModificacao() {
    assertThat(cliente.getCriacaoModificacao(), is(not(nullValue(CriacaoModificacao.class))));
  }

  @Test
  public void defaultDisponivelAPartirDe() {
    assertThat(cliente.getDisponivelAPartirDe(), is(not(nullValue(DateTime.class))));
  }

  @Test
  public void defaultFiltro() {
    assertThat(cliente.getFiltro(), is(equalTo(0)));
  }

  @Test
  public void defaultHistoricoCliente() {
    assertThat(cliente.getHistoricoCliente(), is(not(nullValue(Collection.class))));
    assertThat(cliente.getHistoricoCliente().isEmpty(), is(true));
  }

  @Test
  public void defaultTelefones() {
    assertThat(cliente.getTelefones(), is(not(nullValue(Collection.class))));
    assertThat(cliente.getTelefones().isEmpty(), is(true));
  }

  @Test
  public void defaultUltimaMudancaEstado() {
    assertThat(cliente.getUltimaMudancaEstado(), is(not(nullValue(DateTime.class))));
  }

  @Test
  public void fimDaFilaDeveriaSetarOrdemDaFila() {
    cliente.setOrdemDaFila(null);
    cliente.fimDaFila();
    assertThat(cliente.getOrdemDaFila(), is(not(nullValue(DateTime.class))));
  }

  @Test
  public void setEstadoClienteDeveriaAlterarUltimaMudancaEstado() {
    cliente.setUltimaMudancaEstado(null);
    cliente.setEstadoCliente(estadoCliente);
    assertThat(cliente.getUltimaMudancaEstado(), is(not(nullValue(DateTime.class))));
  }

  @Test
  public void setEstadoClienteNaoDeveriaAlterarUltimaMudancaEstado() {
    cliente.setEstadoCliente(estadoCliente);
    cliente.setUltimaMudancaEstado(null);
    cliente.setEstadoCliente(estadoCliente);
    assertThat(cliente.getUltimaMudancaEstado(), is(nullValue(DateTime.class)));
  }

  @Before
  public void setUp() {
    initMocks(this);
    cliente = new Cliente();
  }

}
