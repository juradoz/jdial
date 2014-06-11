package al.jdi.dao;

import al.jdi.dao.model.Usuario;

public interface UsuarioDao extends Dao<Usuario> {

  Usuario obtemAutenticado(Usuario u);

}
