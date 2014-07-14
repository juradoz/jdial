package al.jdi.core.modelo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import al.jdi.core.filter.FilterModule.ClienteSemTelefoneFilter;
import al.jdi.core.filter.FilterModule.SomenteCelularFilter;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.ModeloModule.ProvidenciaMantemAtual;
import al.jdi.core.modelo.ModeloModule.ProvidenciaProximoTelefone;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

@ProvidenciaProximoTelefone
class ProximoTelefone implements Providencia {

  private final Logger logger;
  private final TelefoneSorter telefoneSorter;
  private final Instance<Providencia> mantemAtual;
  private final TelefoneFilter clienteSemTelefonesFilter;
  private final TelefoneFilter somenteCelulareFilter;

  @Inject
  ProximoTelefone(Logger logger, TelefoneSorter telefoneSorter,
      @ProvidenciaMantemAtual Instance<Providencia> mantemAtual,
      @ClienteSemTelefoneFilter TelefoneFilter clienteSemTelefoneFilter,
      @SomenteCelularFilter TelefoneFilter somenteCelulareFilter) {
    this.logger = logger;
    this.telefoneSorter = telefoneSorter;
    this.mantemAtual = mantemAtual;
    this.clienteSemTelefonesFilter = clienteSemTelefoneFilter;
    this.somenteCelulareFilter = somenteCelulareFilter;
  }

  @Override
  public Telefone getTelefone(DaoFactory daoFactory, Cliente cliente) {
    final Telefone result = cliente.getTelefone();
    if (result == null)
      return mantemAtual.get().getTelefone(daoFactory, cliente);

    List<Telefone> telefones = new LinkedList<Telefone>(cliente.getTelefones());
    if (telefones.isEmpty())
      throw new ClienteSemTelefoneException();

    telefones = clienteSemTelefonesFilter.filter(telefones);
    if (telefones.isEmpty())
      throw new ClienteSemTelefoneException();

    telefones = somenteCelulareFilter.filter(telefones);
    if (telefones.isEmpty())
      throw new SomenteCelularException();

    telefones = telefoneSorter.sort(telefones);

    Telefone telefoneDaVez = null;
    for (Iterator<Telefone> it = telefones.iterator(); it.hasNext();) {
      Telefone telefone = it.next();
      if (telefone.equals(result))
        if (it.hasNext()) {
          telefoneDaVez = it.next();
          break;
        }
    }

    if (telefoneDaVez == null) {
      if (cliente.getUltimoInicioRodadaTelefones() != null) {
        DateTime agora = daoFactory.getDataBanco();
        DateTime expiracao =
            cliente.getUltimoInicioRodadaTelefones().plus(
                configuracoes.getIntervaloMinimoNovaRodadaTelefone());
        if (expiracao.isAfter(agora)) {
          throw new NaoPodeReiniciarRodadaTelefoneException();
        }
      }
    }

    if (telefoneDaVez == null) {
      telefoneDaVez = new LinkedList<Telefone>(telefones).getFirst();
      if (telefoneDaVez.equals(cliente.getTelefone()))
        throw new SemProximoTelefoneException();
      cliente.setUltimoInicioRodadaTelefones(daoFactory.getDataBanco());
      daoFactory.getClienteDao().atualiza(cliente);
    }

    logger.debug("Proximo telefone para cliente {}: {}", new Object[] {cliente, telefoneDaVez});

    return telefoneDaVez;
  }

  @Override
  public Codigo getCodigo() {
    return Providencia.Codigo.PROXIMO_TELEFONE;
  }

}
