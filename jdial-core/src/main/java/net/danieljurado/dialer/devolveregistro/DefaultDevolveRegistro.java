package net.danieljurado.dialer.devolveregistro;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Provider;

import net.danieljurado.dialer.Service;
import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.devolveregistro.DevolveRegistroModule.ThreadCountParameter;
import net.danieljurado.dialer.modelo.Ligacao;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class DefaultDevolveRegistro implements DevolveRegistro, Runnable, Service {

  private static final Logger logger = LoggerFactory.getLogger(DefaultDevolveRegistro.class);

  private final Provider<DaoFactory> daoFactoryProvider;
  private final Configuracoes configuracoes;
  private final BlockingQueue<Ligacao> ligacoes;
  private final Provider<ExecutorService> executorServiceProvider;
  private final int threadCount;
  private final ModificadorResultado modificadorResultado;
  private final List<ProcessoDevolucao> processosDevolucao;

  private ExecutorService executorService;

  @Inject
  DefaultDevolveRegistro(Provider<DaoFactory> daoFactoryProvider, Configuracoes configuracoes,
      Provider<ExecutorService> executorServiceProvider, @ThreadCountParameter int threadCount,
      BlockingQueue<Ligacao> ligacoes, ModificadorResultado modificadorResultado,
      List<ProcessoDevolucao> processosDevolucao) {
    this.daoFactoryProvider = daoFactoryProvider;
    this.configuracoes = configuracoes;
    this.ligacoes = ligacoes;
    this.executorServiceProvider = executorServiceProvider;
    this.threadCount = threadCount;
    this.modificadorResultado = modificadorResultado;
    this.processosDevolucao = processosDevolucao;
    logger.debug("Iniciando {}...", this);
  }

  @Override
  public void devolveLigacao(Ligacao ligacao) {
    ligacoes.offer(ligacao);
  }

  private void localDevolveLigacao(DaoFactory daoFactory, Ligacao ligacao, Cliente cliente) {

    Campanha campanha = daoFactory.getCampanhaDao().procura(configuracoes.getNomeCampanha());

    int motivoFinalizacao = ligacao.getMotivoFinalizacao();

    logger.debug("Procurando resultado {}", cliente);
    ResultadoLigacao resultadoLigacao =
        daoFactory.getResultadoLigacaoDao().procura(motivoFinalizacao, campanha);

    if (resultadoLigacao == null) {
      logger.error("RESULTADO LIGACAO NAO EXISTE: {}", motivoFinalizacao);
      return;
    }

    resultadoLigacao =
        modificadorResultado.modifica(daoFactory, resultadoLigacao, ligacao, cliente, campanha);

    logger.info("Devolvendo com motivo {} {}", resultadoLigacao, cliente);

    for (ProcessoDevolucao processo : processosDevolucao) {
      if (!processo.accept(ligacao, cliente, resultadoLigacao, daoFactory)) {
        continue;
      }

      try {
        if (!processo.executa(ligacao, cliente, resultadoLigacao, daoFactory)) {
          break;
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

  @Override
  public void run() {
    logger.debug("Iniciando {}", Thread.currentThread().getName());
    while (Thread.currentThread().isAlive()) {
      Ligacao ligacao;
      try {
        ligacao = ligacoes.take();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      Long idCliente = ligacao.getDiscavel().getCliente().getId();

      DaoFactory daoFactory = daoFactoryProvider.get();
      try {
        daoFactory.beginTransaction();
        Cliente cliente = daoFactory.getClienteDao().procura(idCliente);
        logger.info("Devolvendo ligacao motivo {} {}", ligacao.getMotivoFinalizacao(), cliente);
        localDevolveLigacao(daoFactory, ligacao, cliente);
        daoFactory.commit();
      } catch (Throwable e) {
        logger.error("Erro na devolucao de {}:{}", new Object[] {idCliente, e.getMessage()}, e);
      } finally {
        if (daoFactory.hasTransaction())
          daoFactory.rollback();
        daoFactory.close();
      }
    }
  }

  @Override
  public void start() {
    if (executorService != null)
      throw new IllegalStateException();
    executorService = executorServiceProvider.get();
    for (int i = 0; i < threadCount; i++)
      executorService.execute(this);
    logger.info("Iniciado {}", this);
  }

  @Override
  public void stop() {
    logger.debug("Encerrando {}...", this);
    if (executorService == null)
      throw new IllegalStateException();
    executorService.shutdown();
    executorService = null;
    logger.info("Encerrado {}", this);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
