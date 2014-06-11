package al.jdi.dao;

import al.jdi.dao.model.Campanha;

public interface CampanhaDao extends Dao<Campanha> {

  Campanha procura(String nome);

}
