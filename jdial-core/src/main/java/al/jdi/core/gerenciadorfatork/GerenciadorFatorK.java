package al.jdi.core.gerenciadorfatork;

import al.jdi.common.Service;
import al.jdi.core.tenant.Tenant;

public interface GerenciadorFatorK extends Service {

  public interface Factory {
    GerenciadorFatorK create(Tenant tenant);
  }

  void chamadaIniciada();

  void chamadaAtendida();

  double getFatorK();
}
