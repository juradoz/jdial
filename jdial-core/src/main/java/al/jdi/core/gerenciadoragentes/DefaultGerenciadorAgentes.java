package al.jdi.core.gerenciadoragentes;

import static org.slf4j.LoggerFactory.getLogger;

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
import al.jdi.core.tenant.Tenant;
import al.jdi.cti.DialerCtiManager;
import al.jdi.dao.beans.DaoFactory;

class DefaultGerenciadorAgentes implements GerenciadorAgentes, Runnable, ProviderListener {

  static class GerenciadorAgentesFactory implements GerenciadorAgentes.Factory {
    @Inject
    private DialerCtiManager dialerCtiManager;
    @Inject
    private Engine.Factory engineFactory;
    @Inject
    private Provider<DaoFactory> daoFactoryProvider;

    @Override
    public GerenciadorAgentes create(Tenant tenant) {
      return new DefaultGerenciadorAgentes(dialerCtiManager, engineFactory, daoFactoryProvider,
          tenant);
    }
  }

  private static final Logger logger = getLogger(DefaultGerenciadorAgentes.class);

  private final DialerCtiManager dialerCtiManager;
  private final Engine.Factory engineFactory;
  private final Provider<DaoFactory> daoFactoryProvider;
  private final Tenant tenant;

  private Engine engine;
  private int livres;
  private boolean inService = false;

  DefaultGerenciadorAgentes(DialerCtiManager dialerCtiManager, Engine.Factory engineFactory,
      Provider<DaoFactory> daoFactoryProvider, Tenant tenant) {
    this.dialerCtiManager = dialerCtiManager;
    this.engineFactory = engineFactory;
    this.daoFactoryProvider = daoFactoryProvider;
    this.tenant = tenant;
    dialerCtiManager.addListener(this);
    logger.debug("Iniciando {}...", this);
  }

  @Override
  public int getLivres() {
    int reservados = tenant.getConfiguracoes().getQtdAgentesReservados();
    synchronized (this) {
      return livres - reservados;
    }
  }

  @Override
  public void run() {
    if (!tenant.getConfiguracoes().getSistemaAtivo())
      return;

    if (!inService) {
      logger.warn("Fora de servico.");
      return;
    }

    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      if (!tenant.getConfiguracoes().getSistemaAtivo())
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
        daoFactory.getCampanhaDao().procura(tenant.getConfiguracoes().getNomeCampanha()).getGrupo().getCodigo();
    logger.debug("Vai obter agentes livres de {}", acd);
    DateTime inicio = new DateTime();
    int livres = dialerCtiManager.getAgentesLivres(acd);
    logger.debug("Acd {} atualmente com {} agentes livres. Consulta demorou {}ms", new Object[] {
        acd, livres, new Duration(inicio, new DateTime()).getMillis()});

    if (tenant.getConfiguracoes().isUraReversa()) {
      if (livres > 0) {
        int qtdMaximaCanaisSimultaneos = tenant.getConfiguracoes().getQtdMaximaCanaisSimultaneos();
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
