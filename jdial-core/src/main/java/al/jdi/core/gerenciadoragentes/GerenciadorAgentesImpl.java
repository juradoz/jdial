package al.jdi.core.gerenciadoragentes;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.telephony.ProviderEvent;
import javax.telephony.ProviderListener;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdial.common.Engine;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.core.Service;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentesModule.GerenciadorAgentesService;
import al.jdi.cti.CtiManager;
import al.jdi.cti.DialerCtiManager;
import al.jdi.dao.beans.DaoFactory;

@Default
@GerenciadorAgentesService
class GerenciadorAgentesImpl implements GerenciadorAgentes, Runnable, Service, ProviderListener {

  private static final Logger logger = LoggerFactory.getLogger(GerenciadorAgentesImpl.class);

  private final DialerCtiManager dialerCtiManager;
  private final Configuracoes configuracoes;
  private final Engine.Factory engineFactory;
  private final Provider<DaoFactory> daoFactoryProvider;

  private Engine engine;
  private int livres;
  private boolean inService = false;

  @Inject
  GerenciadorAgentesImpl(DialerCtiManager dialerCtiManager, CtiManager ctiManager,
      Configuracoes configuracoes, Engine.Factory engineFactory,
      Provider<DaoFactory> daoFactoryProvider) {
    this.dialerCtiManager = dialerCtiManager;
    this.configuracoes = configuracoes;
    this.engineFactory = engineFactory;
    this.daoFactoryProvider = daoFactoryProvider;
    ctiManager.addListener(this);
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
    engine = engineFactory.create(this, Period.seconds(2), true);
    logger.info("Iniciado {}", this);
  }

  @Override
  public void stop() {
    logger.debug("Encerrando {}...", this);
    if (engine == null)
      throw new IllegalStateException();
    engine.stop();
    engine = null;
    logger.info("Encerrado {}", this);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

  @Override
  public void providerEventTransmissionEnded(ProviderEvent event) {
    inService = false;
  }

  @Override
  public void providerInService(ProviderEvent event) {
    inService = true;
  }

  @Override
  public void providerOutOfService(ProviderEvent event) {
    inService = false;
  }

  @Override
  public void providerShutdown(ProviderEvent event) {
    inService = false;
  }

}
