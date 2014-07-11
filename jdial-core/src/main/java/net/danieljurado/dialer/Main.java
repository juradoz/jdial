package net.danieljurado.dialer;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;

import net.danieljurado.dialer.DialerModule.DialerService;
import net.danieljurado.dialer.configuracoes.ConfiguracoesModule.ConfiguracoesService;
import net.danieljurado.dialer.devolveregistro.DevolveRegistroModule.DevolveRegistroService;
import net.danieljurado.dialer.estoque.EstoqueModule.Agendados;
import net.danieljurado.dialer.estoque.EstoqueModule.Livres;
import net.danieljurado.dialer.gerenciadoragentes.GerenciadorAgentesModule.GerenciadorAgentesService;
import net.danieljurado.dialer.gerenciadorfatork.GerenciadorFatorKModule.GerenciadorFatorKService;
import net.danieljurado.dialer.gerenciadorligacoes.GerenciadorLigacoesModule.GerenciadorLigacoesService;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jboss.weld.environment.se.events.ContainerInitialized;

public class Main {

  @ConfiguracoesService
  private static Service configuracoesService;
  @DevolveRegistroService
  private static Service devolveRegistroService;
  @Livres
  Service estoqueLivresService;
  @Agendados
  Service estoqueLivresAgendados;
  @GerenciadorAgentesService
  Service gerenciadorAgentesService;
  @GerenciadorLigacoesService
  Service gerenciadorLigacoesService;
  @GerenciadorFatorKService
  Service gerenciadorFatorKService;
  @DialerService
  Service dialerService;

  @PostConstruct
  public void start() {
    Runtime.getRuntime().addShutdownHook(
        new Thread(new ShutdownHook(devolveRegistroService, estoqueLivresService,
            estoqueLivresAgendados, gerenciadorAgentesService, gerenciadorLigacoesService,
            dialerService, configuracoesService, gerenciadorFatorKService), "EventoShutdown"));

    configuracoesService.start();
    devolveRegistroService.start();
    estoqueLivresService.start();
    estoqueLivresAgendados.start();
    gerenciadorLigacoesService.start();
    gerenciadorAgentesService.start();
    gerenciadorFatorKService.start();
    dialerService.start();
  }

  public void run(@Observes ContainerInitialized event) throws InterruptedException {
    ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);

    while (Thread.currentThread().isAlive())
      Thread.sleep(100);
  }

}
