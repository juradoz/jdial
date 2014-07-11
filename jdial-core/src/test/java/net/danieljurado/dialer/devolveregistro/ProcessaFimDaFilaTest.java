package net.danieljurado.dialer.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import net.danieljurado.dialer.modelo.Ligacao;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ProcessaFimDaFilaTest {

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

  private ProcessaFimDaFila processaFimDaFila;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(tratadorEspecificoCliente.obtemClienteDao(daoFactory)).thenReturn(clienteDao);
    processaFimDaFila = new ProcessaFimDaFila(tratadorEspecificoCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaFimDaFila.getOrdem(), is(11));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isVaiParaOFimDaFila()).thenReturn(true);
    assertThat(processaFimDaFila.accept(ligacao, cliente, resultadoLigacao, daoFactory), is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isVaiParaOFimDaFila()).thenReturn(false);
    assertThat(processaFimDaFila.accept(ligacao, cliente, resultadoLigacao, daoFactory), is(false));
  }

  @Test
  public void executaDeveriaFimDaFila() throws Exception {
    assertThat(processaFimDaFila.executa(ligacao, cliente, resultadoLigacao, daoFactory), is(true));
    verify(cliente).fimDaFila();
  }

  @Test
  public void executaDeveriaAtualizarCliente() throws Exception {
    assertThat(processaFimDaFila.executa(ligacao, cliente, resultadoLigacao, daoFactory), is(true));
    verify(clienteDao).atualiza(cliente);
  }

}
