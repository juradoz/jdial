package al.jdi.core.gerenciadorligacoes;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.devolveregistro.DevolveRegistro;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoesModule.PredictiveListenerFactory;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.cti.DialerCtiManager;
import al.jdi.cti.PredictiveListener;
import al.jdi.cti.TratamentoSecretariaEletronica;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Servico;
import al.jdi.dao.model.Telefone;

public class GerenciadorLigacoesImplTest {

  private static final int CALL_ID = 1;
  private static final int CAUSA = 2;
  private static final DateTime DATA_BANCO = new DateTime();
  private static final String AGENTE = "AGENTE";
  private static final String DESTINO = "DESTINO";
  private static final int MAX_RINGS = 5;
  private static final TratamentoSecretariaEletronica TRATAMENTO_SECRETARIA =
      TratamentoSecretariaEletronica.TRANSFERIR;
  private static final String SERVICO = "SERVICO";
  private static final String CHAVE = "CHAVE";
  private static final DateTime CRIACAO = new DateTime();
  private static final Integer MINUTOS_EXPIRACAO = 5;

  private GerenciadorLigacoesImpl gerenciadorLigacoesImpl;

  @Mock
  private Provider<DaoFactory> daoFactoryProvider;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private DialerCtiManager dialerCtiManager;
  @Mock
  private Map<PredictiveListener, Ligacao> ligacoes;
  @Mock
  private PredictiveListenerFactory predictiveListenerFactory;
  @Mock
  private DevolveRegistro devolveRegistro;
  @Mock
  private Engine.Factory engineFactory;
  @Mock
  private GerenciadorFatorK gerenciadorFatorK;
  @Mock
  private PredictiveListener listener;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Discavel discavel;
  @Mock
  private Cliente cliente;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Exception e;
  @Mock
  private Telefone telefone;
  @Mock
  private Servico servico;
  @Mock
  private Logger logger;

  @Before
  public void setUp() throws Exception {
    initMocks(this);

    when(ligacoes.get(listener)).thenReturn(ligacao);
    when(ligacoes.remove(listener)).thenReturn(ligacao);

    when(ligacao.getCriacao()).thenReturn(CRIACAO);

    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    when(discavel.getDestino()).thenReturn(DESTINO);
    when(discavel.getChave()).thenReturn(CHAVE);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(telefone.isUtil()).thenReturn(true);
    when(daoFactoryProvider.get()).thenReturn(daoFactory);
    when(daoFactory.getDataBanco()).thenReturn(DATA_BANCO);
    when(servico.getNome()).thenReturn(SERVICO);

    when(configuracoes.getMaxRings()).thenReturn(MAX_RINGS);
    when(configuracoes.getTratamentoSecretariaEletronica()).thenReturn(TRATAMENTO_SECRETARIA);
    when(configuracoes.getMinutosExpiracaoChamadasNaoAtendidas()).thenReturn(MINUTOS_EXPIRACAO);

    gerenciadorLigacoesImpl =
        new GerenciadorLigacoesImpl(logger, daoFactoryProvider, configuracoes, dialerCtiManager,
            ligacoes, predictiveListenerFactory, devolveRegistro, engineFactory, gerenciadorFatorK);

    when(predictiveListenerFactory.create(gerenciadorLigacoesImpl)).thenReturn(listener);
  }

  @Test
  public void chamadaAtendidaSemLigacao() throws Exception {
    when(ligacoes.get(listener)).thenReturn(null);
    gerenciadorLigacoesImpl.chamadaAtendida(listener, CALL_ID);
    verify(gerenciadorFatorK, never()).chamadaAtendida();
  }

  @Test
  public void chamadaAtendidaDeveriaNotificar() throws Exception {
    gerenciadorLigacoesImpl.chamadaAtendida(listener, CALL_ID);
    verify(gerenciadorFatorK).chamadaAtendida();
  }

  @Test
  public void chamadaAtendidaDeveriaSetarDataBancoLigacao() throws Exception {
    gerenciadorLigacoesImpl.chamadaAtendida(listener, CALL_ID);
    verify(ligacao).setAtendimento(DATA_BANCO);
  }

  @Test
  public void chamadaEmFilaSemLigacao() throws Exception {
    when(ligacoes.get(listener)).thenReturn(null);
    gerenciadorLigacoesImpl.chamadaEmFila(listener, CALL_ID);
    verify(ligacao, never()).setFoiPraFila(true);
  }

  @Test
  public void chamadaEmFilaDeveriaSetarLigacao() throws Exception {
    gerenciadorLigacoesImpl.chamadaEmFila(listener, CALL_ID);
    verify(ligacao).setFoiPraFila(true);
  }

  @Test
  public void chamadaEncerradaSemLigacao() throws Exception {
    when(ligacoes.remove(listener)).thenReturn(null);
    gerenciadorLigacoesImpl.chamadaEncerrada(listener, CALL_ID, CAUSA);
    verify(ligacao, never()).setTermino(DATA_BANCO);
    verify(ligacao, never()).setMotivoFinalizacao(CAUSA);
    verify(devolveRegistro, never()).devolveLigacao(configuracoes, ligacao);
  }

  @Test
  public void chamadaEncerradaDeveriaSetarTerminoLigacao() throws Exception {
    gerenciadorLigacoesImpl.chamadaEncerrada(listener, CALL_ID, CAUSA);
    verify(ligacao).setTermino(DATA_BANCO);
  }

  @Test
  public void chamadaEncerradaDeveriaSetarMotivoLigacao() throws Exception {
    gerenciadorLigacoesImpl.chamadaEncerrada(listener, CALL_ID, CAUSA);
    verify(ligacao).setMotivoFinalizacao(CAUSA);
  }

  @Test
  public void chamadaEncerradaDeveriaDevolver() throws Exception {
    gerenciadorLigacoesImpl.chamadaEncerrada(listener, CALL_ID, CAUSA);
    verify(devolveRegistro).devolveLigacao(configuracoes, ligacao);
  }

  @Test
  public void chamadaErroSemLigacao() throws Exception {
    when(ligacoes.remove(listener)).thenReturn(null);
    gerenciadorLigacoesImpl.chamadaErro(listener, e);
    verify(ligacao, never()).setTermino(DATA_BANCO);
  }

  @Test
  public void chamadaErroDeveriaSetarTerminoLigacao() throws Exception {
    gerenciadorLigacoesImpl.chamadaErro(listener, e);
    verify(ligacao).setTermino(DATA_BANCO);
  }

  @Test
  public void chamadaErroDeveriaDevolver() throws Exception {
    gerenciadorLigacoesImpl.chamadaErro(listener, e);
    verify(devolveRegistro).devolveLigacao(configuracoes, ligacao);
  }

  @Test
  public void chamadaIniciadaSemLigacao() throws Exception {
    when(ligacoes.get(listener)).thenReturn(null);
    gerenciadorLigacoesImpl.chamadaIniciada(listener, CALL_ID);
    verify(gerenciadorFatorK, never()).chamadaIniciada();
  }

  @Test
  public void chamadaIniciadaDeveriaNotidicar() throws Exception {
    gerenciadorLigacoesImpl.chamadaIniciada(listener, CALL_ID);
    verify(gerenciadorFatorK).chamadaIniciada();
  }

  @Test
  public void chamadaInvalidaSemLigacao() throws Exception {
    when(ligacoes.remove(listener)).thenReturn(null);
    gerenciadorLigacoesImpl.chamadaInvalida(listener, CALL_ID, CAUSA);
    verify(ligacao, never()).setTermino(DATA_BANCO);
    verify(ligacao, never()).setMotivoFinalizacao(CAUSA);
    verify(devolveRegistro, never()).devolveLigacao(configuracoes, ligacao);
  }

  @Test
  public void chamadaInvalidaDeveriaSetarTermino() throws Exception {
    gerenciadorLigacoesImpl.chamadaInvalida(listener, CALL_ID, CAUSA);
    verify(ligacao).setTermino(DATA_BANCO);
  }

  @Test
  public void chamadaInvalidaDeveriaSetarMotivo() throws Exception {
    gerenciadorLigacoesImpl.chamadaInvalida(listener, CALL_ID, CAUSA);
    verify(ligacao).setMotivoFinalizacao(CAUSA);
  }

  @Test
  public void chamadaInvalidaDeveriaDevolver() throws Exception {
    gerenciadorLigacoesImpl.chamadaInvalida(listener, CALL_ID, CAUSA);
    verify(devolveRegistro).devolveLigacao(configuracoes, ligacao);
  }

  @Test
  public void chamadaNoAgenteSemLigacao() throws Exception {
    when(ligacoes.get(listener)).thenReturn(null);
    gerenciadorLigacoesImpl.chamadaNoAgente(listener, CALL_ID, AGENTE);
    verify(ligacao, never()).setAgente(AGENTE);
  }

  @Test
  public void chamadaNoAgenteDeveriaSetarLigacao() throws Exception {
    gerenciadorLigacoesImpl.chamadaNoAgente(listener, CALL_ID, AGENTE);
    verify(ligacao).setAgente(AGENTE);
  }

  @Test
  public void discaDeveriaInserirLigacao() throws Exception {
    gerenciadorLigacoesImpl.disca(ligacao, servico);
    verify(ligacoes).put(listener, ligacao);
  }

  @Test
  public void discaDeveriaDiscar() throws Exception {
    gerenciadorLigacoesImpl.disca(ligacao, servico);
    verify(dialerCtiManager).makePredictiveCall(SERVICO, DESTINO, MAX_RINGS, TRATAMENTO_SECRETARIA,
        CHAVE, listener);
  }

  @Test
  public void getTratamentoSecretariaDeveriaRetornarPadrao() throws Exception {
    assertThat(gerenciadorLigacoesImpl.getTratamentoSecretariaEletronica(telefone),
        is(TRATAMENTO_SECRETARIA));
  }

  @Test
  public void getTratamentoSecretariaDeveriaRetornarTelefone() throws Exception {
    when(configuracoes.isDetectaCaixaPostalPeloTelefone()).thenReturn(true);
    when(telefone.isDetectaCaixaPostal()).thenReturn(true);
    assertThat(gerenciadorLigacoesImpl.getTratamentoSecretariaEletronica(telefone),
        is(TratamentoSecretariaEletronica.DESLIGAR));
  }

  @Test
  public void getQtdLigacoesDeveriaRetornarQtdAtivas() throws Exception {
    int qtd = (int) (Math.random() * 1000);
    when(ligacoes.size()).thenReturn(qtd);
    assertThat(gerenciadorLigacoesImpl.getQuantidadeLigacoes(), is(qtd));
  }

  @Test
  public void getQtdLigacoesNaoAtendidasDeveriaRetornar() throws Exception {
    Ligacao l1 = mock(Ligacao.class);
    when(l1.isAtendida()).thenReturn(true);
    Ligacao l2 = mock(Ligacao.class);
    Ligacao l3 = mock(Ligacao.class);

    Map<PredictiveListener, Ligacao> ligacoes = new HashMap<PredictiveListener, Ligacao>();
    ligacoes.put(mock(PredictiveListener.class), l1);
    ligacoes.put(mock(PredictiveListener.class), l2);
    ligacoes.put(mock(PredictiveListener.class), l3);

    gerenciadorLigacoesImpl =
        new GerenciadorLigacoesImpl(logger, daoFactoryProvider, configuracoes, dialerCtiManager,
            ligacoes, predictiveListenerFactory, devolveRegistro, engineFactory, gerenciadorFatorK);

    assertThat(gerenciadorLigacoesImpl.getQuantidadeLigacoesNaoAtendidas(), is(2));
  }

  @Test
  public void runNaoDeveriaRemoverNenhumaLigacao() throws Exception {
    Map<PredictiveListener, Ligacao> ligacoes = new HashMap<PredictiveListener, Ligacao>();
    ligacoes.put(listener, ligacao);
    gerenciadorLigacoesImpl =
        new GerenciadorLigacoesImpl(logger, daoFactoryProvider, configuracoes, dialerCtiManager,
            ligacoes, predictiveListenerFactory, devolveRegistro, engineFactory, gerenciadorFatorK);
    gerenciadorLigacoesImpl.run();
    assertThat(ligacoes.get(listener), is(sameInstance(ligacao)));
  }

  @Test
  public void runNaoDeveriaRemoverLigacao() throws Exception {
    Map<PredictiveListener, Ligacao> ligacoes = new HashMap<PredictiveListener, Ligacao>();
    ligacoes.put(listener, ligacao);
    when(ligacao.getCriacao()).thenReturn(new DateTime().minusMinutes(MINUTOS_EXPIRACAO + 1));
    gerenciadorLigacoesImpl =
        new GerenciadorLigacoesImpl(logger, daoFactoryProvider, configuracoes, dialerCtiManager,
            ligacoes, predictiveListenerFactory, devolveRegistro, engineFactory, gerenciadorFatorK);
    gerenciadorLigacoesImpl.run();
    assertThat(ligacoes.get(listener), is(nullValue(Ligacao.class)));
  }
}
