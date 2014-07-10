package al.jdi.dao.beans;

import java.util.Collection;

import al.jdi.dao.model.Agente;
import al.jdi.dao.model.Mailing;

public interface AgenteDao extends Dao<Agente> {

  Collection<Agente> agentesComAgenda(Collection<Mailing> mailings);

}
