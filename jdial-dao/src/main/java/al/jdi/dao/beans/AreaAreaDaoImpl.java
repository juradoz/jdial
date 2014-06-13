package al.jdi.dao.beans;

import static org.apache.commons.lang.StringUtils.left;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.AreaAreaDao;
import al.jdi.dao.model.AreaArea;
import al.jdi.dao.model.Telefone;

public class AreaAreaDaoImpl extends DaoImpl<AreaArea> implements AreaAreaDao {

  AreaAreaDaoImpl(Session session) {
    super(session, AreaArea.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean isConurbada(Telefone telefone) {
    if (telefone.isCelular())
      return false;

    String ddd = telefone.getDdd();
    String prefixo = left(telefone.getTelefone(), 4);
    List<AreaArea> resultset =
        getSession().createCriteria(AreaArea.class).add(eq("conurbada", true)).add(eq("ddd", ddd))
            .add(eq("prefixo", prefixo)).list();
    return !resultset.isEmpty();
  }
}
