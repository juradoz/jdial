package net.danieljurado.dialer.modelo;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import net.danieljurado.dialer.configuracoes.Configuracoes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.model.Cliente;

@Alternative
class DiscavelFactoryTsaCRMImpl implements Discavel.Factory {

  private static final Logger logger = LoggerFactory.getLogger(DiscavelFactoryTsaCRMImpl.class);

  private final Configuracoes configuracoes;

  @Inject
  DiscavelFactoryTsaCRMImpl(Configuracoes configuracoes) {
    this.configuracoes = configuracoes;
    logger.debug("Iniciando {}", this);
  }

  @Override
  public Discavel create(Cliente cliente) {
    return new DiscavelTsaCRMImpl(configuracoes, cliente);
  }

}
