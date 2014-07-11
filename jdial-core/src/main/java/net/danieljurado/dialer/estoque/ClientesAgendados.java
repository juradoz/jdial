package net.danieljurado.dialer.estoque;

import java.util.Collection;

import javax.inject.Inject;

import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.estoque.EstoqueModule.Agendados;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;

@Agendados
class ClientesAgendados implements ExtraidorClientes {

  private final static Logger logger = LoggerFactory.getLogger(ClientesAgendados.class);

  private final TratadorEspecificoCliente tratadorEspecificoCliente;
  private final Configuracoes configuracoes;

  @Inject
  ClientesAgendados(TratadorEspecificoCliente tratadorEspecificoCliente, Configuracoes configuracoes) {
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
    this.configuracoes = configuracoes;
    logger.debug("Iniciado {}", this);
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
