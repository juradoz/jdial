package al.jdi.core.tratadorespecificocliente;

import static org.slf4j.LoggerFactory.getLogger;

import javax.enterprise.inject.Alternative;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Situacao;
import al.jdi.dao.model.Telefone;

@Alternative
class TratadorEspecificoClienteTsaCRM implements TratadorEspecificoCliente {

  @Alternative
  static class TratadorEspecificoClienteTsaCRMImplFactory implements
      TratadorEspecificoCliente.Factory {
    @Override
    public TratadorEspecificoCliente create(Tenant tenant, DaoFactory daoFactory) {
      return new TratadorEspecificoClienteTsaCRM(tenant, daoFactory);
    }

  }

  private static final Logger logger = getLogger(TratadorEspecificoClienteTsaCRM.class);

  private final Tenant tenant;
  private final DaoFactory daoFactory;

  TratadorEspecificoClienteTsaCRM(Tenant tenant, DaoFactory daoFactory) {
    this.tenant = tenant;
    this.daoFactory = daoFactory;
    logger.debug("Iniciando {}", this);
  }

  @Override
  public boolean isDnc(Cliente cliente) {
    return daoFactory.getClienteDaoTsaCRM().isDnc(cliente,
        tenant.getConfiguracoes().getNomeBaseDados());
  }

  @Override
  public void notificaFimTentativa(Tenant tenant, Ligacao ligacao, Cliente cliente,
      DateTime dataBanco, Telefone telefoneOriginal, ResultadoLigacao resultadoLigacao,
      boolean inutilizaComMotivoDiferenciado) {
    try {
      ResultadoLigacao resultadoSemAgentes =
          daoFactory.getResultadoLigacaoDao().procura(23, tenant.getCampanha());

      if (!resultadoLigacao.equals(resultadoSemAgentes) && ligacao.isAtendida()
          && !tenant.getConfiguracoes().isUraReversa()) {
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
          tenant.getConfiguracoes().getMotivoFinalizacao(),
          tenant.getConfiguracoes().getNomeBaseDados(), tenant.getConfiguracoes().getOperador(),
          tenant.getConfiguracoes().getMotivoCampanha());
    } finally {
      daoFactory.getClienteDaoTsaCRM().liberaNaBaseDoCliente(cliente,
          tenant.getConfiguracoes().getNomeBaseDados(), tenant.getConfiguracoes().getOperador());
    }
  }

  @Override
  public void notificaFinalizacao(Tenant tenant, Ligacao ligacao, Cliente cliente,
      DateTime dataBanco, Telefone telefoneOriginal, ResultadoLigacao resultadoLigacao,
      boolean inutilizaComMotivoDiferenciado) {
    try {
      ResultadoLigacao resultadoSemAgentes =
          daoFactory.getResultadoLigacaoDao().procura(23, tenant.getCampanha());

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
          tenant.getConfiguracoes().getMotivoFinalizacao(),
          tenant.getConfiguracoes().getNomeBaseDados(), tenant.getConfiguracoes().getOperador(),
          tenant.getConfiguracoes().getMotivoCampanha());
    } finally {
      daoFactory.getClienteDaoTsaCRM().liberaNaBaseDoCliente(cliente,
          tenant.getConfiguracoes().getNomeBaseDados(), tenant.getConfiguracoes().getOperador());
    }
  }

  @Override
  public ClienteDao obtemClienteDao() {
    return daoFactory.getClienteDaoTsaCRM();
  }

  @Override
  public boolean reservaNaBaseDoCliente(Cliente cliente) {
    return daoFactory.getClienteDaoTsaCRM().reservaNaBaseDoCliente(cliente,
        tenant.getConfiguracoes().getOperador(), tenant.getConfiguracoes().getNomeBaseDados());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
