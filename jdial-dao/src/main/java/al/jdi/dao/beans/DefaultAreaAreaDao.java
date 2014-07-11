package al.jdi.dao.beans;

import static org.apache.commons.lang3.StringUtils.left;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.AreaArea;
import al.jdi.dao.model.Telefone;

class DefaultAreaAreaDao implements AreaAreaDao {

  private final DefaultDao<AreaArea> dao;

  public DefaultAreaAreaDao(Session session) {
    this(new DefaultDao<AreaArea>(session, AreaArea.class));
  }

  DefaultAreaAreaDao(DefaultDao<AreaArea> dao) {
    this.dao = dao;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean isConurbada(Telefone telefone) {
    if (telefone.isCelular())
      return false;

    String ddd = telefone.getDdd();
    String prefixo = left(telefone.getTelefone(), 4);
    List<AreaArea> resultset =
        dao.getSession().createCriteria(AreaArea.class).add(eq("conurbada", true))
            .add(eq("ddd", ddd)).add(eq("prefixo", prefixo)).list();
    return !resultset.isEmpty();
  }

  @Override
  public void adiciona(AreaArea t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(AreaArea t) {
    dao.atualiza(t);
  }

  @Override
  public List<AreaArea> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public AreaArea procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(AreaArea u) {
    dao.remove(u);
  }

  @Override
  public AreaArea procura(String s) {
    return dao.procura(s);
  }
}
