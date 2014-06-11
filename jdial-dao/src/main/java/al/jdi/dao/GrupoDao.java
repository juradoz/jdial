package al.jdi.dao;

import al.jdi.dao.model.Grupo;

public interface GrupoDao extends Dao<Grupo> {

  Grupo procura(String codigo);

}
