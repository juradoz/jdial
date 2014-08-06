package al.jdi.core;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.telephony.ProviderEvent;
import javax.telephony.ProviderListener;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.core.JDialModule.Versao;
import al.jdi.core.estoque.Estoque;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.cti.DialerCtiManager;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Servico;

class DefaultJDial implements Runnable, ProviderListener, JDial {

  static class DefaultJDialFactory implements JDial.Factory {
    @Inject
    private Engine.Factory engineFactory;
    @Inject
    private @Versao
    String versao;
    @Inject
    private Discavel.Factory discavelFactory;
    @Inject
    private Provider<DaoFactory> daoFactoryProvider;
    @Inject
    private TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
    @Inject
    private DialerCtiManager dialerCtiManager;

    @Override
    public JDial create(Tenant tenant) {
      return new DefaultJDial(engineFactory, versao, discavelFactory, daoFactoryProvider,
          tratadorEspecificoClienteFactory, dialerCtiManager, tenant);
    }
  }

  private static final Logger logger = getLogger(DefaultJDial.class);

  private final Engine.Factory engineFactory;
  private final String versao;
  private final Discavel.Factory discavelFactory;
  private final Provider<DaoFactory> daoFactoryProvider;
  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
  private final Tenant tenant;

  private Engine engine;
  private boolean inService = false;

  DefaultJDial(Engine.Factory engineFactory, String versao, Discavel.Factory discavelFactory,
      Provider<DaoFactory> daoFactoryProvider,
      TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory,
      DialerCtiManager dialerCtiManager, Tenant tenant) {
    this.engineFactory = engineFactory;
    this.versao = versao;
    this.discavelFactory = discavelFactory;
    this.daoFactoryProvider = daoFactoryProvider;
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
    this.tenant = tenant;

    dialerCtiManager.addListener(this);
    logger.info("Iniciando jDial {} {}...", versao, tenant.getCampanha());
    limpaReservas();
  }

  void limpaReservas() {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      Campanha campanha =
          daoFactory.getCampanhaDao().procura(tenant.getConfiguracoes().getNomeCampanha());
      logger.debug("Limpando reservas {}...", campanha.getNome());
      DateTime inicio = new DateTime();
      daoFactory.beginTransaction();
      tratadorEspecificoClienteFactory
          .create(tenant, daoFactory)
          .obtemClienteDao()
          .limpaReservas(campanha, tenant.getConfiguracoes().getNomeBaseDados(),
              tenant.getConfiguracoes().getNomeBase(), tenant.getConfiguracoes().getOperador());
      daoFactory.commit();
      logger.info("Limpou reservas. Demorou {}ms {}",
          new Duration(inicio, new DateTime()).getMillis(), campanha);
    } catch (Exception e) {
      logger.error("{} {}", e.getMessage(), tenant.getCampanha(), e);
    } finally {
      if (daoFactory.hasTransaction())
        daoFactory.rollback();
      daoFactory.close();
    }
  }

  void rodada(DaoFactory daoFactory, Estoque estoque) {
    Campanha campanha =
        daoFactory.getCampanhaDao().procura(tenant.getConfiguracoes().getNomeCampanha());
    logger.debug("Rodada {} {}", estoque, campanha);
    int livres = tenant.getGerenciadorAgentes().getLivres();

    double fatorK = tenant.getGerenciadorFatorK().getFatorK();

    int quantidadeLigacoes = tenant.getGerenciadorLigacoes().getQuantidadeLigacoes();

    int quantidadeLigacoesNaoAtendidas =
        tenant.getGerenciadorLigacoes().getQuantidadeLigacoesNaoAtendidas();

    int quantidade = ((int) (livres * fatorK) - quantidadeLigacoesNaoAtendidas);

    logger.info("Rodada {} Livres: {} * fatorK: {} - ligacoes: {} ({} total) = quantidade: {}",
        new Object[] {estoque, livres, fatorK, quantidadeLigacoesNaoAtendidas, quantidadeLigacoes,
            quantidade});

    if (quantidade <= 0)
      return;

    Collection<Cliente> clientesAgendados = estoque.obtemRegistros(quantidade);

    logger.debug("Obtive {} clientes de {} {}", clientesAgendados.size(), estoque,
        tenant.getCampanha());

    DateTime dataBanco = daoFactory.getDataBanco();
    Servico servico = campanha.getServico();

    for (Cliente cliente : clientesAgendados) {
      Discavel discavel = discavelFactory.create(tenant, cliente);
      Ligacao ligacao = new Ligacao.Builder(discavel).setInicio(dataBanco).build();
      DateTime inicio = new DateTime();
      tenant.getGerenciadorLigacoes().disca(ligacao, servico);
      logger.debug("Discagem demorou {} ms {}", new Duration(inicio, new DateTime()).getMillis(),
          tenant.getCampanha());
    }
  }

  @Override
  public void run() {
    if (!tenant.getConfiguracoes().getSistemaAtivo()) {
      logger.warn("Sistema inativo {}", tenant.getCampanha());
      return;
    }

    if (!inService) {
      logger.warn("Fora de servico. {}", tenant.getCampanha());
      return;
    }

    logger.debug("Sistema ativo! {}", tenant.getCampanha());
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      rodada(daoFactory, tenant.getEstoqueAgendados());
      rodada(daoFactory, tenant.getEstoqueLivres());
    } finally {
      daoFactory.close();
    }

  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException("Already started");

    engine =
        engineFactory.create(this, tenant.getConfiguracoes().getIntervaloEntreRodadas(), false,
            true);
    logger
        .warn(
            "\n------------------------------------\nIniciado jDial {} {}\n------------------------------------",
            tenant.getConfiguracoes().getNomeCampanha(), versao);
  }

  @Override
  public void stop() {
    if (engine == null)
      throw new IllegalStateException("Already stopped");
    engine.stop();
    engine = null;
    limpaReservas();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(tenant.getCampanha())
        .toString();
  }

  @Override
  public void providerEventTransmissionEnded(ProviderEvent event) {
    logger.debug("providerEventTransmissionEnded {}", tenant.getCampanha());
    inService = false;
  }

  @Override
  public void providerInService(ProviderEvent event) {
    logger.debug("providerInService {}", tenant.getCampanha());
    inService = true;
  }

  @Override
  public void providerOutOfService(ProviderEvent event) {
    logger.debug("providerOutOfService {}", tenant.getCampanha());
    inService = false;
  }

  @Override
  public void providerShutdown(ProviderEvent event) {
    logger.debug("providerShutdown {}", tenant.getCampanha());
    inService = false;
  }

}
