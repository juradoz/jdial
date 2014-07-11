package net.danieljurado.dialer.tratadorespecificocliente;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.modelo.Discavel;
import net.danieljurado.dialer.modelo.Ligacao;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.dao.beans.ClienteDaoTsa;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.ResultadoLigacaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Grupo;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Situacao;
import al.jdi.dao.model.Telefone;

public class TratadorEspecificoClienteTsaImplTest {

  private static final String NOME_BASE_DADOS = "NOME_BASE_DADOS";
  private static final int MOTIVO_POR_QUANTIDADE = 3;
  private static final int MOTIVO = 2;
  private static final int MOTIVO_FINALIZACAO = 3;
  private static final Situacao SITUACAO_TENTATIVA = Situacao.TENTATIVA;
  private static final Situacao SITUACAO_FINALIZACAO = Situacao.FINALIZACAO;
  private static final boolean INUTILIZA_COM_MOTIVO_DIFERENCIADO = false;
  private static final int OPERADOR_DISCADOR = 3;
  private static final DateTime DATA_BANCO = new DateTime();
  private static final int MOTIVO_CAMPANHA = 0;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Cliente cliente;
  @Mock
  private ClienteDaoTsa clienteDaoTsa;
  @Mock
  private Ligacao ligacao;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private Telefone telefone;
  @Mock
  private Discavel discavel;
  @Mock
  private Campanha campanha;
  @Mock
  private Grupo grupo;
  @Mock
  private ResultadoLigacaoDao resultadoLigacaoDao;

  private TratadorEspecificoClienteTsaImpl tratadorEspecificoClienteTsaImpl;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(campanha.getGrupo()).thenReturn(grupo);
    when(configuracoes.isUraReversa()).thenReturn(false);
    when(daoFactory.getClienteDaoTsa()).thenReturn(clienteDaoTsa);
    when(daoFactory.getResultadoLigacaoDao()).thenReturn(resultadoLigacaoDao);
    when(clienteDaoTsa.isDnc(cliente, NOME_BASE_DADOS)).thenReturn(true);
    when(ligacao.isAtendida()).thenReturn(false);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    when(resultadoLigacao.getMotivo()).thenReturn(MOTIVO);
    when(resultadoLigacao.getMotivoFinalizacaoPorQuantidadeResultado()).thenReturn(
        MOTIVO_POR_QUANTIDADE);
    when(configuracoes.getOperador()).thenReturn(OPERADOR_DISCADOR);
    when(configuracoes.getMotivoFinalizacao()).thenReturn(MOTIVO_FINALIZACAO);
    when(configuracoes.getNomeBaseDados()).thenReturn(NOME_BASE_DADOS);
    when(clienteDaoTsa.reservaNaBaseDoCliente(cliente, OPERADOR_DISCADOR, NOME_BASE_DADOS))
        .thenReturn(true);
    tratadorEspecificoClienteTsaImpl = new TratadorEspecificoClienteTsaImpl(configuracoes);
  }

  @Test
  public void isDncDeveriaRetornarTrueSeClienteDaoTsaMandar() {
    boolean isDnc = tratadorEspecificoClienteTsaImpl.isDnc(daoFactory, cliente, NOME_BASE_DADOS);
    assertThat(isDnc, is(true));
  }

  @Test
  public void isDncDeveriaRetornarFalseSeClienteDaoTsaMandar() {
    when(clienteDaoTsa.isDnc(cliente, NOME_BASE_DADOS)).thenReturn(false);
    boolean isDnc = tratadorEspecificoClienteTsaImpl.isDnc(daoFactory, cliente, NOME_BASE_DADOS);
    assertThat(isDnc, is(false));
  }

  @Test
  public void notificaFimTentativaDeveriaLiberarNaBaseSempre() {
    tratadorEspecificoClienteTsaImpl.notificaFimTentativa(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, INUTILIZA_COM_MOTIVO_DIFERENCIADO);
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  @Test
  public void notificaFimTentativaDeveriaNotificarComLigacaoNaoAtendida() {
    tratadorEspecificoClienteTsaImpl.notificaFimTentativa(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, INUTILIZA_COM_MOTIVO_DIFERENCIADO);
    verify(clienteDaoTsa).insereResultadoTsa(cliente, resultadoLigacao, telefone, DATA_BANCO,
        SITUACAO_TENTATIVA, MOTIVO, MOTIVO_FINALIZACAO, NOME_BASE_DADOS, OPERADOR_DISCADOR,
        MOTIVO_CAMPANHA);
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  @Test
  public void notificaFimTentativaDeveriaNotificarComLigacaoNaoAtendidaMotivoDiferenciado() {
    tratadorEspecificoClienteTsaImpl.notificaFimTentativa(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, true);
    verify(clienteDaoTsa).insereResultadoTsa(cliente, resultadoLigacao, telefone, DATA_BANCO,
        SITUACAO_TENTATIVA, MOTIVO_POR_QUANTIDADE, MOTIVO_FINALIZACAO, NOME_BASE_DADOS,
        OPERADOR_DISCADOR, MOTIVO_CAMPANHA);
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  @Test
  public void notificaFimTentativaNaoDeveriaNotificarComLigacaoAtendida() {
    when(ligacao.isAtendida()).thenReturn(true);
    tratadorEspecificoClienteTsaImpl.notificaFimTentativa(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, INUTILIZA_COM_MOTIVO_DIFERENCIADO);
    verify(clienteDaoTsa, never()).insereResultadoTsa(any(Cliente.class),
        any(ResultadoLigacao.class), any(Telefone.class), any(DateTime.class), any(Situacao.class),
        anyInt(), anyInt(), anyString(), anyInt(), anyInt());
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  // xxx

  @Test
  public void notificaFinalizacaoDeveriaLiberarNaBaseSempre() {
    tratadorEspecificoClienteTsaImpl.notificaFinalizacao(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, INUTILIZA_COM_MOTIVO_DIFERENCIADO);
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  @Test
  public void notificaFinalizacaoDeveriaNotificarComLigacaoNaoAtendida() {
    tratadorEspecificoClienteTsaImpl.notificaFinalizacao(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, INUTILIZA_COM_MOTIVO_DIFERENCIADO);
    verify(clienteDaoTsa).insereResultadoTsa(cliente, resultadoLigacao, telefone, DATA_BANCO,
        SITUACAO_FINALIZACAO, MOTIVO, MOTIVO_FINALIZACAO, NOME_BASE_DADOS, OPERADOR_DISCADOR,
        MOTIVO_CAMPANHA);
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  @Test
  public void notificaFinalizacaoDeveriaNotificarComLigacaoNaoAtendidaMotivoDiferenciado() {
    tratadorEspecificoClienteTsaImpl.notificaFinalizacao(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, true);
    verify(clienteDaoTsa).insereResultadoTsa(cliente, resultadoLigacao, telefone, DATA_BANCO,
        SITUACAO_FINALIZACAO, MOTIVO_POR_QUANTIDADE, MOTIVO_FINALIZACAO, NOME_BASE_DADOS,
        OPERADOR_DISCADOR, MOTIVO_CAMPANHA);
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  @Test
  public void notificaFinalizacaoNaoDeveriaNotificarComLigacaoAtendida() {
    when(ligacao.isAtendida()).thenReturn(true);
    tratadorEspecificoClienteTsaImpl.notificaFinalizacao(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, INUTILIZA_COM_MOTIVO_DIFERENCIADO);
    verify(clienteDaoTsa, never()).insereResultadoTsa(any(Cliente.class),
        any(ResultadoLigacao.class), any(Telefone.class), any(DateTime.class), any(Situacao.class),
        anyInt(), anyInt(), anyString(), anyInt(), anyInt());
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  @Test
  public void notificaFinalizacaoDeveriaNotificarLigacaoAtendidaSemGrupo() {
    when(ligacao.isAtendida()).thenReturn(true);
    when(campanha.getGrupo()).thenReturn(null);
    when(configuracoes.isUraReversa()).thenReturn(true);
    tratadorEspecificoClienteTsaImpl.notificaFinalizacao(daoFactory, ligacao, cliente, campanha,
        DATA_BANCO, telefone, resultadoLigacao, INUTILIZA_COM_MOTIVO_DIFERENCIADO);
    verify(clienteDaoTsa).insereResultadoTsa(any(Cliente.class), any(ResultadoLigacao.class),
        any(Telefone.class), any(DateTime.class), any(Situacao.class), anyInt(), anyInt(),
        anyString(), anyInt(), anyInt());
    verify(clienteDaoTsa).liberaNaBaseDoCliente(cliente, NOME_BASE_DADOS, OPERADOR_DISCADOR);
  }

  @Test
  public void obtemClienteDaoDeveriaRetornarClienteDaoTsa() {
    ClienteDaoTsa clienteDao =
        (ClienteDaoTsa) tratadorEspecificoClienteTsaImpl.obtemClienteDao(daoFactory);
    assertThat(clienteDao, is(sameInstance(clienteDaoTsa)));
  }

  @Test
  public void reservaNaBaseDoClienteDeveriaRetornarTrueSeClienteDaoTsaMandar() {
    boolean reservaNaBaseDoCliente =
        tratadorEspecificoClienteTsaImpl.reservaNaBaseDoCliente(daoFactory, cliente);
    assertThat(reservaNaBaseDoCliente, is(true));
  }

  @Test
  public void reservaNaBaseDoClienteDeveriaRetornarFalseSeClienteDaoTsaMandar() {
    when(clienteDaoTsa.reservaNaBaseDoCliente(cliente, OPERADOR_DISCADOR, NOME_BASE_DADOS))
        .thenReturn(false);
    boolean reservaNaBaseDoCliente =
        tratadorEspecificoClienteTsaImpl.reservaNaBaseDoCliente(daoFactory, cliente);
    assertThat(reservaNaBaseDoCliente, is(false));
  }

}
