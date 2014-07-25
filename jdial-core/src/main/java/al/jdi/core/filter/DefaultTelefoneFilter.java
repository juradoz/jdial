package al.jdi.core.filter;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Telefone;

@Alternative
class DefaultTelefoneFilter implements TelefoneFilter {

  private static final Logger logger = getLogger(DefaultTelefoneFilter.class);

  private final Set<TelefoneUtil> checkers;

  @Inject
  DefaultTelefoneFilter(Set<TelefoneUtil> checkers) {
    this.checkers = checkers;
  }

  @Override
  public List<Telefone> filter(Configuracoes configuracoes, List<Telefone> telefones) {
    List<Telefone> result = new LinkedList<Telefone>(telefones);
    for (Iterator<Telefone> it = result.iterator(); it.hasNext();) {
      Telefone telefone = it.next();
      for (TelefoneUtil checker : checkers) {
        if (!checker.isUtil(configuracoes, telefone)) {
          logger.info("Excluindo telefone inutil {} por {}", telefone, checker);
          it.remove();
          break;
        }
      }
    }
    return result;
  }
}
