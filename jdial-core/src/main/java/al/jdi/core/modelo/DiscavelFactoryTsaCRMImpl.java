package al.jdi.core.modelo;

import javax.enterprise.inject.Alternative;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Cliente;

@Alternative
class DiscavelFactoryTsaCRMImpl implements Discavel.Factory {

  @Override
  public Discavel create(Configuracoes configuracoes, Cliente cliente) {
    return new DiscavelTsaCRMImpl(configuracoes, cliente);
  }

}
