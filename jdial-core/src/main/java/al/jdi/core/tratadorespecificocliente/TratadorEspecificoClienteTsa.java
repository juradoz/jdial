package al.jdi.core.tratadorespecificocliente;

import static org.slf4j.LoggerFactory.getLogger;

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

class TratadorEspecificoClienteTsa implements TratadorEspecificoCliente {

  static class TratadorEspecificoClienteTsaImplFactory implements TratadorEspecificoCliente.Factory {
    @Override
    public TratadorEspecificoCliente create(Tenant tenant, DaoFactory daoFactory) {
      return new TratadorEspecificoClienteTsa(tenant, daoFactory);
    }
  }

  private static final Logger logger = getLogger(TratadorEspecificoClienteTsa.class);

  private final Tenant tenant;
  private final DaoFactory daoFactory;

  TratadorEspecificoClienteTsa(Tenant tenant, DaoFactory daoFactory) {
    this.tenant = tenant;
    this.daoFactory = daoFactory;
    logger.debug("Iniciando {}", this);
  }

  @Override
  public boolean isDnc(Cliente cliente) {
    return daoFactory.getClienteDaoTsa().isDnc(cliente,
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
            .getDiscavel().getCliente());
        return;
      }

      logger.info("Efetivamente notificando tentativa para {}", ligacao.getDiscavel().getCliente());
      daoFactory.getClienteDaoTsa().insereResultadoTsa(
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
      daoFactory.getClienteDaoTsa().liberaNaBaseDoCliente(cliente,
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

      if (!resultadoLigacao.equals(resultadoSemAgentes) && ligacao.isAtendida()
          && !tenant.getConfiguracoes().isUraReversa()) {
        logger.info("Nao vai efetivamente notificar finalizacao pois isAtendida() para {}", ligacao
            .getDiscavel().getCliente());
        return;
      }

      logger.info("Efetivamente notificando finalizacao para {}", ligacao.getDiscavel()
          .getCliente());
      daoFactory.getClienteDaoTsa().insereResultadoTsa(
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
      daoFactory.getClienteDaoTsa().liberaNaBaseDoCliente(cliente,
          tenant.getConfiguracoes().getNomeBaseDados(), tenant.getConfiguracoes().getOperador());
    }
  }

  @Override
  public ClienteDao obtemClienteDao() {
    return daoFactory.getClienteDaoTsa();
  }

  @Override
  public boolean reservaNaBaseDoCliente(Cliente cliente) {
    return daoFactory.getClienteDaoTsa().reservaNaBaseDoCliente(cliente,
        tenant.getConfiguracoes().getOperador(), tenant.getConfiguracoes().getNomeBaseDados());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
