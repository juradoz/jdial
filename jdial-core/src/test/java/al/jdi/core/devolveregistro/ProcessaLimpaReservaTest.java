package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaLimpaReservaTest {

  private static final Integer OPERADOR = 3;
  private static final String NOME_BASE_DADOS = "BASE";

  private ProcessaLimpaReserva processaLimpaReserva;

  @Mock
  private TratadorEspecificoCliente tratadorEspecificoCliente;
  @Mock
  private TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private ClienteDao clienteDao;
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(tratadorEspecificoClienteFactory.create(tenant, daoFactory)).thenReturn(
        tratadorEspecificoCliente);
    when(tratadorEspecificoCliente.obtemClienteDao()).thenReturn(clienteDao);
    when(configuracoes.getOperador()).thenReturn(OPERADOR);
    when(configuracoes.getNomeBaseDados()).thenReturn(NOME_BASE_DADOS);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);

    processaLimpaReserva = new ProcessaLimpaReserva(tratadorEspecificoClienteFactory);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaLimpaReserva.getOrdem(), is(12));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    assertThat(processaLimpaReserva.accept(tenant, ligacao, resultadoLigacao, daoFactory), is(true));

  }

  @Test
  public void executaDeveriaLimpar() throws Exception {
    processaLimpaReserva.executa(tenant, ligacao, resultadoLigacao, daoFactory);
    verify(clienteDao).limpaReserva(cliente, OPERADOR, NOME_BASE_DADOS);
  }

  @Test
  public void executaDeveriaRetornarTrue() throws Exception {
    assertThat(processaLimpaReserva.executa(tenant, ligacao, resultadoLigacao, daoFactory),
        is(true));
  }

}
