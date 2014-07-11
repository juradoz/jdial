package net.danieljurado.dialer.gerenciadorligacoes;

import net.danieljurado.dialer.modelo.Ligacao;
import al.jdi.dao.model.Servico;

public interface GerenciadorLigacoes {
  void disca(Ligacao ligacao, Servico servico);

  int getQuantidadeLigacoes();

  int getQuantidadeLigacoesNaoAtendidas();
}
