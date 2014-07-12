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

  private final TratadorEspecificoCliente tratadorEspecificoCliente;
  private final Configuracoes configuracoes;

  @Inject
  ClientesAgendados(TratadorEspecificoCliente tratadorEspecificoCliente, Configuracoes configuracoes) {
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
    this.configuracoes = configuracoes;
  }

  @Override
  public Collection<Cliente> extrai(DaoFactory daoFactory, int quantidade) {
    return tratadorEspecificoCliente.obtemClienteDao(daoFactory).obtemAGGs(quantidade,
        daoFactory.getCampanhaDao().procura(configuracoes.getNomeCampanha()),
        configuracoes.getNomeBaseDados(), configuracoes.getNomeBase(), configuracoes.getOperador());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
