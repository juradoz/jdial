package net.danieljurado.dialer.tratadorespecificocliente;

import javax.inject.Inject;

import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.modelo.Ligacao;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Situacao;
import al.jdi.dao.model.Telefone;

class TratadorEspecificoClienteTsaCRMImpl implements TratadorEspecificoCliente {

  private static final Logger logger = LoggerFactory
      .getLogger(TratadorEspecificoClienteTsaCRMImpl.class);

  private final Configuracoes configuracoes;

  @Inject
  TratadorEspecificoClienteTsaCRMImpl(Configuracoes configuracoes) {
    this.configuracoes = configuracoes;
    logger.info("Iniciando {}", this);
  }

  @Override
  public boolean isDnc(DaoFactory daoFactory, Cliente cliente, String baseDados) {
    return daoFactory.getClienteDaoTsaCRM().isDnc(cliente, baseDados);
  }

  @Override
  public void notificaFimTentativa(DaoFactory daoFactory, Ligacao ligacao, Cliente cliente,
      Campanha campanha, DateTime dataBanco, Telefone telefoneOriginal,
      ResultadoLigacao resultadoLigacao, boolean inutilizaComMotivoDiferenciado) {
    try {
      ResultadoLigacao resultadoSemAgentes =
          daoFactory.getResultadoLigacaoDao().procura(23, campanha);

      if (!resultadoLigacao.equals(resultadoSemAgentes) && ligacao.isAtendida()
          && !configuracoes.isUraReversa()) {
        logger.info("Nao vai efetivamente notificar tentativa pois isAtendida() para {}", ligacao
            .getDiscavel().getCliente().getId());
        return;
      }

      logger.info("Efetivamente notificando tentativa para {}", ligacao.getDiscavel().getCliente()
          .getId());
      daoFactory.getClienteDaoTsaCRM().insereResultadoTsa(
          cliente,
          resultadoLigacao,
          telefoneOriginal,
          dataBanco,
          Situacao.TENTATIVA,
          inutilizaComMotivoDiferenciado ? resultadoLigacao
              .getMotivoFinalizacaoPorQuantidadeResultado() : resultadoLigacao.getMotivo(),
          configuracoes.getMotivoFinalizacao(), configuracoes.getNomeBaseDados(),
          configuracoes.getOperador(), configuracoes.getMotivoCampanha());
    } finally {
      daoFactory.getClienteDaoTsaCRM().liberaNaBaseDoCliente(cliente,
          configuracoes.getNomeBaseDados(), configuracoes.getOperador());
    }
  }

  @Override
  public void notificaFinalizacao(DaoFactory daoFactory, Ligacao ligacao, Cliente cliente,
      Campanha campanha, DateTime dataBanco, Telefone telefoneOriginal,
      ResultadoLigacao resultadoLigacao, boolean inutilizaComMotivoDiferenciado) {
    try {
      ResultadoLigacao resultadoSemAgentes =
          daoFactory.getResultadoLigacaoDao().procura(23, campanha);

      if (!resultadoLigacao.equals(resultadoSemAgentes) && ligacao.isAtendida()) {
        logger.info("Nao vai efetivamente notificar finalizacao pois isAtendida() para {}", ligacao
            .getDiscavel().getCliente().getId());
        return;
      }

      logger.info("Efetivamente notificando finalizacao para {}", ligacao.getDiscavel()
          .getCliente().getId());
      daoFactory.getClienteDaoTsaCRM().insereResultadoTsa(
          cliente,
          resultadoLigacao,
          telefoneOriginal,
          dataBanco,
          Situacao.FINALIZACAO,
          inutilizaComMotivoDiferenciado ? resultadoLigacao
              .getMotivoFinalizacaoPorQuantidadeResultado() : resultadoLigacao.getMotivo(),
          configuracoes.getMotivoFinalizacao(), configuracoes.getNomeBaseDados(),
          configuracoes.getOperador(), configuracoes.getMotivoCampanha());
    } finally {
      daoFactory.getClienteDaoTsaCRM().liberaNaBaseDoCliente(cliente,
          configuracoes.getNomeBaseDados(), configuracoes.getOperador());
    }
  }

  @Override
  public ClienteDao obtemClienteDao(DaoFactory daoFactory) {
    return daoFactory.getClienteDaoTsaCRM();
  }

  @Override
  public boolean reservaNaBaseDoCliente(DaoFactory daoFactory, Cliente cliente) {
    return daoFactory.getClienteDaoTsaCRM().reservaNaBaseDoCliente(cliente,
        configuracoes.getOperador(), configuracoes.getNomeBaseDados());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
