package al.jdi.core.filter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.model.Telefone;

@Alternative
class DefaultTelefoneFilter implements TelefoneFilter {

  private static final Logger logger = LoggerFactory.getLogger(DefaultTelefoneFilter.class);

  private final Set<TelefoneUtil> checkers;

  @Inject
  DefaultTelefoneFilter(Set<TelefoneUtil> checkers) {
    this.checkers = checkers;
  }

  @Override
  public List<Telefone> filter(List<Telefone> telefones) {
    List<Telefone> result = new LinkedList<Telefone>(telefones);
    for (Iterator<Telefone> it = result.iterator(); it.hasNext();) {
      Telefone telefone = it.next();
      for (TelefoneUtil checker : checkers) {
        if (!checker.isUtil(telefone)) {
          logger.info("Excluindo telefone inutil {} por {}", telefone, checker);
          it.remove();
          break;
        }
      }
    }
    return result;
  }
}