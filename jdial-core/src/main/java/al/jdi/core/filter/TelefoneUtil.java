package al.jdi.core.filter;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Telefone;

public interface TelefoneUtil {
  boolean isUtil(Configuracoes configuracoes, Telefone telefone);
}
