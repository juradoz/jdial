package net.danieljurado.dialer.devolveregistro;

import javax.inject.Inject;

import net.danieljurado.dialer.modelo.Ligacao;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

class NotificadorCliente {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TratadorEspecificoCliente tratadorEspecificoCliente;

  @Inject
  NotificadorCliente(TratadorEspecificoCliente tratadorEspecificoCliente) {
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
  }

  void notificaFimTentativa(DaoFactory daoFactory, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, Telefone telefoneOriginal, Campanha campanha) {
    if (!resultadoLigacao.isNotificaFimTentativa()) {
      logger.info("Nao vai notificar fim tentativa motivo {} {}", resultadoLigacao, cliente);
      return;
    }

    logger.info("Vai notificar fim tentativa motivo {} {}", resultadoLigacao, cliente);
    tratadorEspecificoCliente.notificaFimTentativa(daoFactory, ligacao, cliente, campanha,
        daoFactory.getDataBanco(), telefoneOriginal, resultadoLigacao,
        ligacao.isInutilizaComMotivoDiferenciado());
  }

  void notificaFinalizacao(DaoFactory daoFactory, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, Telefone telefoneOriginal,
      boolean inutilizaComMotivoDiferenciado, Campanha campanha) {
    if (!resultadoLigacao.isNotificaFimTentativa()) {
      logger.info("Nao vai notificar finalizacao motivo {} {}", resultadoLigacao, cliente);
      return;
    }

    logger.info("Vai notificar finalizacao motivo {} {} ", resultadoLigacao, cliente);
    tratadorEspecificoCliente.notificaFinalizacao(daoFactory, ligacao, cliente, campanha,
        daoFactory.getDataBanco(), telefoneOriginal, resultadoLigacao,
        inutilizaComMotivoDiferenciado);
  }
}
