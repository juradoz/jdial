package al.jdi.core.devolveregistro;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultado {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Instance<ModificadorResultadoFilter> filters;

  @Inject
  ModificadorResultado(@Any Instance<ModificadorResultadoFilter> filters) {
    this.filters = filters;
  }

  ResultadoLigacao modifica(DaoFactory daoFactory, ResultadoLigacao resultadoLigacao,
      Ligacao ligacao, Cliente cliente, Campanha campanha) {

    for (ModificadorResultadoFilter filter : filters) {
      logger.debug("Avaliando {}...", filter);
      if (filter.accept(daoFactory, resultadoLigacao, ligacao, cliente, campanha)) {
        logger.debug("Vai processar {}", filter);
        return filter.modifica(daoFactory, resultadoLigacao, ligacao, cliente, campanha);
      }
      logger.debug("Filtro {} nao aceito", filter);
    }
    return resultadoLigacao;
  }
}
