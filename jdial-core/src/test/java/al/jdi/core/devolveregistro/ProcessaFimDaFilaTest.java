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

public class ProcessaFimDaFilaTest {

  private ProcessaFimDaFila processaFimDaFila;

  @Mock
  private TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
  @Mock
  private TratadorEspecificoCliente tratadorEspecificoCliente;
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
  private Configuracoes configuracoes;
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
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    processaFimDaFila = new ProcessaFimDaFila(tratadorEspecificoClienteFactory);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaFimDaFila.getOrdem(), is(11));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isVaiParaOFimDaFila()).thenReturn(true);
    assertThat(processaFimDaFila.accept(tenant, ligacao, resultadoLigacao, daoFactory), is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isVaiParaOFimDaFila()).thenReturn(false);
    assertThat(processaFimDaFila.accept(tenant, ligacao, resultadoLigacao, daoFactory), is(false));
  }

  @Test
  public void executaDeveriaFimDaFila() throws Exception {
    assertThat(processaFimDaFila.executa(tenant, ligacao, resultadoLigacao, daoFactory), is(true));
    verify(cliente).fimDaFila();
  }

  @Test
  public void executaDeveriaAtualizarCliente() throws Exception {
    assertThat(processaFimDaFila.executa(tenant, ligacao, resultadoLigacao, daoFactory), is(true));
    verify(clienteDao).atualiza(cliente);
  }

}
