package net.danieljurado.dialer.devolveregistro;

import javax.inject.Inject;

import net.danieljurado.dialer.modelo.Ligacao;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaFimDaFila implements ProcessoDevolucao {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TratadorEspecificoCliente tratadorEspecificoCliente;

  @Inject
  ProcessaFimDaFila(TratadorEspecificoCliente tratadorEspecificoCliente) {
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
  }

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    if (resultadoLigacao != null && !resultadoLigacao.isVaiParaOFimDaFila()) {
      logger.info("Nao vai para o fim da fila {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    logger.info("Fim da fila {}", cliente);

    cliente.fimDaFila();
    tratadorEspecificoCliente.obtemClienteDao(daoFactory).atualiza(cliente);
    return true;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 11;
  }

}
