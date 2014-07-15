package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultadoSemAgentesFake implements ModificadorResultadoFilter {

  private final Logger logger;

  @Inject
  ModificadorResultadoSemAgentesFake(Logger logger) {
    this.logger = logger;
  }

  @Override
  public boolean accept(Configuracoes configuracoes, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao, Cliente cliente, Campanha campanha) {
    if (configuracoes.isUraReversa())
      return false;
    ResultadoLigacao resultadoLigacaoAtendida =
        daoFactory.getResultadoLigacaoDao().procura(-1, campanha);
    return resultadoLigacao.equals(resultadoLigacaoAtendida) && !ligacao.isNoAgente();
  }

  @Override
  public ResultadoLigacao modifica(Configuracoes configuracoes, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao, Cliente cliente, Campanha campanha) {
    logger.info("Alterando resultado por semAgentesFake {}", cliente);
    return daoFactory.getResultadoLigacaoDao().procura(23, campanha);
  }

}
