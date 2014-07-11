package al.jdi.core.configuracoes;

import org.joda.time.DateTime;

public interface SistemaAtivo {

  public interface Factory {
    SistemaAtivo create(ConfiguracoesImpl configuracoes);
  }

  boolean isAtivo(DateTime agora);

}
