package al.jdi.dao;

import java.util.Collection;

import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.Telefone;

public interface TelefoneDao extends Dao<Telefone> {

  Collection<Telefone> telefonesUteis(boolean excluiCelular, Cliente cliente);

  Collection<Telefone> telefonesUteis(Cliente cliente);

  int totalTentativas(boolean excluiCelular, Cliente cliente);

  Telefone procura(Mailing mailing, long chaveTelefone, String ddd, String telefone);

}
