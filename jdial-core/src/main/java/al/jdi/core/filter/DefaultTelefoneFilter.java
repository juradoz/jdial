package al.jdi.core.filter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.slf4j.Logger;

import al.jdi.dao.model.Telefone;

@Alternative
class DefaultTelefoneFilter implements TelefoneFilter {

  private final Logger logger;
  private final Set<TelefoneUtil> checkers;

  @Inject
  DefaultTelefoneFilter(Logger logger, Set<TelefoneUtil> checkers) {
    this.logger = logger;
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
