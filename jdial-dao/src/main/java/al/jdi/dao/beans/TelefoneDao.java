package al.jdi.dao.beans;

import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.Telefone;

public interface TelefoneDao extends Dao<Telefone> {

  int totalTentativas(boolean excluiCelular, Cliente cliente);

  Telefone procura(Mailing mailing, long chaveTelefone, String ddd, String telefone);

}
