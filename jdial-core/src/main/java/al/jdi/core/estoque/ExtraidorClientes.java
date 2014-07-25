package al.jdi.core.estoque;

import java.util.Collection;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;

public interface ExtraidorClientes {
  Collection<Cliente> extrai(Tenant tenant, DaoFactory daoFactory, int quantidade);
}
