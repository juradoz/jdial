package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultadoAtendidoFake implements ModificadorResultadoFilter {

  private static final Logger logger = getLogger(ModificadorResultadoAtendidoFake.class);

  @Override
  public boolean accept(Tenant tenant, DaoFactory daoFactory, ResultadoLigacao resultadoLigacao,
      Ligacao ligacao) {
    ResultadoLigacao resultadoLigacaoInexistente =
        daoFactory.getResultadoLigacaoDao().procura(13, tenant.getCampanha());
    return !tenant.getConfiguracoes().isUraReversa()
        && resultadoLigacao.equals(resultadoLigacaoInexistente) && ligacao.isAtendida();
  }

  @Override
  public ResultadoLigacao modifica(Tenant tenant, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao) {
    logger.info("Alterando resultado por atendidoFake {}", ligacao.getDiscavel().getCliente());
    return daoFactory.getResultadoLigacaoDao().procura(-1, tenant.getCampanha());
  }

}
