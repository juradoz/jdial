package al.jdi.dao.beans;

import static org.hibernate.criterion.Projections.distinct;
import static org.hibernate.criterion.Projections.id;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.isNotEmpty;
import static org.hibernate.criterion.Restrictions.isNotNull;
import static org.hibernate.criterion.Restrictions.le;

import java.util.Collection;

import org.hibernate.Session;
import org.joda.time.DateTime;

import al.jdi.dao.AgenteDao;
import al.jdi.dao.model.Agente;
import al.jdi.dao.model.Mailing;

class AgenteDaoImpl extends DaoImpl<Agente> implements AgenteDao {

  AgenteDaoImpl(Session session) {
    super(session, Agente.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<Agente> agentesComAgenda(Collection<Mailing> mailings) {
    return getSession().createCriteria(Agente.class).createAlias("c.mailing", "m")
        .add(eq("m.ativo", true)).createAlias("agendamento.cliente", "c")
        .add(in("c.mailing", mailings)).add(isNotNull("c.telefone"))
        .add(isNotEmpty("c.agendamento")).createAlias("c.agendamento", "a")
        .add(isNotNull("a.agente")).add(le("a.agendamento", new DateTime()))
        .setProjection(distinct(id())).list();
  }

  @Override
  public Agente procura(String codigo) {
    return (Agente) getSession().createCriteria(Agente.class).add(eq("codigo", codigo))
        .uniqueResult();
  }
}
