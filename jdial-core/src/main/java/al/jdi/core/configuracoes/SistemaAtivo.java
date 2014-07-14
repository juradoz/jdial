package al.jdi.core.configuracoes;

import org.joda.time.DateTime;

public interface SistemaAtivo {

  public interface Factory {
    SistemaAtivo create(DefaultConfiguracoes configuracoes);
  }

  boolean isAtivo(DateTime agora);

}
