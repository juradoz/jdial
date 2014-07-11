package net.danieljurado.dialer.devolveregistro;

import javax.inject.Inject;

import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.modelo.Ligacao;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaLimpaReserva implements ProcessoDevolucao {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TratadorEspecificoCliente tratadorEspecificoCliente;
  private final Configuracoes configuracoes;

  @Inject
  ProcessaLimpaReserva(TratadorEspecificoCliente tratadorEspecificoCliente,
      Configuracoes configuracoes) {
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
    this.configuracoes = configuracoes;
  }

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    return true;
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    logger.info("Vai limpar reserva {}", cliente);

    tratadorEspecificoCliente.obtemClienteDao(daoFactory).limpaReserva(cliente,
        configuracoes.getOperador(), configuracoes.getNomeBaseDados());
    return true;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 12;
  }

}
