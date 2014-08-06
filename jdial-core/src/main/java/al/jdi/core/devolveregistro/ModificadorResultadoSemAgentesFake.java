package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import al.jdi.core.devolveregistro.ModificadorResultado.ResultadosConhecidos;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultadoSemAgentesFake implements ModificadorResultadoFilter {

  private static final Logger logger = getLogger(ModificadorResultadoSemAgentesFake.class);

  @Override
  public boolean accept(Tenant tenant, DaoFactory daoFactory, Ligacao ligacao,
      ResultadoLigacao resultadoLigacao) {
    if (tenant.getConfiguracoes().isUraReversa())
      return false;
    ResultadoLigacao resultadoLigacaoAtendida =
        daoFactory.getResultadoLigacaoDao().procura(MotivoSistema.ATENDIDA.getCodigo(),
            tenant.getCampanha());
    return resultadoLigacao.equals(resultadoLigacaoAtendida) && !ligacao.isNoAgente();
  }

  @Override
  public ResultadoLigacao modifica(Tenant tenant, DaoFactory daoFactory, Ligacao ligacao,
      ResultadoLigacao resultadoLigacao) {
    logger.info("Alterando resultado por semAgentesFake {}", ligacao.getDiscavel().getCliente());
    return daoFactory.getResultadoLigacaoDao().procura(
        ResultadosConhecidos.SEM_AGENTES.getCodigo(), tenant.getCampanha());
  }

}
