package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaNotificaFimTentativa implements ProcessoDevolucao {

  private static final Logger logger = getLogger(ProcessaNotificaFimTentativa.class);

  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;

  @Inject
  ProcessaNotificaFimTentativa(TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory) {
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 14;
  }

  @Override
  public boolean accept(Tenant tenant, Ligacao ligacao, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    if (!resultadoLigacao.isNotificaFimTentativa()) {
      logger.info("Nao vai notificar fim tentativa motivo {} {}", resultadoLigacao, ligacao
          .getDiscavel().getCliente());
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Tenant tenant, Ligacao ligacao, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    logger.info("Vai notificar fim tentativa motivo {} {}", resultadoLigacao, cliente);
    tratadorEspecificoClienteFactory.create(tenant, daoFactory).notificaFimTentativa(tenant,
        ligacao, cliente, daoFactory.getDataBanco(), ligacao.getTelefoneOriginal(),
        resultadoLigacao, ligacao.isInutilizaComMotivoDiferenciado());
    return true;
  }

}
