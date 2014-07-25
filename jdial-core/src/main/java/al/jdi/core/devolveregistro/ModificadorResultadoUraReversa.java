package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultadoUraReversa implements ModificadorResultadoFilter {

  private static final Logger logger = getLogger(ModificadorResultadoUraReversa.class);

  @Override
  public boolean accept(Tenant tenant, DaoFactory daoFactory, ResultadoLigacao resultadoLigacao,
      Ligacao ligacao) {
    if (!tenant.getConfiguracoes().isUraReversa())
      return false;

    if (ligacao.isNoAgente())
      return false;

    ResultadoLigacao resultadoLigacaoAtendida =
        daoFactory.getResultadoLigacaoDao().procura(-1, tenant.getCampanha());
    ResultadoLigacao resultadoLigacaoSemAgentes =
        daoFactory.getResultadoLigacaoDao().procura(23, tenant.getCampanha());

    boolean semAgentes = resultadoLigacao.equals(resultadoLigacaoSemAgentes);
    boolean atendida = resultadoLigacao.equals(resultadoLigacaoAtendida);

    if (!(semAgentes || atendida))
      return false;

    return true;
  }

  @Override
  public ResultadoLigacao modifica(Tenant tenant, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    if (ligacao.isFoiPraFila()) {
      logger.info("Alterando resultado por abandono Ura reversa {}", cliente);
      return daoFactory.getResultadoLigacaoDao().procura(-10, tenant.getCampanha());
    }
    logger.info("Alterando resultado por sem interesse Ura reversa {}", cliente);
    return daoFactory.getResultadoLigacaoDao().procura(-11, tenant.getCampanha());
  }

}
