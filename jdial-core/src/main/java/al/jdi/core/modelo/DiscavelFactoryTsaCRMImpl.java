package al.jdi.core.modelo;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Cliente;

@Alternative
class DiscavelFactoryTsaCRMImpl implements Discavel.Factory {

  private final Configuracoes configuracoes;

  @Inject
  DiscavelFactoryTsaCRMImpl(Configuracoes configuracoes) {
    this.configuracoes = configuracoes;
  }

  @Override
  public Discavel create(Cliente cliente) {
    return new DiscavelTsaCRMImpl(configuracoes, cliente);
  }

}
