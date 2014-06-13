package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import org.hibernate.Session;

import al.jdi.dao.UsuarioDao;
import al.jdi.dao.model.Usuario;

class UsuarioDaoImpl extends DaoImpl<Usuario> implements UsuarioDao {

  UsuarioDaoImpl(Session session) {
    super(session, Usuario.class);
  }

  @Override
  public Usuario obtemAutenticado(Usuario u) {
    return (Usuario) getSession().createCriteria(Usuario.class).add(eq("login", u.getLogin()))
        .add(eq("senha", u.getSenha())).uniqueResult();
  }

}
