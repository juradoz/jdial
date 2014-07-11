package net.danieljurado.dialer.devolveregistro;

import javax.inject.Inject;

import net.danieljurado.dialer.modelo.Ligacao;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Agendamento;
import al.jdi.dao.model.Agente;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.HistoricoCliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaAgendamento implements ProcessoDevolucao {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TratadorEspecificoCliente tratadorEspecificoCliente;

  @Inject
  ProcessaAgendamento(TratadorEspecificoCliente tratadorEspecificoCliente) {
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
  }

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    if (resultadoLigacao == null || resultadoLigacao.getIntervaloReagendamento() <= 0) {
      logger.info("Nao vai agendar {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    DateTime cal =
        new DateTime().minusMinutes(resultadoLigacao.getIntervaloDesteResultadoReagenda());

    if (daoFactory.getHistoricoLigacaoDao().procura(cliente, resultadoLigacao, cal).size() > 1) {
      logger.info("Nao vai agendar por ja ter resultado no intervalo {}", cliente);
      cliente.getAgendamento().clear();
      tratadorEspecificoCliente.obtemClienteDao(daoFactory).atualiza(cliente);
      return true;
    }

    logger.info("Reagendando para {} minutos por resultado {} {}",
        new Object[] {resultadoLigacao.getIntervaloReagendamento(), resultadoLigacao, cliente});

    DateTime calAgendamento =
        new DateTime().plusMinutes(resultadoLigacao.getIntervaloReagendamento());

    Agente agente = null;
    Agendamento agendamento = daoFactory.getAgendamentoDao().procura(cliente);

    if (agendamento == null) {
      agendamento = new Agendamento();
      agendamento.setCliente(cliente);
      agendamento.setAgendamento(calAgendamento);
      daoFactory.getAgendamentoDao().adiciona(agendamento);
    } else {
      agendamento.setAgendamento(calAgendamento);
      daoFactory.getAgendamentoDao().atualiza(agendamento);
    }

    HistoricoCliente historicoCliente = new HistoricoCliente();
    historicoCliente.setCliente(cliente);
    historicoCliente.setAgendamento(calAgendamento);
    historicoCliente.setAgente(agente);
    historicoCliente.setEstadoCliente(cliente.getEstadoCliente());
    historicoCliente
        .setDescricao(String.format("Agendamento de registro por %s", resultadoLigacao));
    daoFactory.getHistoricoClienteDao().adiciona(historicoCliente);
    return true;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 8;
  }

}
