package net.danieljurado.dialer.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.modelo.Ligacao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.MotivoFinalizacao;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public class ProcessaFinalizaRegistroAtendidoTest {

  @Mock
  private Configuracoes configuracoes;
  @Mock
  private FinalizadorCliente finalizadorCliente;
  @Mock
  private NotificadorCliente notificadorCliente;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private MotivoFinalizacao motivoFinalizacao;
  @Mock
  private Dao<MotivoFinalizacao> motivoFinalizacaoDao;
  @Mock
  private Mailing mailing;
  @Mock
  private Campanha campanha;
  @Mock
  private Telefone telefone;

  private ProcessaFinalizaRegistroAtendido processaFinalizaRegistroAtendido;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getMotivoFinalizacaoDao()).thenReturn(motivoFinalizacaoDao);
    when(motivoFinalizacaoDao.procura("Atendimento")).thenReturn(motivoFinalizacao);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(cliente.getMailing()).thenReturn(mailing);
    when(mailing.getCampanha()).thenReturn(campanha);
    processaFinalizaRegistroAtendido =
        new ProcessaFinalizaRegistroAtendido(configuracoes, finalizadorCliente, notificadorCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaFinalizaRegistroAtendido.getOrdem(), is(5));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(configuracoes.getFinalizaRegistroAtendido()).thenReturn(true);
    when(ligacao.isAtendida()).thenReturn(true);
    assertThat(
        processaFinalizaRegistroAtendido.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalseConfig() throws Exception {
    when(configuracoes.getFinalizaRegistroAtendido()).thenReturn(false);
    when(ligacao.isAtendida()).thenReturn(true);
    assertThat(
        processaFinalizaRegistroAtendido.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseLigacao() throws Exception {
    when(configuracoes.getFinalizaRegistroAtendido()).thenReturn(true);
    when(ligacao.isAtendida()).thenReturn(false);
    assertThat(
        processaFinalizaRegistroAtendido.accept(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
  }

  @Test
  public void executaDeveriaFinalizar() throws Exception {
    assertThat(
        processaFinalizaRegistroAtendido.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
    verify(finalizadorCliente).finaliza(daoFactory, cliente, motivoFinalizacao);
  }

  @Test
  public void executaDeveriaNotificar() throws Exception {
    assertThat(
        processaFinalizaRegistroAtendido.executa(ligacao, cliente, resultadoLigacao, daoFactory),
        is(false));
    verify(notificadorCliente).notificaFinalizacao(daoFactory, ligacao, cliente, resultadoLigacao,
        telefone, false, campanha);
  }

}
