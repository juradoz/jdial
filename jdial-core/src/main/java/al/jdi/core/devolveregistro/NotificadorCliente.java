package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

class NotificadorCliente {
  private final Logger logger;
  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;

  @Inject
  NotificadorCliente(Logger logger,
      TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory) {
    this.logger = logger;
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
  }

  void notificaFimTentativa(Configuracoes configuracoes, DaoFactory daoFactory, Ligacao ligacao,
      Cliente cliente, ResultadoLigacao resultadoLigacao, Telefone telefoneOriginal,
      Campanha campanha) {
    if (!resultadoLigacao.isNotificaFimTentativa()) {
      logger.info("Nao vai notificar fim tentativa motivo {} {}", resultadoLigacao, cliente);
      return;
    }

    logger.info("Vai notificar fim tentativa motivo {} {}", resultadoLigacao, cliente);
    tratadorEspecificoClienteFactory.create(configuracoes, daoFactory).notificaFimTentativa(
        ligacao, cliente, campanha, daoFactory.getDataBanco(), telefoneOriginal, resultadoLigacao,
        ligacao.isInutilizaComMotivoDiferenciado());
  }

  void notificaFinalizacao(Configuracoes configuracoes, DaoFactory daoFactory, Ligacao ligacao,
      Cliente cliente, ResultadoLigacao resultadoLigacao, Telefone telefoneOriginal,
      boolean inutilizaComMotivoDiferenciado, Campanha campanha) {
    if (!resultadoLigacao.isNotificaFimTentativa()) {
      logger.info("Nao vai notificar finalizacao motivo {} {}", resultadoLigacao, cliente);
      return;
    }

    logger.info("Vai notificar finalizacao motivo {} {} ", resultadoLigacao, cliente);
    tratadorEspecificoClienteFactory.create(configuracoes, daoFactory).notificaFinalizacao(ligacao,
        cliente, campanha, daoFactory.getDataBanco(), telefoneOriginal, resultadoLigacao,
        inutilizaComMotivoDiferenciado);
  }
}
