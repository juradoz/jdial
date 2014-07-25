package al.jdi.core.modelo;

import static ch.lambdaj.Lambda.on;
import static org.apache.commons.collections.ComparatorUtils.chainedComparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Telefone;
import ch.lambdaj.Lambda;
import ch.lambdaj.function.compare.ArgumentComparator;

class DefaultTelefoneSorter implements TelefoneSorter {

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Telefone> sort(Tenant tenant, List<Telefone> telefones) {
    if (tenant.getConfiguracoes().isPriorizaCelular()) {
      Collections.sort(telefones);
      return telefones;
    }

    ArgumentComparator byPrioridade = new ArgumentComparator(on(Telefone.class).getPrioridade());
    ArgumentComparator byId = new ArgumentComparator(on(Telefone.class).getId());
    Comparator orderBy = chainedComparator(byPrioridade, byId);
    return Lambda.sort(telefones, on(Telefone.class), orderBy);
  }
}
