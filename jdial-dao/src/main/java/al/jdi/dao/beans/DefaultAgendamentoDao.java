package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Cliente;

class DefaultAgendamentoDao implements AgendamentoDao {

  private final DefaultDao<Agendamento> dao;

  DefaultAgendamentoDao(Session session) {
    this.dao = new DefaultDao<>(session, Agendamento.class);
  }

  @Override
  public void adiciona(Agendamento agendamento) {
    dao.adiciona(agendamento);
    adicionaCliente(agendamento);
    adicionaAgente(agendamento);
  }

  private void adicionaAgente(Agendamento agendamento) {
    if (agendamento.getAgente() == null)
      return;

    agendamento.getAgente().getAgendamento().add(agendamento);
    new DefaultAgenteDao(dao.getSession()).atualiza(agendamento.getAgente());
  }

  private void adicionaCliente(Agendamento agendamento) {
    if (agendamento.getCliente().getAgendamento().isEmpty())
      agendamento.getCliente().getAgendamento().clear();
    agendamento.getCliente().getAgendamento().add(agendamento);
    new DefaultClienteDao(dao.getSession()).atualiza(agendamento.getCliente());
  }

  @Override
  public Agendamento procura(Cliente cliente) {
    return (Agendamento) dao.getSession().createCriteria(Agendamento.class)
        .add(eq("cliente", cliente)).uniqueResult();
  }

  @Override
  public void atualiza(Agendamento t) {
    dao.atualiza(t);
  }

  @Override
  public List<Agendamento> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Agendamento procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Agendamento u) {
    dao.remove(u);
  }

  @Override
  public Agendamento procura(String s) {
    return procura(s);
  }
}
