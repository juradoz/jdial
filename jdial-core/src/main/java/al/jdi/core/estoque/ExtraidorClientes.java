package al.jdi.core.estoque;

import java.util.Collection;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;

public interface ExtraidorClientes {
  Collection<Cliente> extrai(Configuracoes configuracoes, DaoFactory daoFactory, int quantidade);
}
