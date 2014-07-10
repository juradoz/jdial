package al.jdi.dao.beans;

import al.jdi.dao.model.Usuario;

public interface UsuarioDao extends Dao<Usuario> {

  Usuario obtemAutenticado(Usuario u);

}
