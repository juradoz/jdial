package al.jdi.core.gerenciadoragentes;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.telephony.ProviderEvent;
import javax.telephony.ProviderListener;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.cti.DialerCtiManager;
import al.jdi.dao.beans.DaoFactory;

class GerenciadorAgentesImpl implements GerenciadorAgentes, Runnable, ProviderListener {

  static class GerenciadorAgentesFactory implements GerenciadorAgentes.Factory {
    @Inject
    private Logger logger;
    @Inject
    private DialerCtiManager dialerCtiManager;
    @Inject
    private Engine.Factory engineFactory;
    @Inject
    private Provider<DaoFactory> daoFactoryProvider;

    @Override
    public GerenciadorAgentes create(Configuracoes configuracoes) {
      return new GerenciadorAgentesImpl(logger, dialerCtiManager, configuracoes, engineFactory,
          daoFactoryProvider);
    }
  }

  private final Logger logger;
  private final DialerCtiManager dialerCtiManager;
  private final Configuracoes configuracoes;
  private final Engine.Factory engineFactory;
  private final Provider<DaoFactory> daoFactoryProvider;

  private Engine engine;
  private int livres;
  private boolean inService = false;

  GerenciadorAgentesImpl(Logger logger, DialerCtiManager dialerCtiManager,
      Configuracoes configuracoes, Engine.Factory engineFactory,
      Provider<DaoFactory> daoFactoryProvider) {
    this.logger = logger;
    this.dialerCtiManager = dialerCtiManager;
    this.configuracoes = configuracoes;
    this.engineFactory = engineFactory;
    this.daoFactoryProvider = daoFactoryProvider;
    dialerCtiManager.addListener(this);
    logger.debug("Iniciando {}...", this);
  }

  @Override
  public int getLivres() {
    int reservados = configuracoes.getQtdAgentesReservados();
    synchronized (this) {
      return livres - reservados;
    }
  }

  @Override
  public void run() {
    if (!configuracoes.getSistemaAtivo())
      return;

    if (!inService) {
      logger.warn("Fora de servico.");
      return;
    }

    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      if (!configuracoes.getSistemaAtivo())
        return;

      int livres = obtemQtdAgentesLivres(daoFactory);

      synchronized (this) {
        this.livres = livres;
      }
    } finally {
      daoFactory.close();
    }
  }

  int obtemQtdAgentesLivres(DaoFactory daoFactory) {
    String acd =
        daoFactory.getCampanhaDao().procura(configuracoes.getNomeCampanha()).getGrupo().getCodigo();
    logger.debug("Vai obter agentes livres de {}", acd);
    DateTime inicio = new DateTime();
    int livres = dialerCtiManager.getAgentesLivres(acd);
    logger.debug("Acd {} atualmente com {} agentes livres. Consulta demorou {}ms", new Object[] {
        acd, livres, new Duration(inicio, new DateTime()).getMillis()});

    if (configuracoes.isUraReversa()) {
      if (livres > 0) {
        int qtdMaximaCanaisSimultaneos = configuracoes.getQtdMaximaCanaisSimultaneos();
        logger.info("URA Reversa. Retornando canais simultaneos ({})...",
            qtdMaximaCanaisSimultaneos);
        return qtdMaximaCanaisSimultaneos;
      }

      logger.info("URA Reversa. Sem agentes logados...");
      return 0;
    }

    return livres;
  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException();
    engine = engineFactory.create(this, Period.seconds(2), true, true);
  }

  @Override
  public void stop() {
    if (engine == null)
      throw new IllegalStateException("Already stopped");
    engine.stop();
    engine = null;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

  @Override
  public void providerEventTransmissionEnded(ProviderEvent event) {
    logger.debug("providerEventTransmissionEnded");
    inService = false;
  }

  @Override
  public void providerInService(ProviderEvent event) {
    logger.debug("providerInService");
    inService = true;
  }

  @Override
  public void providerOutOfService(ProviderEvent event) {
    logger.debug("providerOutOfService");
    inService = false;
  }

  @Override
  public void providerShutdown(ProviderEvent event) {
    logger.debug("providerShutdown");
    inService = false;
  }

}
