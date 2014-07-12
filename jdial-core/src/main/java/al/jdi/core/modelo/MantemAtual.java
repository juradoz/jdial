package al.jdi.core.modelo;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;

import al.jdi.core.filter.FilterModule.ClienteSemTelefoneFilter;
import al.jdi.core.filter.FilterModule.SomenteCelularFilter;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.ModeloModule.ProvidenciaMantemAtual;
import al.jdi.core.modelo.ModeloModule.ProvidenciaProximoTelefone;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

@ProvidenciaMantemAtual
class MantemAtual implements Providencia {

  private final Logger logger;
  private final TelefoneSorter telefoneSorter;
  private final Instance<Providencia> proximoTelefone;
  private final TelefoneFilter clienteSemTelefoneFilter;
  private final TelefoneFilter somenteCelularFilter;

  @Inject
  MantemAtual(Logger logger, TelefoneSorter telefoneSorter,
      @ProvidenciaProximoTelefone Instance<Providencia> proximoTelefone,
      @ClienteSemTelefoneFilter TelefoneFilter clienteSemTelefoneFilter,
      @SomenteCelularFilter TelefoneFilter somenteCelularFilter) {
    this.logger = logger;
    this.telefoneSorter = telefoneSorter;
    this.proximoTelefone = proximoTelefone;
    this.clienteSemTelefoneFilter = clienteSemTelefoneFilter;
    this.somenteCelularFilter = somenteCelularFilter;
  }

  @Override
  public Telefone getTelefone(DaoFactory daoFactory, Cliente cliente) {
    List<Telefone> telefones = new LinkedList<Telefone>(cliente.getTelefones());
    if (telefones.isEmpty())
      throw new ClienteSemTelefoneException();

    telefones = clienteSemTelefoneFilter.filter(telefones);
    if (telefones.isEmpty())
      throw new ClienteSemTelefoneException();

    telefones = somenteCelularFilter.filter(telefones);
    if (telefones.isEmpty())
      throw new SomenteCelularException();

    telefones = telefoneSorter.sort(telefones);

    Telefone result = cliente.getTelefone();
    if (result == null) {
      cliente.setUltimoInicioRodadaTelefones(daoFactory.getDataBanco());
      daoFactory.getClienteDao().atualiza(cliente);
      result = new LinkedList<Telefone>(telefones).getFirst();
    }
    if (!telefones.contains(result)) {
      logger.warn("Telefone {} nao esta contido na relacao de telefones uteis cliente {}", result,
          cliente);
      return proximoTelefone.get().getTelefone(daoFactory, cliente);
    }
    logger.debug("Mantendo telefone atual para cliente {} Id {} DDD {} TEL {}", new Object[] {
        cliente, result, result.getDdd(), result.getTelefone()});
    return result;
  }

  @Override
  public Codigo getCodigo() {
    return Providencia.Codigo.MANTEM_ATUAL;
  }

}
