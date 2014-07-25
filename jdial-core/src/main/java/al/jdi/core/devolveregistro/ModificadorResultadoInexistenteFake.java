package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultadoInexistenteFake implements ModificadorResultadoFilter {

  private static final Logger logger = getLogger(ModificadorResultadoInexistenteFake.class);

  @Override
  public boolean accept(Configuracoes configuracoes, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao, Cliente cliente, Campanha campanha) {
    if (configuracoes.isUraReversa())
      return false;
    ResultadoLigacao resultadoLigacaoAtendida =
        daoFactory.getResultadoLigacaoDao().procura(-1, campanha);
    return resultadoLigacao.equals(resultadoLigacaoAtendida) && !ligacao.isAtendida();
  }

  @Override
  public ResultadoLigacao modifica(Configuracoes configuracoes, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao, Cliente cliente, Campanha campanha) {
    logger.info("Alterando resultado por inexistenteFake {}", cliente);
    return daoFactory.getResultadoLigacaoDao().procura(13, campanha);
  }

}
