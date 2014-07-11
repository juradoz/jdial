package al.jdi.core.modelo;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

class InvalidaAtualEProximoTelefone implements Providencia {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private final ProximoTelefone proximoTelefone;

  @Inject
  InvalidaAtualEProximoTelefone(ProximoTelefone proximoTelefone) {
    this.proximoTelefone = proximoTelefone;
  }

  @Override
  public Telefone getTelefone(DaoFactory daoFactory, Cliente cliente) {
    logger.debug("Invalida atual e proximo para cliente {}...", cliente);
    Telefone result = cliente.getTelefone();
    if (result == null) {
      logger.debug("Cliente {} nao possuia telefone atual", cliente);
      return proximoTelefone.getTelefone(daoFactory, cliente);
    }
    logger.debug("Vai efetivamente invalidar para cliente {} Id {} DDD {} TEL {}", new Object[] {
        cliente, result, result.getDdd(), result.getTelefone()});
    result.setUtil(false);
    daoFactory.getTelefoneDao().atualiza(result);
    daoFactory.getClienteDao().atualiza(cliente);
    return proximoTelefone.getTelefone(daoFactory, cliente);
  }

  @Override
  public Codigo getCodigo() {
    return Providencia.Codigo.INVALIDA_ATUAL_E_PROXIMO_TELEFONE;
  }

}
