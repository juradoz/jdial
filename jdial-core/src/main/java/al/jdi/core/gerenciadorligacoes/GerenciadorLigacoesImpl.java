package al.jdi.core.gerenciadorligacoes;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.CoreMatchers.is;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdial.common.Engine;
import org.jdial.common.Service;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.devolveregistro.DevolveRegistro;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoesModule.GerenciadorLigacoesService;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoesModule.PredictiveListenerFactory;
import al.jdi.core.modelo.Ligacao;
import al.jdi.cti.DialerCtiManager;
import al.jdi.cti.PredictiveListener;
import al.jdi.cti.TratamentoSecretariaEletronica;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Servico;
import al.jdi.dao.model.Telefone;

@Default
@Singleton
@GerenciadorLigacoesService
class GerenciadorLigacoesImpl implements GerenciadorLigacoes, Runnable, Service {

  private final Logger logger;
  private final Provider<DaoFactory> daoFactoryProvider;
  private final Configuracoes configuracoes;
  private final DialerCtiManager dialerCtiManager;
  private final DevolveRegistro devolveRegistro;
  private final Map<PredictiveListener, Ligacao> ligacoes;
  private final PredictiveListenerFactory predictiveListenerFactory;
  private final Engine.Factory engineFactory;
  private final GerenciadorFatorK gerenciadorFatorK;

  private Engine engine;

  @Inject
  GerenciadorLigacoesImpl(Logger logger, Provider<DaoFactory> daoFactoryProvider,
      Configuracoes configuracoes, DialerCtiManager dialerCtiManager,
      Map<PredictiveListener, Ligacao> ligacoes,
      PredictiveListenerFactory predictiveListenerFactory, DevolveRegistro devolveRegistro,
      Engine.Factory engineFactory, GerenciadorFatorK gerenciadorFatorK) {
    this.logger = logger;
    this.daoFactoryProvider = daoFactoryProvider;
    this.configuracoes = configuracoes;
    this.dialerCtiManager = dialerCtiManager;
    this.ligacoes = ligacoes;
    this.predictiveListenerFactory = predictiveListenerFactory;
    this.devolveRegistro = devolveRegistro;
    this.engineFactory = engineFactory;
    this.gerenciadorFatorK = gerenciadorFatorK;
    logger.debug("Iniciando {}...", this);
  }

  void chamadaAtendida(PredictiveListener listener, int callId) {
    Ligacao ligacao;
    synchronized (ligacoes) {
      ligacao = ligacoes.get(listener);
    }

    if (ligacao == null) {
      logger.debug("Ligacao atendida nao encontrada. callId: {}", callId);
      return;
    }

    logger.info("Chamada atendida. callId: {} cliente: {}", callId, ligacao.getDiscavel()
        .getCliente());

    if (!ligacao.isAtendida())
      gerenciadorFatorK.chamadaAtendida();

    DateTime dataBanco;
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      dataBanco = daoFactory.getDataBanco();
    } finally {
      daoFactory.close();
    }
    ligacao.setAtendimento(dataBanco);
  }

  void chamadaEmFila(PredictiveListener listener, int callId) {
    Ligacao ligacao;
    synchronized (ligacoes) {
      ligacao = ligacoes.get(listener);
    }

    if (ligacao == null) {
      logger.debug("Ligacao em fila nao encontrada. callId: {}", callId);
      return;
    }

    logger.info("Chamada em fila. callId: {} cliente: {}", callId, ligacao.getDiscavel()
        .getCliente());
    ligacao.setFoiPraFila(true);
  }

  void chamadaEncerrada(PredictiveListener listener, int callId, int causa) {
    Ligacao ligacao;
    synchronized (ligacoes) {
      ligacao = ligacoes.remove(listener);
    }

    if (ligacao == null) {
      logger.debug("Ligacao encerrada nao encontrada. callId: {}", callId);
      return;
    }

    logger.info("Chamada encerrada. callId: {} causa: {} cliente: {}", new Object[] {callId, causa,
        ligacao.getDiscavel().getCliente()});

    DateTime dataBanco;
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      dataBanco = daoFactory.getDataBanco();
    } finally {
      daoFactory.close();
    }
    ligacao.setTermino(dataBanco);
    ligacao.setMotivoFinalizacao(causa);
    devolveRegistro.devolveLigacao(ligacao);
  }

  void chamadaErro(PredictiveListener listener, Exception e) {
    Ligacao ligacao;
    synchronized (ligacoes) {
      ligacao = ligacoes.remove(listener);
    }

    if (ligacao == null) {
      logger.debug("Ligacao com erro nao encontrada. {}", e.getMessage());
      return;
    }

    logger.info("Chamada com erro para cliente: {} erro: {}", ligacao.getDiscavel().getCliente(),
        e.getMessage());

    DateTime dataBanco;
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      dataBanco = daoFactory.getDataBanco();
    } finally {
      daoFactory.close();
    }
    ligacao.setTermino(dataBanco);
    devolveRegistro.devolveLigacao(ligacao);
  }

  void chamadaIniciada(PredictiveListener listener, int callId) {
    Ligacao ligacao;
    synchronized (ligacoes) {
      ligacao = ligacoes.get(listener);
    }

    if (ligacao == null) {
      logger.debug("Ligacao iniciada nao encontrada. callId: {}", callId);
      return;
    }

    gerenciadorFatorK.chamadaIniciada();
    logger.info("Chamada iniciada. callId: {} cliente: {}", callId, ligacao.getDiscavel()
        .getCliente());
  }

  void chamadaInvalida(PredictiveListener listener, int callId, int causa) {
    Ligacao ligacao;
    synchronized (ligacoes) {
      ligacao = ligacoes.remove(listener);
    }

    if (ligacao == null) {
      logger.debug("Ligacao invalida nao encontrada. callId: {}", callId);
      return;
    }

    logger.info("Chamada invalida. callId: {} causa: {} cliente: {}", new Object[] {callId, causa,
        ligacao.getDiscavel().getCliente()});

    DateTime dataBanco;
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      dataBanco = daoFactory.getDataBanco();
    } finally {
      daoFactory.close();
    }
    ligacao.setTermino(dataBanco);
    ligacao.setMotivoFinalizacao(causa);
    devolveRegistro.devolveLigacao(ligacao);
  }

  void chamadaNoAgente(PredictiveListener listener, int callId, String agente) {
    Ligacao ligacao;
    synchronized (ligacoes) {
      ligacao = ligacoes.get(listener);
    }

    if (ligacao == null) {
      logger.debug("Ligacao no agente nao encontrada. callId: {} agente: {}", callId, agente);
      return;
    }

    logger.info("Chamada no agente. callId: {} agente: {} cliente: {}", new Object[] {callId,
        agente, ligacao.getDiscavel().getCliente()});
    ligacao.setAgente(agente);
  }

  @Override
  public void disca(Ligacao ligacao, Servico servico) {

    if (!ligacao.getDiscavel().getCliente().getTelefone().isUtil())
      logger.warn("Tentando discar para um telefone inutil {} Telefone {}", ligacao.getDiscavel()
          .getCliente(), ligacao.getDiscavel().getCliente().getTelefone());

    String destino = ligacao.getDiscavel().getDestino();

    int maxRings = configuracoes.getMaxRings();

    TratamentoSecretariaEletronica tratamentoSecretariaEletronica =
        getTratamentoSecretariaEletronica(ligacao.getDiscavel().getCliente().getTelefone());

    logger.info("Discando para {} cliente: {} chave: {} tratamentoSecretaria: {}", new Object[] {
        destino, ligacao.getDiscavel().getCliente(), ligacao.getDiscavel().getChave(),
        tratamentoSecretariaEletronica});

    PredictiveListener predictiveListener = predictiveListenerFactory.create(this);
    synchronized (ligacoes) {
      ligacoes.put(predictiveListener, ligacao);
    }

    DateTime inicio = new DateTime();
    dialerCtiManager.makePredictiveCall(servico.getNome(), destino, maxRings,
        tratamentoSecretariaEletronica, ligacao.getDiscavel().getChave(), predictiveListener);
    logger.debug("makePredictiveCall demorou {} ms",
        new Duration(inicio, new DateTime()).getMillis());
  }

  TratamentoSecretariaEletronica getTratamentoSecretariaEletronica(Telefone telefone) {
    if (!configuracoes.isDetectaCaixaPostalPeloTelefone())
      return configuracoes.getTratamentoSecretariaEletronica();

    return telefone.isDetectaCaixaPostal() ? TratamentoSecretariaEletronica.DESLIGAR
        : TratamentoSecretariaEletronica.TRANSFERIR;
  }

  @Override
  public int getQuantidadeLigacoes() {
    synchronized (ligacoes) {
      return ligacoes.size();
    }
  }

  @Override
  public void run() {
    Period timeout = Period.minutes(configuracoes.getMinutosExpiracaoChamadasNaoAtendidas());
    HashSet<Ligacao> expiradas = new HashSet<Ligacao>();
    synchronized (ligacoes) {
      for (Iterator<Ligacao> it = ligacoes.values().iterator(); it.hasNext();) {
        Ligacao ligacao = it.next();
        if (ligacao.isAtendida())
          continue;

        boolean expirada = ligacao.getCriacao().plus(timeout).isBeforeNow();

        if (!expirada)
          continue;

        it.remove();
        expiradas.add(ligacao);
      }
    }

    for (Ligacao ligacao : expiradas) {
      logger.warn("Chamada para cliente {} removida por inatividade: {} segundos", ligacao
          .getDiscavel().getCliente(), new Duration(ligacao.getCriacao(), new DateTime())
          .getStandardSeconds());
      ligacao.setMotivoFinalizacao(0);
      devolveRegistro.devolveLigacao(ligacao);
    }
  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalArgumentException();
    engine = engineFactory.create(this, Period.seconds(5), true, true);
    logger.info("Iniciado {}", this);
  }

  @Override
  public void stop() {
    logger.debug("Encerrando {}...", this);
    if (engine == null)
      throw new IllegalArgumentException("Already stopped");
    engine.stop();
    engine = null;
    logger.info("Encerrado {}", this);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

  @Override
  public int getQuantidadeLigacoesNaoAtendidas() {
    synchronized (ligacoes) {
      return filter(having(on(Ligacao.class).isAtendida(), is(false)), ligacoes.values()).size();
    }
  }
}
