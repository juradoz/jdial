package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import al.jdi.common.Service;
import al.jdi.core.devolveregistro.DevolveRegistroModule.DevolveRegistroService;
import al.jdi.core.devolveregistro.DevolveRegistroModule.ThreadCountParameter;
import al.jdi.core.devolveregistro.FinalizadorCliente.ClienteFinalizadoException;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

@Default
@Singleton
@DevolveRegistroService
class DefaultDevolveRegistro implements DevolveRegistro, Runnable, Service {

  static class Bean {
    private final Tenant tenant;
    private final Ligacao ligacao;

    public Bean(Tenant tenant, Ligacao ligacao) {
      this.tenant = tenant;
      this.ligacao = ligacao;
    }

    public Tenant getTenant() {
      return tenant;
    }

    public Ligacao getLigacao() {
      return ligacao;
    }
  }

  private static final Logger logger = getLogger(DefaultDevolveRegistro.class);

  private final Provider<DaoFactory> daoFactoryProvider;
  private final BlockingQueue<Bean> ligacoes;
  private final Provider<ExecutorService> executorServiceProvider;
  private final int threadCount;
  private final ModificadorResultado modificadorResultado;
  private final Instance<ProcessoDevolucao> processosDevolucao;

  private ExecutorService executorService;

  @Inject
  DefaultDevolveRegistro(Provider<DaoFactory> daoFactoryProvider,
      Provider<ExecutorService> executorServiceProvider, @ThreadCountParameter int threadCount,
      BlockingQueue<Bean> ligacoes, ModificadorResultado modificadorResultado,
      Instance<ProcessoDevolucao> processosDevolucao) {
    this.daoFactoryProvider = daoFactoryProvider;
    this.ligacoes = ligacoes;
    this.executorServiceProvider = executorServiceProvider;
    this.threadCount = threadCount;
    this.modificadorResultado = modificadorResultado;
    this.processosDevolucao = processosDevolucao;
    logger.debug("Iniciando {}...", this);
  }

  @Override
  public void devolveLigacao(Tenant tenant, Ligacao ligacao) {
    ligacoes.offer(new Bean(tenant, ligacao));
  }

  private void localDevolveLigacao(Tenant tenant, DaoFactory daoFactory, Ligacao ligacao) {

    Campanha campanha =
        daoFactory.getCampanhaDao().procura(
            ligacao.getDiscavel().getCliente().getMailing().getCampanha().getId());

    int motivoFinalizacao = ligacao.getMotivoFinalizacao();

    logger.debug("Procurando resultado {}", ligacao.getDiscavel().getCliente());
    ResultadoLigacao resultadoLigacao =
        daoFactory.getResultadoLigacaoDao().procura(motivoFinalizacao, campanha);

    if (resultadoLigacao == null) {
      logger.error("RESULTADO LIGACAO NAO EXISTE: {}", motivoFinalizacao);
      return;
    }

    resultadoLigacao = modificadorResultado.modifica(tenant, daoFactory, ligacao, resultadoLigacao);

    logger
        .info("Devolvendo com motivo {} {}", resultadoLigacao, ligacao.getDiscavel().getCliente());

    for (ProcessoDevolucao processo : processosDevolucao) {
      if (!processo.accept(tenant, daoFactory, ligacao, resultadoLigacao)) {
        continue;
      }

      try {
        if (!processo.executa(tenant, daoFactory, ligacao, resultadoLigacao)) {
          break;
        }
      } catch (ClienteFinalizadoException e) {
        logger.error(e.getMessage(), e);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

  @Override
  public void run() {
    logger.debug("Iniciando {}", Thread.currentThread().getName());
    while (Thread.currentThread().isAlive()) {
      Bean bean;
      try {
        bean = ligacoes.take();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      Long idCliente = bean.getLigacao().getDiscavel().getCliente().getId();
      Long idTelefoneOriginal =
          bean.getLigacao().getTelefoneOriginal() == null ? null : bean.getLigacao()
              .getTelefoneOriginal().getId();

      DaoFactory daoFactory = daoFactoryProvider.get();
      try {
        daoFactory.beginTransaction();
        Cliente cliente = daoFactory.getClienteDao().procura(idCliente);
        Telefone telefoneOriginal =
            idTelefoneOriginal == null ? null : daoFactory.getTelefoneDao().procura(
                idTelefoneOriginal);
        bean.getLigacao().setTelefoneOriginal(telefoneOriginal);
        bean.getLigacao().getDiscavel().setCliente(cliente);
        logger.info("Devolvendo ligacao motivo {} {}", bean.getLigacao().getMotivoFinalizacao(),
            cliente);
        localDevolveLigacao(bean.getTenant(), daoFactory, bean.getLigacao());
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
  }

  @Override
  public void stop() {
    logger.debug("Parando {}...", this);
    if (executorService == null)
      throw new IllegalStateException("Already stopped");
    executorService.shutdown();
    executorService = null;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
