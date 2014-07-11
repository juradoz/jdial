package al.jdi.dao.beans;

import static org.hibernate.criterion.Projections.distinct;
import static org.hibernate.criterion.Projections.id;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.isNotEmpty;
import static org.hibernate.criterion.Restrictions.isNotNull;
import static org.hibernate.criterion.Restrictions.le;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.joda.time.DateTime;

import al.jdi.dao.model.Agente;
import al.jdi.dao.model.Mailing;

class DefaultAgenteDao implements AgenteDao {

  private final DefaultDao<Agente> dao;

  public DefaultAgenteDao(Session session) {
    this(new DefaultDao<Agente>(session, Agente.class));
  }

  DefaultAgenteDao(DefaultDao<Agente> dao) {
    this.dao = dao;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<Agente> agentesComAgenda(Collection<Mailing> mailings) {
    return dao.getSession().createCriteria(Agente.class).createAlias("c.mailing", "m")
        .add(eq("m.ativo", true)).createAlias("agendamento.cliente", "c")
        .add(in("c.mailing", mailings)).add(isNotNull("c.telefone"))
        .add(isNotEmpty("c.agendamento")).createAlias("c.agendamento", "a")
        .add(isNotNull("a.agente")).add(le("a.agendamento", new DateTime()))
        .setProjection(distinct(id())).list();
  }

  @Override
  public void adiciona(Agente t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(Agente t) {
    dao.atualiza(t);
  }

  @Override
  public List<Agente> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Agente procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Agente u) {
    dao.remove(u);
  }

  @Override
  public Agente procura(String s) {
    return dao.procura(s);
  }

}
