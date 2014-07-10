package al.jdi.dao.beans;

import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Cliente;

public interface AgendamentoDao extends Dao<Agendamento> {

  Agendamento procura(Cliente cliente);

}
