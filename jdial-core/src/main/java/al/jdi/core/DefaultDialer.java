package al.jdi.core;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdial.common.Engine;
import org.jdial.common.Service;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;

import al.jdi.core.DialerModule.DialerService;
import al.jdi.core.DialerModule.Versao;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.estoque.Estoque;
import al.jdi.core.estoque.EstoqueModule.Agendados;
import al.jdi.core.estoque.EstoqueModule.Livres;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoes;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.modelo.ModeloModule.DiscavelTsa;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Servico;

@DialerService
class DefaultDialer implements Service, Runnable {

  private final Logger logger;
  private final Configuracoes configuracoes;
  private final GerenciadorAgentes gerenciadorAgentes;
  private final GerenciadorLigacoes gerenciadorLigacoes;
  private final Estoque estoqueLivres;
  private final Estoque estoqueAgendados;
  private final Discavel.Factory discavelFactory;
  private final Engine.Factory engineFactory;
  private final Provider<DaoFactory> daoFactoryProvider;
  private final GerenciadorFatorK gerenciadorFatorK;
  private final TratadorEspecificoCliente tratadorEspecificoCliente;
  private final String versao;

  private Engine engine;

  @Inject
  DefaultDialer(Logger logger, Configuracoes configuracoes, Engine.Factory engineFactory,
      @Versao String versao, GerenciadorAgentes gerenciadorAgentes,
      GerenciadorLigacoes gerenciadorLigacoes, @Livres Estoque estoqueLivres,
      @Agendados Estoque estoqueAgendados, @DiscavelTsa Discavel.Factory discavelFactory,
      Provider<DaoFactory> daoFactoryProvider, TratadorEspecificoCliente tratadorEspecificoCliente,
      GerenciadorFatorK gerenciadorFatorK) {
    this.logger = logger;
    this.configuracoes = configuracoes;
    this.gerenciadorAgentes = gerenciadorAgentes;
    this.gerenciadorLigacoes = gerenciadorLigacoes;
    this.estoqueLivres = estoqueLivres;
    this.estoqueAgendados = estoqueAgendados;
    this.discavelFactory = discavelFactory;
    this.engineFactory = engineFactory;
    this.daoFactoryProvider = daoFactoryProvider;
    this.gerenciadorFatorK = gerenciadorFatorK;
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
    this.versao = versao;
    logger.info("Iniciando Dialer {}...", this.versao);

    limpaReservas(configuracoes, daoFactoryProvider, tratadorEspecificoCliente);
  }

  void limpaReservas(Configuracoes configuracoes, Provider<DaoFactory> daoFactoryProvider,
      TratadorEspecificoCliente tratadorEspecificoCliente) {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      Campanha campanha = daoFactory.getCampanhaDao().procura(configuracoes.getNomeCampanha());
      logger.debug("Limpando reservas para campanha {}...", campanha.getNome());
      DateTime inicio = new DateTime();
      daoFactory.beginTransaction();
      tratadorEspecificoCliente.obtemClienteDao(daoFactory).limpaReservas(campanha,
          configuracoes.getNomeBaseDados(), configuracoes.getNomeBase(),
          configuracoes.getOperador());
      daoFactory.commit();
      logger.info("Limpou reservas para campanha {}. Demorou {}ms", campanha.getNome(),
          new Duration(inicio, new DateTime()).getMillis());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } finally {
      if (daoFactory.hasTransaction())
        daoFactory.rollback();
      daoFactory.close();
    }
  }

  void rodada(DaoFactory daoFactory, Estoque estoque) {
    Campanha campanha = daoFactory.getCampanhaDao().procura(configuracoes.getNomeCampanha());
    logger.debug("Rodada {} para campanha {}", estoque, campanha.getNome());
    int livres = gerenciadorAgentes.getLivres();

    double fatorK = gerenciadorFatorK.getFatorK();

    int quantidadeLigacoes = gerenciadorLigacoes.getQuantidadeLigacoes();

    int quantidadeLigacoesNaoAtendidas = gerenciadorLigacoes.getQuantidadeLigacoesNaoAtendidas();

    int quantidade = ((int) (livres * fatorK) - quantidadeLigacoesNaoAtendidas);

    logger.info("Rodada {} Livres: {} * fatorK: {} - ligacoes: {} ({} total) = quantidade: {}",
        new Object[] {estoque, livres, fatorK, quantidadeLigacoesNaoAtendidas, quantidadeLigacoes,
            quantidade});

    if (quantidade <= 0)
      return;

    Collection<Cliente> clientesAgendados = estoque.obtemRegistros(quantidade);

    logger.debug("Obtive {} clientes de {}", clientesAgendados.size(), estoque);

    DateTime dataBanco = daoFactory.getDataBanco();
    Servico servico = campanha.getServico();

    for (Cliente cliente : clientesAgendados) {
      Discavel discavel = discavelFactory.create(cliente);
      Ligacao ligacao = new Ligacao.Builder(discavel).setInicio(dataBanco).build();
      DateTime inicio = new DateTime();
      gerenciadorLigacoes.disca(ligacao, servico);
      logger.debug("Discagem demorou {} ms", new Duration(inicio, new DateTime()).getMillis());
    }
  }

  @Override
  public void run() {
    if (!configuracoes.getSistemaAtivo()) {
      logger.warn("Sistema inativo");
      return;
    }
    logger.debug("Sistema ativo!");
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      rodada(daoFactory, estoqueAgendados);
      rodada(daoFactory, estoqueLivres);
    } finally {
      daoFactory.close();
    }

  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException("Already started");

    engine = engineFactory.create(this, configuracoes.getIntervaloEntreRodadas(), false);
    engine.start();
    logger
        .warn(
            "\n------------------------------------\nIniciado Dialer {}\n------------------------------------",
            versao);
  }

  @Override
  public void stop() {
    logger.debug("Encerrando Dialer {}...", versao);
    if (engine == null)
      throw new IllegalStateException("Already stopped");
    engine.stop();
    engine = null;
    limpaReservas(configuracoes, daoFactoryProvider, tratadorEspecificoCliente);
    logger.info("Encerrado Dialer {}", versao);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }
}
