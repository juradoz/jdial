package al.jdi.core.tenant;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.core.JDial;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.estoque.Estoque;
import al.jdi.core.estoque.EstoqueModule.Agendados;
import al.jdi.core.estoque.EstoqueModule.Livres;
import al.jdi.core.estoque.ExtraidorClientes;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoes;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;

class DefaultTenant implements Tenant, Runnable {

  static class DefaultTenantFactory implements Tenant.Factory {
    @Inject
    private Provider<DaoFactory> daoFactoryProvider;
    @Inject
    private Engine.Factory engineFactory;
    @Inject
    private Configuracoes.Factory configuracoesFactory;
    @Inject
    @Livres
    private ExtraidorClientes extraidorClientesLivres;
    @Inject
    @Livres
    private Period intervaloMonitoracaoLivres;
    @Inject
    @Agendados
    private ExtraidorClientes extraidorClientesAgendados;
    @Inject
    @Agendados
    private Period intervaloMonitoracaoAgendados;
    @Inject
    private Estoque.Factory estoqueFactory;
    @Inject
    private GerenciadorAgentes.Factory gerenciadorAgentesFactory;
    @Inject
    private GerenciadorFatorK.Factory gerenciadorFatorKFactory;
    @Inject
    private GerenciadorLigacoes.Factory gerenciadorLigacoesFactory;
    @Inject
    private JDial.Factory jdialFactory;

    @Override
    public Tenant create(Campanha campanha) {
      return new DefaultTenant(daoFactoryProvider, engineFactory, configuracoesFactory,
          extraidorClientesLivres, intervaloMonitoracaoLivres, extraidorClientesAgendados,
          intervaloMonitoracaoAgendados, estoqueFactory, gerenciadorAgentesFactory,
          gerenciadorFatorKFactory, gerenciadorLigacoesFactory, jdialFactory, campanha);
    }
  }

  private static final Logger logger = getLogger(DefaultTenant.class);

  private final Provider<DaoFactory> daoFactoryProvider;
  private final Engine.Factory engineFactory;
  private final Configuracoes configuracoes;
  private final Estoque estoqueLivres;
  private final Estoque estoqueAgendados;
  private final GerenciadorAgentes gerenciadorAgentes;
  private final GerenciadorLigacoes gerenciadorLigacoes;
  private final GerenciadorFatorK gerenciadorFatorK;
  private final JDial jdial;

  private Engine engine;
  private Campanha campanha;

  DefaultTenant(Provider<DaoFactory> daoFactoryProvider, Engine.Factory engineFactory,
      Configuracoes.Factory configuracoesFactory, ExtraidorClientes extraidorClientesLivres,
      Period intervaloMonitoracaoLivres, ExtraidorClientes extraidorClientesAgendados,
      Period intervaloMonitoracaoAgendados, Estoque.Factory estoqueFactory,
      GerenciadorAgentes.Factory gerenciadorAgentesFactory,
      GerenciadorFatorK.Factory gerenciadorFatorKFactory,
      GerenciadorLigacoes.Factory gerenciadorLigacoesFactory, JDial.Factory jdialFactory,
      Campanha campanha) {
    this.campanha = campanha.clone();
    this.daoFactoryProvider = daoFactoryProvider;
    this.engineFactory = engineFactory;
    this.configuracoes = configuracoesFactory.create(campanha.getNome());
    this.estoqueLivres =
        estoqueFactory.create(this, extraidorClientesLivres, intervaloMonitoracaoLivres);
    this.estoqueAgendados =
        estoqueFactory.create(this, extraidorClientesAgendados, intervaloMonitoracaoAgendados);
    this.gerenciadorAgentes = gerenciadorAgentesFactory.create(this);
    this.gerenciadorFatorK = gerenciadorFatorKFactory.create(this);
    this.gerenciadorLigacoes = gerenciadorLigacoesFactory.create(this);
    this.jdial = jdialFactory.create(this);
  }

  @Override
  public Campanha getCampanha() {
    return campanha;
  }

  @Override
  public Configuracoes getConfiguracoes() {
    return configuracoes;
  }

  @Override
  public Estoque getEstoqueLivres() {
    return estoqueLivres;
  }

  @Override
  public Estoque getEstoqueAgendados() {
    return estoqueAgendados;
  }

  @Override
  public GerenciadorAgentes getGerenciadorAgentes() {
    return gerenciadorAgentes;
  }

  @Override
  public GerenciadorLigacoes getGerenciadorLigacoes() {
    return gerenciadorLigacoes;
  }

  @Override
  public GerenciadorFatorK getGerenciadorFatorK() {
    return gerenciadorFatorK;
  }

  @Override
  public JDial getJdial() {
    return jdial;
  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException("Already started!");
    logger.info("Starting {}...", this);
    configuracoes.start();
    estoqueLivres.start();
    estoqueAgendados.start();
    gerenciadorLigacoes.start();
    gerenciadorAgentes.start();
    gerenciadorFatorK.start();
    jdial.start();
    engine = engineFactory.create(this, Period.minutes(5), true, true);
    logger.info("Started {}.", this);
  }

  @Override
  public void stop() {
    if (engine == null)
      throw new IllegalStateException("Already stopped!");
    logger.info("Stopping {}...", this);
    jdial.stop();
    gerenciadorFatorK.stop();
    gerenciadorAgentes.stop();
    gerenciadorLigacoes.stop();
    estoqueAgendados.stop();
    estoqueLivres.stop();
    configuracoes.stop();
    engine.stop();
    engine = null;
    logger.info("Stopped {}.", this);
  }

  @Override
  public void run() {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      this.campanha = daoFactory.getCampanhaDao().procura(this.campanha.getId()).clone();
    } finally {
      daoFactory.close();
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("campanha", campanha).toString();
  }
}
