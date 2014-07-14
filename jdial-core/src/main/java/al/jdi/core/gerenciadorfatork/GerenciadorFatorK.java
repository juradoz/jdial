package al.jdi.core.gerenciadorfatork;

import al.jdi.common.Service;
import al.jdi.core.configuracoes.Configuracoes;

public interface GerenciadorFatorK extends Service {

  public interface Factory {
    GerenciadorFatorK create(Configuracoes configuracoes);
  }

  void chamadaIniciada();

  void chamadaAtendida();

  double getFatorK();
}
