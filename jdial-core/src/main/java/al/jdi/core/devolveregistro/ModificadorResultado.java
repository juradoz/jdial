package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultado {

  private static final Logger logger = getLogger(ModificadorResultado.class);

  private final Instance<ModificadorResultadoFilter> filters;

  @Inject
  ModificadorResultado(@Any Instance<ModificadorResultadoFilter> filters) {
    this.filters = filters;
  }

  ResultadoLigacao modifica(Configuracoes configuracoes, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao, Cliente cliente, Campanha campanha) {

    for (ModificadorResultadoFilter filter : filters) {
      logger.debug("Avaliando {}...", filter);
      if (filter.accept(configuracoes, daoFactory, resultadoLigacao, ligacao, cliente, campanha)) {
        logger.debug("Vai processar {}", filter);
        return filter.modifica(configuracoes, daoFactory, resultadoLigacao, ligacao, cliente,
            campanha);
      }
      logger.debug("Filtro {} nao aceito", filter);
    }
    return resultadoLigacao;
  }
}
