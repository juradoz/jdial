package al.jdi.dao;

import al.jdi.dao.model.AreaArea;
import al.jdi.dao.model.Telefone;

public interface AreaAreaDao extends Dao<AreaArea> {

  boolean isConurbada(Telefone telefone);

}
