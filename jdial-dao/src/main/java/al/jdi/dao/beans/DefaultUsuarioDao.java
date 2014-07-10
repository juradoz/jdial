package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Usuario;

class DefaultUsuarioDao implements UsuarioDao {

  private final DefaultDao<Usuario> dao;

  DefaultUsuarioDao(Session session) {
    this.dao = new DefaultDao<>(session, Usuario.class);
  }

  @Override
  public Usuario obtemAutenticado(Usuario u) {
    Usuario usuario =
        (Usuario) dao.getSession().createCriteria(Usuario.class).add(eq("login", u.getLogin()))
            .uniqueResult();

    if (usuario == null)
      return null;

    if (!usuario.verificaSenha(u.getSenha()))
      return null;

    return usuario;
  }

  @Override
  public void adiciona(Usuario t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(Usuario t) {
    dao.atualiza(t);
  }

  @Override
  public List<Usuario> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Usuario procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Usuario u) {
    dao.remove(u);
  }

  @Override
  public Usuario procura(String s) {
    return dao.procura(s);
  }

}
