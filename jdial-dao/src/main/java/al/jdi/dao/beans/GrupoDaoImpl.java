package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import org.hibernate.Session;

import al.jdi.dao.GrupoDao;
import al.jdi.dao.model.Grupo;

class GrupoDaoImpl extends DaoImpl<Grupo> implements GrupoDao {

  GrupoDaoImpl(Session session) {
    super(session, Grupo.class);
  }

  @Override
  public Grupo procura(String codigo) {
    return (Grupo) getSession().createCriteria(Grupo.class).add(eq("codigo", codigo))
        .uniqueResult();
  }

}
