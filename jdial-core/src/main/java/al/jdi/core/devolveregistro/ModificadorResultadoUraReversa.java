package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultadoUraReversa implements ModificadorResultadoFilter {

  private final Logger logger;

  @Inject
  ModificadorResultadoUraReversa(Logger logger) {
    this.logger = logger;
  }

  @Override
  public boolean accept(Configuracoes configuracoes, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao, Cliente cliente, Campanha campanha) {
    if (!configuracoes.isUraReversa())
      return false;

    if (ligacao.isNoAgente())
      return false;

    ResultadoLigacao resultadoLigacaoAtendida =
        daoFactory.getResultadoLigacaoDao().procura(-1, campanha);
    ResultadoLigacao resultadoLigacaoSemAgentes =
        daoFactory.getResultadoLigacaoDao().procura(23, campanha);

    boolean semAgentes = resultadoLigacao.equals(resultadoLigacaoSemAgentes);
    boolean atendida = resultadoLigacao.equals(resultadoLigacaoAtendida);

    if (!(semAgentes || atendida))
      return false;

    return true;
  }

  @Override
  public ResultadoLigacao modifica(Configuracoes configuracoes, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao, Cliente cliente, Campanha campanha) {
    if (ligacao.isFoiPraFila()) {
      logger.info("Alterando resultado por abandono Ura reversa {}", cliente);
      return daoFactory.getResultadoLigacaoDao().procura(-10, campanha);
    }
    logger.info("Alterando resultado por sem interesse Ura reversa {}", cliente);
    return daoFactory.getResultadoLigacaoDao().procura(-11, campanha);
  }

}
