package al.jdi.core.tenant;

import al.jdi.core.JDial;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.estoque.Estoque;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoes;
import al.jdi.dao.model.Campanha;

public interface Tenant {
  
  public interface Factory{
    Tenant create(Campanha campanha);
  }

  public abstract Campanha getCampanha();

  public abstract Configuracoes getConfiguracoes();

  public abstract Estoque getEstoqueLivres();

  public abstract Estoque getEstoqueAgendados();

  public abstract GerenciadorAgentes getGerenciadorAgentes();

  public abstract GerenciadorLigacoes getGerenciadorLigacoes();

  public abstract GerenciadorFatorK getGerenciadorFatorK();

  public abstract JDial getJdial();

}
