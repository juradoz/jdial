package al.jdi.core.gerenciadorligacoes;

import al.jdi.common.Service;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Servico;

public interface GerenciadorLigacoes extends Service {

  public interface Factory {
    GerenciadorLigacoes create(Tenant tenant);
  }

  void disca(Ligacao ligacao, Servico servico);

  int getQuantidadeLigacoes();

  int getQuantidadeLigacoesNaoAtendidas();
}
