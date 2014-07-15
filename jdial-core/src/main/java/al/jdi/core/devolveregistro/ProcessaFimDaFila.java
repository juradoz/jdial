package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaFimDaFila implements ProcessoDevolucao {

  private final Logger logger;
  private final TratadorEspecificoCliente tratadorEspecificoCliente;

  @Inject
  ProcessaFimDaFila(Logger logger, TratadorEspecificoCliente tratadorEspecificoCliente) {
    this.logger = logger;
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
  }

  @Override
  public boolean accept(Configuracoes configuracoes, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, DaoFactory daoFactory) {
    if (resultadoLigacao != null && !resultadoLigacao.isVaiParaOFimDaFila()) {
      logger.info("Nao vai para o fim da fila {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Configuracoes configuracoes, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, DaoFactory daoFactory) {
    logger.info("Fim da fila {}", cliente);

    cliente.fimDaFila();
    tratadorEspecificoCliente.obtemClienteDao(configuracoes, daoFactory).atualiza(cliente);
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
