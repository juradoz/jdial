package al.jdi.core.modelo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

public interface Providencia {

  @SuppressWarnings("serial")
  public static class NaoPodeReiniciarRodadaTelefoneException extends RuntimeException {
  }

  @SuppressWarnings("serial")
  public static class ClienteSemTelefoneException extends RuntimeException {
  }

  @SuppressWarnings("serial")
  public static class SomenteCelularException extends RuntimeException {
  }

  @SuppressWarnings("serial")
  public static class SemProximoTelefoneException extends RuntimeException {
  }

  public enum Codigo {
    MANTEM_ATUAL(1), PROXIMO_TELEFONE(2), INVALIDA_ATUAL_E_PROXIMO_TELEFONE(3);
    private final int codigo;

    private static class Mapeamento {
      private static final Map<Integer, Providencia.Codigo> values = Collections
          .synchronizedMap(new HashMap<Integer, Providencia.Codigo>());
    }

    private Codigo(int codigo) {
      this.codigo = codigo;
      Mapeamento.values.put(codigo, this);
    }

    public static Providencia.Codigo fromValue(int codigo) {
      Providencia.Codigo result = Mapeamento.values.get(codigo);
      if (result == null)
        return MANTEM_ATUAL;
      return result;
    }

    public int getCodigo() {
      return codigo;
    }
  }

  Providencia.Codigo getCodigo();

  Telefone getTelefone(Configuracoes configuracoes, DaoFactory daoFactory, Cliente cliente);

}
