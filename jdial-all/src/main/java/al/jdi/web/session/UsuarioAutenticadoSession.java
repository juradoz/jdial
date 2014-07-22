package al.jdi.web.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import al.jdi.dao.model.Usuario;

@SessionScoped
public class UsuarioAutenticadoSession implements Serializable {
  private static final long serialVersionUID = 1L;

  private Usuario usuario;

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
}
