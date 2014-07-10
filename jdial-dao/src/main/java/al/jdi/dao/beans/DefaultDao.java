package al.jdi.dao.beans;

import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import al.jdi.dao.model.DaoObject;

class DefaultDao<T extends DaoObject> implements Dao<T> {
  private final Session session;

  private final Class<?> classe;

  DefaultDao(Session session, Class<?> classe) {
    this.session = session;
    this.classe = classe;
  }

  @Override
  public void adiciona(T t) {
    this.session.save(t);
  }

  @Override
  public void atualiza(T t) {
    t.getCriacaoModificacao().setModificacao(new DateTime());
    this.session.update(t);
  }

  Session getSession() {
    return session;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> listaTudo() {
    return this.session.createCriteria(this.classe).list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public T procura(Long id) {
    return (T) this.session.load(this.classe, id);
  }

  @Override
  public void remove(T u) {
    this.session.delete(u);
  }

  @SuppressWarnings("unchecked")
  @Override
  public T procura(String s) {
    Field field = null;
    Field[] declaredFields = classe.getDeclaredFields();
    for (Field f : declaredFields) {
      if (!f.isAnnotationPresent(CampoBusca.class))
        continue;
      field = f;
      break;
    }

    if (field == null)
      throw new IllegalArgumentException("Nenhum campo anotado em " + classe.getName());

    return (T) session.createCriteria(classe).add(Restrictions.eq(field.getName(), s))
        .uniqueResult();
  }
}
