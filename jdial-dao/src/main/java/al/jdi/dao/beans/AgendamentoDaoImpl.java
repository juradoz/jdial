package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import org.hibernate.Session;

import al.jdi.dao.AgendamentoDao;
import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Cliente;

class AgendamentoDaoImpl extends DaoImpl<Agendamento> implements AgendamentoDao {

  AgendamentoDaoImpl(Session session) {
    super(session, Agendamento.class);
  }

  @Override
  public void adiciona(Agendamento agendamento) {
    super.adiciona(agendamento);
    adicionaCliente(agendamento);
    adicionaAgente(agendamento);
  }

  private void adicionaAgente(Agendamento agendamento) {
    if (agendamento.getAgente() == null)
      return;

    agendamento.getAgente().getAgendamento().add(agendamento);
    new AgenteDaoImpl(getSession()).atualiza(agendamento.getAgente());
  }

  private void adicionaCliente(Agendamento agendamento) {
    if (agendamento.getCliente().getAgendamento().isEmpty())
      agendamento.getCliente().getAgendamento().clear();
    agendamento.getCliente().getAgendamento().add(agendamento);
    new ClienteDaoImpl(getSession()).atualiza(agendamento.getCliente());
  }

  @Override
  public Agendamento procura(Cliente cliente) {
    return (Agendamento) getSession().createCriteria(Agendamento.class).add(eq("cliente", cliente))
        .uniqueResult();
  }
}
