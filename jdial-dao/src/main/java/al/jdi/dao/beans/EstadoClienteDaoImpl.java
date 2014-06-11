package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import org.hibernate.Session;

import al.jdi.dao.EstadoClienteDao;
import al.jdi.dao.model.EstadoCliente;

class EstadoClienteDaoImpl extends DaoImpl<EstadoCliente> implements EstadoClienteDao {

  EstadoClienteDaoImpl(Session session) {
    super(session, EstadoCliente.class);
  }

  @Override
  public EstadoCliente procura(String nome) {
    return (EstadoCliente) getSession().createCriteria(EstadoCliente.class).add(eq("nome", nome))
        .uniqueResult();
  }

}
