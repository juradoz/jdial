package al.jdi.dao.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.DaoFactory;

public enum ProvidenciaTelefone {

  MANTEM_ATUAL(1) {
    @Override
    public Telefone getTelefone(DaoFactory daoFactory, Cliente cliente,
        Collection<Telefone> telefones, Period intervaloMinimoNovaRodadaTelefone) {
      Telefone result = cliente.getTelefone();
      if (result == null) {
        cliente.setUltimoInicioRodadaTelefones(daoFactory.getDataBanco());
        daoFactory.getClienteDao().atualiza(cliente);
        result = new LinkedList<Telefone>(telefones).getFirst();
      }
      if (!telefones.contains(result)) {
        logger.warn("Telefone {} nao esta contido na relacao de telefones uteis cliente {}",
            result, cliente);
        return PROXIMO_TELEFONE.getTelefone(daoFactory, cliente, telefones,
            intervaloMinimoNovaRodadaTelefone);
      }
      logger.debug("Mantendo telefone atual para cliente {} Id {} DDD {} TEL {}", new Object[] {
          cliente, result, result.getDdd(), result.getTelefone()});
      return result;
    }
  },
  PROXIMO_TELEFONE(2) {
    @Override
    public Telefone getTelefone(DaoFactory daoFactory, Cliente cliente,
        Collection<Telefone> telefones, Period intervaloMinimoNovaRodadaTelefone) {
      Telefone result = cliente.getTelefone();
      if (result == null)
        return MANTEM_ATUAL.getTelefone(daoFactory, cliente, telefones,
            intervaloMinimoNovaRodadaTelefone);

      Telefone telefoneDaVez = null;
      for (Iterator<Telefone> it = telefones.iterator(); it.hasNext();)
        if (it.next().equals(result))
          if (it.hasNext()) {
            telefoneDaVez = it.next();
            break;
          }

      if (telefoneDaVez == null)
        if (cliente.getUltimoInicioRodadaTelefones() != null
            && cliente.getUltimoInicioRodadaTelefones().plus(intervaloMinimoNovaRodadaTelefone)
                .isAfter(daoFactory.getDataBanco()))
          throw new NaoPodeReiniciarRodadaTelefoneException();

      result =
          telefoneDaVez != null ? telefoneDaVez : new LinkedList<Telefone>(telefones).getFirst();
      if (result.equals(cliente.getTelefone()))
        throw new SemProximoTelefoneException();

      cliente.setUltimoInicioRodadaTelefones(daoFactory.getDataBanco());
      daoFactory.getClienteDao().atualiza(cliente);

      logger.debug("Proximo telefone para cliente {}: Id {} DDD {} TEL {}", new Object[] {cliente,
          result, result.getDdd(), result.getTelefone()});
      return result;
    }
  },
  INVALIDA_ATUAL_E_PROXIMO_TELEFONE(3) {
    @Override
    public Telefone getTelefone(DaoFactory daoFactory, Cliente cliente,
        Collection<Telefone> telefones, Period intervaloMinimoNovaRodadaTelefone) {
      logger.debug("Invalida atual e proximo para cliente {}...", cliente);
      Telefone result = cliente.getTelefone();
      if (result == null) {
        logger.debug("Cliente {} nao possuia telefone atual", cliente);
        return PROXIMO_TELEFONE.getTelefone(daoFactory, cliente, telefones,
            intervaloMinimoNovaRodadaTelefone);
      }
      logger.debug("Vai efetivamente invalidar para cliente {} Id {} DDD {} TEL {}", new Object[] {
          cliente, result, result.getDdd(), result.getTelefone()});
      result.setUtil(false);
      daoFactory.getTelefoneDao().atualiza(result);
      daoFactory.getClienteDao().atualiza(cliente);
      return PROXIMO_TELEFONE.getTelefone(daoFactory, cliente, telefones,
          intervaloMinimoNovaRodadaTelefone);
    }
  };

  private static class Mapeamento {
    private static final Map<Integer, ProvidenciaTelefone> values = Collections
        .synchronizedMap(new HashMap<Integer, ProvidenciaTelefone>());
  }

  @SuppressWarnings("serial")
  public static class NaoPodeReiniciarRodadaTelefoneException extends RuntimeException {
  }

  @SuppressWarnings("serial")
  public static class SemProximoTelefoneException extends RuntimeException {
  }

  private final int codigo;

  private static final Logger logger = LoggerFactory.getLogger(ProvidenciaTelefone.class);

  public static ProvidenciaTelefone fromValue(int codigo) {
    ProvidenciaTelefone result = Mapeamento.values.get(codigo);
    if (result == null)
      return MANTEM_ATUAL;
    return result;
  }

  private ProvidenciaTelefone(int codigo) {
    this.codigo = codigo;
    Mapeamento.values.put(codigo, this);
  }

  public int getCodigo() {
    return codigo;
  }

  public abstract Telefone getTelefone(DaoFactory daoFactory, Cliente cliente,
      Collection<Telefone> telefones, Period intervaloMinimoNovaRodadaTelefone);

}
