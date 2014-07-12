package al.jdi.core.modelo;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.core.configuracoes.Configuracoes;
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

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
