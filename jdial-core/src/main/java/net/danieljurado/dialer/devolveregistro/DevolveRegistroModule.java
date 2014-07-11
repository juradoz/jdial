package net.danieljurado.dialer.devolveregistro;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import javax.enterprise.inject.Produces;

import net.danieljurado.dialer.Service;
import net.danieljurado.dialer.modelo.Ligacao;

public class DevolveRegistroModule {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER})
  public @interface DevolvedorRegistroExecutorService {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER})
  public @interface DevolveRegistroService {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER})
  public @interface ThreadCountParameter {
  }

  private static final int THREAD_COUNT = 10;

  protected void configure() {
    bindConstant().annotatedWith(ThreadCountParameter.class).to(THREAD_COUNT);
    bind(DevolveRegistro.class).to(DefaultDevolveRegistro.class);
    bind(Service.class).annotatedWith(DevolveRegistroService.class)
        .to(DefaultDevolveRegistro.class);

    bind(FinalizadorCliente.class);
    bind(ModificadorResultado.class);
    bind(NotificadorCliente.class);

    Multibinder<ProcessoDevolucao> processosDevolucao =
        Multibinder.newSetBinder(binder(), ProcessoDevolucao.class);
    processosDevolucao.addBinding().to(ProcessaAgendamento.class);
    processosDevolucao.addBinding().to(ProcessaAsseguraExistenciaReserva.class);
    processosDevolucao.addBinding().to(ProcessaCiclaTelefone.class);
    processosDevolucao.addBinding().to(ProcessaFimDaFila.class);
    processosDevolucao.addBinding().to(ProcessaFinalizaCliente.class);
    processosDevolucao.addBinding().to(ProcessaFinalizaRegistroAtendido.class);
    processosDevolucao.addBinding().to(ProcessaIncrementaTentativa.class);
    processosDevolucao.addBinding().to(ProcessaIndisponibilizaTemporariamente.class);
    processosDevolucao.addBinding().to(ProcessaInsereHistorico.class);
    processosDevolucao.addBinding().to(ProcessaInutilizaTelefone.class);
    processosDevolucao.addBinding().to(ProcessaLimpaReserva.class);
    processosDevolucao.addBinding().to(ProcessaNotificaFimTentativa.class);
    processosDevolucao.addBinding().to(ProcessaRemoveTodosAgendamentos.class);
    processosDevolucao.addBinding().to(ProcessaSemTelefones.class);
    processosDevolucao.addBinding().to(ProcessaRetornaProvidencia.class);

    Multibinder<ModificadorResultadoFilter> modificadoresResultadoFilter =
        Multibinder.newSetBinder(binder(), ModificadorResultadoFilter.class);
    modificadoresResultadoFilter.addBinding().to(ModificadorResultadoAtendidoFake.class);
    modificadoresResultadoFilter.addBinding().to(ModificadorResultadoInexistenteFake.class);
    modificadoresResultadoFilter.addBinding().to(ModificadorResultadoSemAgentesFake.class);
    modificadoresResultadoFilter.addBinding().to(ModificadorResultadoUraReversa.class);
  }

  @Produces
  public ExecutorService getExecutorService() {
    return Executors.newFixedThreadPool(THREAD_COUNT, new ThreadFactory() {
      private int i;

      @Override
      public Thread newThread(Runnable arg0) {
        Thread t = new Thread(arg0, "DevolveRegistro-" + ++i);
        t.setDaemon(true);
        return t;
      }
    });
  }

  @Produces
  public BlockingQueue<Ligacao> getBlockingQueue() {
    return new LinkedBlockingQueue<Ligacao>();
  }

  @Produces
  public List<ProcessoDevolucao> getProcessosDevolucao(Set<ProcessoDevolucao> set) {
    LinkedList<ProcessoDevolucao> localList = new LinkedList<ProcessoDevolucao>(set);
    Collections.sort(localList);
    return Collections.unmodifiableList(localList);
  }

}
