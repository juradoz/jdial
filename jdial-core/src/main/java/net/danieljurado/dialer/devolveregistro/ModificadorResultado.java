package net.danieljurado.dialer.devolveregistro;

import java.util.Set;

import javax.inject.Inject;

import net.danieljurado.dialer.modelo.Ligacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ModificadorResultado {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Set<ModificadorResultadoFilter> filters;

  @Inject
  ModificadorResultado(Set<ModificadorResultadoFilter> filters) {
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
