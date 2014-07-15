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

class ProcessaLimpaReserva implements ProcessoDevolucao {

  private final Logger logger;
  private final TratadorEspecificoCliente tratadorEspecificoCliente;

  @Inject
  ProcessaLimpaReserva(Logger logger, TratadorEspecificoCliente tratadorEspecificoCliente) {
    this.logger = logger;
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
  }

  @Override
  public boolean accept(Configuracoes configuracoes, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, DaoFactory daoFactory) {
    return true;
  }

  @Override
  public boolean executa(Configuracoes configuracoes, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, DaoFactory daoFactory) {
    logger.info("Vai limpar reserva {}", cliente);

    tratadorEspecificoCliente.obtemClienteDao(configuracoes, daoFactory).limpaReserva(cliente,
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
