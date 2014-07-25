package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaRemoveTodosAgendamentos implements ProcessoDevolucao {

  private static final Logger logger = getLogger(ProcessaRemoveTodosAgendamentos.class);

  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;

  @Inject
  ProcessaRemoveTodosAgendamentos(TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory) {
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
  }

  @Override
  public boolean accept(Tenant tenant, DaoFactory daoFactory, Ligacao ligacao,
      ResultadoLigacao resultadoLigacao) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    if (!resultadoLigacao.isLimpaAgendamentos()) {
      logger.info("Nao vai limpar agendamentos {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Tenant tenant, DaoFactory daoFactory, Ligacao ligacao,
      ResultadoLigacao resultadoLigacao) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    logger.info("Vai limpar agendamentos {}", cliente);
    for (Agendamento agendamento : cliente.getAgendamento())
      daoFactory.getAgendamentoDao().remove(agendamento);
    cliente.getAgendamento().clear();
    tratadorEspecificoClienteFactory.create(tenant, daoFactory).obtemClienteDao().atualiza(cliente);
    return true;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 3;
  }

}
