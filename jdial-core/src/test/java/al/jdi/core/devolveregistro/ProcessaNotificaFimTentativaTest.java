package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public class ProcessaNotificaFimTentativaTest {

  private ProcessaNotificaFimTentativa processaNotificaFimTentativa;

  private static final boolean INUTILIZA_MOTIVO_DIFERENCIADO = false;
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
  private Campanha campanha;
  @Mock
  private Telefone telefone;
  @Mock
  private Mailing mailing;
  @Mock
  private Logger logger;

  private DateTime dataBanco;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    dataBanco = new DateTime();
    when(cliente.getMailing()).thenReturn(mailing);
    when(mailing.getCampanha()).thenReturn(campanha);
    when(daoFactory.getDataBanco()).thenReturn(dataBanco);
    when(ligacao.getTelefoneOriginal()).thenReturn(telefone);
    when(ligacao.isInutilizaComMotivoDiferenciado()).thenReturn(INUTILIZA_MOTIVO_DIFERENCIADO);
    processaNotificaFimTentativa =
        new ProcessaNotificaFimTentativa(logger, tratadorEspecificoCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar14() throws Exception {
    assertThat(processaNotificaFimTentativa.getOrdem(), is(14));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isNotificaFimTentativa()).thenReturn(true);
    assertThat(processaNotificaFimTentativa.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isNotificaFimTentativa()).thenReturn(false);
    assertThat(processaNotificaFimTentativa.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void processaDeveriaNotificar() throws Exception {
    processaNotificaFimTentativa.executa(ligacao, cliente, resultadoLigacao, daoFactory);
    verify(tratadorEspecificoCliente).notificaFimTentativa(daoFactory, ligacao, cliente, campanha,
        dataBanco, telefone, resultadoLigacao, INUTILIZA_MOTIVO_DIFERENCIADO);
  }

  @Test
  public void processaDeveriaRetornarTrue() throws Exception {
    assertThat(
        processaNotificaFimTentativa.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

}
