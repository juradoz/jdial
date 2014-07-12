package al.jdi.core;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.jdi.core.DialerModule.DialerService;
import al.jdi.core.configuracoes.ConfiguracoesModule.ConfiguracoesService;
import al.jdi.core.devolveregistro.DevolveRegistroModule.DevolveRegistroService;
import al.jdi.core.estoque.EstoqueModule.Agendados;
import al.jdi.core.estoque.EstoqueModule.Livres;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentesModule.GerenciadorAgentesService;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorKModule.GerenciadorFatorKService;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoesModule.GerenciadorLigacoesService;

class Main {

  private final Service configuracoesService;
  private final Service devolveRegistroService;
  private final Service estoqueLivresService;
  private final Service estoqueLivresAgendados;
  private final Service gerenciadorAgentesService;
  private final Service gerenciadorLigacoesService;
  private final Service gerenciadorFatorKService;
  private final Service dialerService;

  @Inject
  Main(@ConfiguracoesService Service configuracoesService,
      @DevolveRegistroService Service devolveRegistroService, @Livres Service estoqueLivresService,
      @Agendados Service estoqueLivresAgendados,
      @GerenciadorAgentesService Service gerenciadorAgentesService,
      @GerenciadorLigacoesService Service gerenciadorLigacoesService,
      @GerenciadorFatorKService Service gerenciadorFatorKService,
      @DialerService Service dialerService) {
    this.configuracoesService = configuracoesService;
    this.devolveRegistroService = devolveRegistroService;
    this.estoqueLivresService = estoqueLivresService;
    this.estoqueLivresAgendados = estoqueLivresAgendados;
    this.gerenciadorAgentesService = gerenciadorAgentesService;
    this.gerenciadorLigacoesService = gerenciadorLigacoesService;
    this.gerenciadorFatorKService = gerenciadorFatorKService;
    this.dialerService = dialerService;
  }

  @PostConstruct
  public void start() {
    Runtime.getRuntime().addShutdownHook(
        new Thread(new ShutdownHook(devolveRegistroService, estoqueLivresService,
            estoqueLivresAgendados, gerenciadorAgentesService, gerenciadorLigacoesService,
            dialerService, configuracoesService, gerenciadorFatorKService), "EventoShutdown"));

    // configuracoesService.start();
    // devolveRegistroService.start();
    // estoqueLivresService.start();
    // estoqueLivresAgendados.start();
    // gerenciadorLigacoesService.start();
    // gerenciadorAgentesService.start();
    // gerenciadorFatorKService.start();
    // dialerService.start();
  }

  public void run(@Observes ContainerInitialized event) throws InterruptedException {
    ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);

    while (Thread.currentThread().isAlive())
      Thread.sleep(100);
  }

}
