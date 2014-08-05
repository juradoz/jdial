package al.jdi.core.modelo;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.apache.commons.collections.ComparatorUtils.chainedComparator;
import static org.hamcrest.CoreMatchers.is;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Telefone;
import ch.lambdaj.Lambda;
import ch.lambdaj.function.compare.ArgumentComparator;

class DefaultTelefoneSorter implements TelefoneSorter {

  @Override
  public List<Telefone> sort(Tenant tenant, List<Telefone> telefones) {
    if (tenant.getConfiguracoes().isPriorizaCelular())
      return celularSort(telefones);

    return defaultSort(telefones);
  }

  private List<Telefone> celularSort(List<Telefone> telefones) {
    List<Telefone> celulares =
        defaultSort(filter(having(on(Telefone.class).isCelular(), is(true)), telefones));
    List<Telefone> fixos =
        defaultSort(filter(having(on(Telefone.class).isCelular(), is(false)), telefones));
    LinkedList<Telefone> result = new LinkedList<Telefone>();
    result.addAll(celulares);
    result.addAll(fixos);
    return result;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private List<Telefone> defaultSort(List<Telefone> telefones) {
    ArgumentComparator byPrioridade = new ArgumentComparator(on(Telefone.class).getPrioridade());
    ArgumentComparator byId = new ArgumentComparator(on(Telefone.class).getId());
    Comparator orderBy = chainedComparator(byPrioridade, byId);
    return Lambda.sort(telefones, on(Telefone.class), orderBy);
  }

}
