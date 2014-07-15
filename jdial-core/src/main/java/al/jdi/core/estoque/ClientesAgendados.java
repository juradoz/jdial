package al.jdi.core.estoque;

import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.estoque.EstoqueModule.Agendados;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;

@Agendados
class ClientesAgendados implements ExtraidorClientes {

  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;

  @Inject
  ClientesAgendados(TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory) {
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
  }

  @Override
  public Collection<Cliente> extrai(Configuracoes configuracoes, DaoFactory daoFactory,
      int quantidade) {
    return tratadorEspecificoClienteFactory
        .create(configuracoes, daoFactory)
        .obtemClienteDao()
        .obtemAGGs(quantidade,
            daoFactory.getCampanhaDao().procura(configuracoes.getNomeCampanha()),
            configuracoes.getNomeBaseDados(), configuracoes.getNomeBase(),
            configuracoes.getOperador());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
