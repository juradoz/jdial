package al.jdi.dao.beans;

import al.jdi.dao.model.AreaArea;
import al.jdi.dao.model.Telefone;

public interface AreaAreaDao extends Dao<AreaArea> {

  boolean isConurbada(Telefone telefone);

}
