package al.jdi.core.filter;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import al.jdi.dao.model.Telefone;

class CelularChecker {
  public boolean isCelular(Telefone telefone) {
    if (isBlank(telefone.getTelefone()))
      return false;
    return asList('9', '8', '7', '6').contains(telefone.getTelefone().charAt(0));
  }

}
