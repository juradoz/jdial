package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaRemoveTodosAgendamentos implements ProcessoDevolucao {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TratadorEspecificoCliente tratadorEspecificoCliente;

  @Inject
  ProcessaRemoveTodosAgendamentos(TratadorEspecificoCliente tratadorEspecificoCliente) {
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
  }

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    if (!resultadoLigacao.isLimpaAgendamentos()) {
      logger.info("Nao vai limpar agendamentos {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    logger.info("Vai limpar agendamentos {}", cliente);
    for (Agendamento agendamento : cliente.getAgendamento())
      daoFactory.getAgendamentoDao().remove(agendamento);
    cliente.getAgendamento().clear();
    tratadorEspecificoCliente.obtemClienteDao(daoFactory).atualiza(cliente);
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
