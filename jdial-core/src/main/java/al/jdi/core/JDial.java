package al.jdi.core;

import al.jdi.common.Service;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.estoque.Estoque;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoes;

public interface JDial extends Service {
  public interface Factory {
    JDial create(Configuracoes configuracoes, GerenciadorAgentes gerenciadorAgentes,
        GerenciadorLigacoes gerenciadorLigacoes, Estoque estoqueLivres, Estoque estoqueAgendados,
        GerenciadorFatorK gerenciadorFatorK);
  }
}
