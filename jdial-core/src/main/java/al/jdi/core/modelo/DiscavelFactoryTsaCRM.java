package al.jdi.core.modelo;

import javax.enterprise.inject.Alternative;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Cliente;

@Alternative
class DiscavelFactoryTsaCRM implements Discavel.Factory {

  @Override
  public Discavel create(Configuracoes configuracoes, Cliente cliente) {
    return new DiscavelTsaCRM(configuracoes, cliente);
  }

}
