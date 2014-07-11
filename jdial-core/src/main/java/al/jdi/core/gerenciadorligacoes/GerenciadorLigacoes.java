package al.jdi.core.gerenciadorligacoes;

import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.model.Servico;

public interface GerenciadorLigacoes {
  void disca(Ligacao ligacao, Servico servico);

  int getQuantidadeLigacoes();

  int getQuantidadeLigacoesNaoAtendidas();
}
