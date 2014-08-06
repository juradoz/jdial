package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import al.jdi.core.devolveregistro.ModificadorResultado.ResultadosConhecidos;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultadoAtendidoFake implements ModificadorResultadoFilter {

  private static final Logger logger = getLogger(ModificadorResultadoAtendidoFake.class);

  @Override
  public boolean accept(Tenant tenant, DaoFactory daoFactory, Ligacao ligacao,
      ResultadoLigacao resultadoLigacao) {
    ResultadoLigacao resultadoLigacaoInexistente =
        daoFactory.getResultadoLigacaoDao().procura(ResultadosConhecidos.INEXISTENTE.getCodigo(),
            tenant.getCampanha());
    return !tenant.getConfiguracoes().isUraReversa()
        && resultadoLigacao.equals(resultadoLigacaoInexistente) && ligacao.isAtendida();
  }

  @Override
  public ResultadoLigacao modifica(Tenant tenant, DaoFactory daoFactory, Ligacao ligacao,
      ResultadoLigacao resultadoLigacao) {
    logger.info("Alterando resultado por atendidoFake {}", ligacao.getDiscavel().getCliente());
    return daoFactory.getResultadoLigacaoDao().procura(MotivoSistema.ATENDIDA.getCodigo(),
        tenant.getCampanha());
  }

}
