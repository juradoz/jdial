package net.danieljurado.dialer;

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
import org.jboss.weld.injection.producer.Injector;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);

    String campanha = args[0];

    Injector injector = Guice.createInjector(new DialerModule(campanha));

    Service configuracoesService =
        injector.getInstance(Key.get(Service.class, ConfiguracoesService.class));
    Service devolveRegistroService =
        injector.getInstance(Key.get(Service.class, DevolveRegistroService.class));
    Service estoqueLivresService = injector.getInstance(Key.get(Service.class, Livres.class));
    Service estoqueLivresAgendados = injector.getInstance(Key.get(Service.class, Agendados.class));
    Service gerenciadorAgentesService =
        injector.getInstance(Key.get(Service.class, GerenciadorAgentesService.class));
    Service gerenciadorLigacoesService =
        injector.getInstance(Key.get(Service.class, GerenciadorLigacoesService.class));
    Service gerenciadorFatorKService =
        injector.getInstance(Key.get(Service.class, GerenciadorFatorKService.class));
    Service dialerService = injector.getInstance(Key.get(Service.class, DialerService.class));

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

}
