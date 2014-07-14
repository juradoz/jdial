package al.jdi.core.gerenciadorligacoes;

import al.jdi.common.Service;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.model.Servico;

public interface GerenciadorLigacoes extends Service {

  public interface Factory {
    GerenciadorLigacoes create(Configuracoes configuracoes, GerenciadorFatorK gerenciadorFatorK);
  }

  void disca(Ligacao ligacao, Servico servico);

  int getQuantidadeLigacoes();

  int getQuantidadeLigacoesNaoAtendidas();
}
