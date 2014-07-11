package net.danieljurado.dialer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.configuracoes.ConfiguracoesModule;
import net.danieljurado.dialer.devolveregistro.DevolveRegistroModule;
import net.danieljurado.dialer.estoque.Estoque;
import net.danieljurado.dialer.estoque.EstoqueModule;
import net.danieljurado.dialer.estoque.EstoqueModule.Agendados;
import net.danieljurado.dialer.estoque.EstoqueModule.Livres;
import net.danieljurado.dialer.filter.FilterModule;
import net.danieljurado.dialer.gerenciadoragentes.GerenciadorAgentes;
import net.danieljurado.dialer.gerenciadoragentes.GerenciadorAgentesModule;
import net.danieljurado.dialer.gerenciadorfatork.GerenciadorFatorKModule;
import net.danieljurado.dialer.gerenciadorligacoes.GerenciadorLigacoes;
import net.danieljurado.dialer.gerenciadorligacoes.GerenciadorLigacoesModule;
import net.danieljurado.dialer.modelo.Discavel;
import net.danieljurado.dialer.modelo.ModeloModule;
import net.danieljurado.dialer.modelo.ModeloModule.DiscavelTsa;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoClienteModule;

public class DialerModule {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER})
  public @interface DialerService {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PARAMETER})
  public @interface VersaoParameter {
  }

  private static final String VERSAO = "4.2.2";

  private final String nomeCampanha;

  public DialerModule(String nomeCampanha) {
    this.nomeCampanha = nomeCampanha;
  }

  protected void configure() {
    install(new DaoModule());
    install(new EngineModule());
    install(new ConfiguracoesModule(nomeCampanha));
    install(new CtiManagerModuleFromFile("dialer.properties"));
    install(new DialerCtiManagerModule());
    install(new FilterModule());
    install(new DevolveRegistroModule());
    install(new ModeloModule());
    install(new TratadorEspecificoClienteModule());
    install(new EstoqueModule());
    install(new GerenciadorAgentesModule());
    install(new GerenciadorLigacoesModule());
    install(new GerenciadorFatorKModule());

    bindConstant().annotatedWith(VersaoParameter.class).to(VERSAO);

    bind(Service.class).annotatedWith(DialerService.class).to(DialerImpl.class);
  }
}
