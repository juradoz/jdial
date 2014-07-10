package al.jdi.dao.beans;

import java.util.List;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Definicao;

public interface DefinicaoDao extends Dao<Definicao> {

  List<Definicao> listaTudo(Campanha campanha);

  Definicao procura(Campanha campanha, String propriedade);

}
