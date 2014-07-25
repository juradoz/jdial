package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultadoInexistenteFake implements ModificadorResultadoFilter {

  private static final Logger logger = getLogger(ModificadorResultadoInexistenteFake.class);

  @Override
  public boolean accept(Tenant tenant, DaoFactory daoFactory, ResultadoLigacao resultadoLigacao,
      Ligacao ligacao) {
    if (tenant.getConfiguracoes().isUraReversa())
      return false;
    ResultadoLigacao resultadoLigacaoAtendida =
        daoFactory.getResultadoLigacaoDao().procura(-1, tenant.getCampanha());
    return resultadoLigacao.equals(resultadoLigacaoAtendida) && !ligacao.isAtendida();
  }

  @Override
  public ResultadoLigacao modifica(Tenant tenant, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao) {
    logger.info("Alterando resultado por inexistenteFake {}", ligacao.getDiscavel().getCliente());
    return daoFactory.getResultadoLigacaoDao().procura(13, tenant.getCampanha());
  }

}
