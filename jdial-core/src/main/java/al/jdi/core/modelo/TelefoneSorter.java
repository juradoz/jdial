package al.jdi.core.modelo;

import java.util.List;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Telefone;

public interface TelefoneSorter {

  List<Telefone> sort(Tenant tenant, List<Telefone> telefones);

}
