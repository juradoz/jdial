package al.jdi.core.estoque;

import java.util.Collection;

import al.jdi.dao.model.Cliente;

public interface Estoque {

  boolean contemCliente(Cliente cliente);

  Collection<Cliente> obtemRegistros(int quantidade);
}
