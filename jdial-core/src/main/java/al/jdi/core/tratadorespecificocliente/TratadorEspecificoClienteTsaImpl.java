package al.jdi.core.tratadorespecificocliente;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import al.jdi.common.LogProducer.LogClass;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Situacao;
import al.jdi.dao.model.Telefone;

class TratadorEspecificoClienteTsaImpl implements TratadorEspecificoCliente {

  static class TratadorEspecificoClienteTsaImplFactory implements TratadorEspecificoCliente.Factory {
    @Inject
    @LogClass(clazz = TratadorEspecificoClienteTsaImpl.class)
    private Logger logger;

    @Override
    public TratadorEspecificoCliente create(Configuracoes configuracoes, DaoFactory daoFactory) {
      return new TratadorEspecificoClienteTsaImpl(logger, configuracoes, daoFactory);
    }
  }

  private final Logger logger;
  private final Configuracoes configuracoes;
  private final DaoFactory daoFactory;

  TratadorEspecificoClienteTsaImpl(Logger logger, Configuracoes configuracoes, DaoFactory daoFactory) {
    this.logger = logger;
    this.configuracoes = configuracoes;
    this.daoFactory = daoFactory;
    logger.debug("Iniciando {}", this);
  }

  @Override
  public boolean isDnc(Cliente cliente) {
    return daoFactory.getClienteDaoTsa().isDnc(cliente, configuracoes.getNomeBaseDados());
  }

  @Override
  public void notificaFimTentativa(Ligacao ligacao, Cliente cliente, Campanha campanha,
      DateTime dataBanco, Telefone telefoneOriginal, ResultadoLigacao resultadoLigacao,
      boolean inutilizaComMotivoDiferenciado) {
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
      daoFactory.getClienteDaoTsa().insereResultadoTsa(
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
      daoFactory.getClienteDaoTsa().liberaNaBaseDoCliente(cliente,
          configuracoes.getNomeBaseDados(), configuracoes.getOperador());
    }
  }

  @Override
  public void notificaFinalizacao(Ligacao ligacao, Cliente cliente, Campanha campanha,
      DateTime dataBanco, Telefone telefoneOriginal, ResultadoLigacao resultadoLigacao,
      boolean inutilizaComMotivoDiferenciado) {
    try {
      ResultadoLigacao resultadoSemAgentes =
          daoFactory.getResultadoLigacaoDao().procura(23, campanha);

      if (!resultadoLigacao.equals(resultadoSemAgentes) && ligacao.isAtendida()
          && !configuracoes.isUraReversa()) {
        logger.info("Nao vai efetivamente notificar finalizacao pois isAtendida() para {}", ligacao
            .getDiscavel().getCliente().getId());
        return;
      }

      logger.info("Efetivamente notificando finalizacao para {}", ligacao.getDiscavel()
          .getCliente().getId());
      daoFactory.getClienteDaoTsa().insereResultadoTsa(
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
      daoFactory.getClienteDaoTsa().liberaNaBaseDoCliente(cliente,
          configuracoes.getNomeBaseDados(), configuracoes.getOperador());
    }
  }

  @Override
  public ClienteDao obtemClienteDao() {
    return daoFactory.getClienteDaoTsa();
  }

  @Override
  public boolean reservaNaBaseDoCliente(Cliente cliente) {
    return daoFactory.getClienteDaoTsa().reservaNaBaseDoCliente(cliente,
        configuracoes.getOperador(), configuracoes.getNomeBaseDados());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
