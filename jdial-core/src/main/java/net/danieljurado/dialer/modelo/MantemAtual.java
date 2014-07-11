package net.danieljurado.dialer.modelo;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import net.danieljurado.dialer.filter.FilterModule.ClienteSemTelefoneFilter;
import net.danieljurado.dialer.filter.FilterModule.SomenteCelularFilter;
import net.danieljurado.dialer.filter.TelefoneFilter;
import net.danieljurado.dialer.modelo.ModeloModule.ProvidenciaProximoTelefone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

class MantemAtual implements Providencia {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private final TelefoneSorter telefoneSorter;
  private final Providencia proximoTelefone;
  private final TelefoneFilter clienteSemTelefoneFilter;
  private final TelefoneFilter somenteCelularFilter;

  @Inject
  MantemAtual(TelefoneSorter telefoneSorter,
      @ProvidenciaProximoTelefone Providencia proximoTelefone,
      @ClienteSemTelefoneFilter TelefoneFilter clienteSemTelefoneFilter,
      @SomenteCelularFilter TelefoneFilter somenteCelularFilter) {
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
      return proximoTelefone.getTelefone(daoFactory, cliente);
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
