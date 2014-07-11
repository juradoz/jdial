package al.jdi.dao.beans;

import java.util.Collection;
import java.util.List;

import al.jdi.dao.model.Agente;
import al.jdi.dao.model.Mailing;

public interface AgenteDao extends Dao<Agente> {

  List<Agente> agentesComAgenda(Collection<Mailing> mailings);

}
