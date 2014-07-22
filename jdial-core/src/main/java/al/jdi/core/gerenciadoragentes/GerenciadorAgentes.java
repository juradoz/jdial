package al.jdi.core.gerenciadoragentes;

import al.jdi.common.Service;
import al.jdi.core.configuracoes.Configuracoes;

public interface GerenciadorAgentes extends Service {

  public interface Factory {
    GerenciadorAgentes create(Configuracoes configuracoes);
  }

  int getLivres();
}
