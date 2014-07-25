package al.jdi.core.filter;

import java.util.List;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Telefone;

public interface TelefoneFilter {
  List<Telefone> filter(Tenant tenant, List<Telefone> telefones);
}
