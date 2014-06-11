package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.ServicoDao;
import al.jdi.dao.model.Servico;

class ServicoDaoImpl extends DaoImpl<Servico> implements ServicoDao {

  ServicoDaoImpl(Session session) {
    super(session, Servico.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Servico> monitoraveisQrf() {
    return getSession().createCriteria(Servico.class).add(eq("monitoraQrf", true)).list();
  }

  @Override
  public Servico procura(String nome) {
    return (Servico) getSession().createCriteria(Servico.class).add(eq("nome", nome))
        .uniqueResult();
  }

}
