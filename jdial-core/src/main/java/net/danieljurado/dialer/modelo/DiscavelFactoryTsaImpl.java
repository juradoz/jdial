package net.danieljurado.dialer.modelo;

import javax.inject.Inject;

import net.danieljurado.dialer.configuracoes.Configuracoes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.model.Cliente;

class DiscavelFactoryTsaImpl implements Discavel.Factory {

  private static final Logger logger = LoggerFactory.getLogger(DiscavelFactoryTsaImpl.class);

  private final Configuracoes configuracoes;

  @Inject
  DiscavelFactoryTsaImpl(Configuracoes configuracoes) {
    this.configuracoes = configuracoes;
    logger.debug("Iniciando {}", this);
  }

  @Override
  public Discavel create(Cliente cliente) {
    return new DiscavelTsaImpl(configuracoes, cliente);
  }

}
