package al.jdi.core.modelo;

import java.util.List;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Telefone;

public interface TelefoneSorter {

  List<Telefone> sort(Configuracoes configuracoes, List<Telefone> telefones);

}
