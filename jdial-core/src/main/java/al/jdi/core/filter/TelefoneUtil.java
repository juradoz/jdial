package al.jdi.core.filter;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Telefone;

public interface TelefoneUtil {
  boolean isUtil(Tenant tenant, Telefone telefone);
}
