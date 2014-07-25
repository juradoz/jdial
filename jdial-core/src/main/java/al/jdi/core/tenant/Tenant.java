package al.jdi.core.tenant;

import al.jdi.common.Service;
import al.jdi.core.JDial;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.estoque.Estoque;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoes;
import al.jdi.dao.model.Campanha;

public interface Tenant extends Service {

  public interface Factory {
    Tenant create(Campanha campanha);
  }

  Campanha getCampanha();

  Configuracoes getConfiguracoes();

  Estoque getEstoqueLivres();

  Estoque getEstoqueAgendados();

  GerenciadorAgentes getGerenciadorAgentes();

  GerenciadorLigacoes getGerenciadorLigacoes();

  GerenciadorFatorK getGerenciadorFatorK();

  JDial getJdial();

}
