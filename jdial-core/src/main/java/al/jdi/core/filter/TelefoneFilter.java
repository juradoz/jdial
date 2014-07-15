package al.jdi.core.filter;

import java.util.List;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Telefone;

public interface TelefoneFilter {
  List<Telefone> filter(Configuracoes configuracoes, List<Telefone> telefones);
}
