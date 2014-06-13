package al.jdi.dao.beans;

import java.util.List;

import org.hibernate.Session;
import org.joda.time.DateTime;

import al.jdi.dao.Dao;
import al.jdi.dao.model.DaoObject;

class DaoImpl<T extends DaoObject> implements Dao<T> {
  private final Session session;

  private final Class<?> classe;

  DaoImpl(Session session, Class<?> classe) {
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

  protected Session getSession() {
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
}
