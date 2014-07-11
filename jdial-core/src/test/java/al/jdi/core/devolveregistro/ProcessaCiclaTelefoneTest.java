package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.devolveregistro.ProcessaCiclaTelefone;
import al.jdi.core.devolveregistro.ProcessaFimDaFila;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.modelo.Providencia;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.InformacaoCliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public class ProcessaCiclaTelefoneTest {

  @Mock
  private TratadorEspecificoCliente tratadorEspecificoCliente;
  @Mock
  private ProcessaFimDaFila processaFimDaFila;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Telefone telefone;
  @Mock
  private InformacaoCliente informacaoCliente;
  @Mock
  private Map<Providencia.Codigo, Providencia> providencias;

  private ProcessaCiclaTelefone processaCiclaTelefone;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(cliente.getInformacaoCliente()).thenReturn(informacaoCliente);
    processaCiclaTelefone =
        new ProcessaCiclaTelefone(tratadorEspecificoCliente, processaFimDaFila, providencias);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaCiclaTelefone.getOrdem(), is(9));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isCiclaTelefone()).thenReturn(true);
    assertThat(processaCiclaTelefone.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isCiclaTelefone()).thenReturn(false);
    assertThat(processaCiclaTelefone.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void acceptDeveriaSetarTelOriginal() throws Exception {
    assertThat(processaCiclaTelefone.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
    verify(ligacao).setTelefoneOriginal(telefone);
  }

  // TODO : Extrair impl de Providencia para classes regulares

}
