package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaIndisponibilizaTemporariamente implements ProcessoDevolucao {

  private final Logger logger;
  private final TratadorEspecificoCliente tratadorEspecificoCliente;

  @Inject
  ProcessaIndisponibilizaTemporariamente(Logger logger,
      TratadorEspecificoCliente tratadorEspecificoCliente) {
    this.logger = logger;
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
  }

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    return true;
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    logger.info("Indisponibilizando por {} minutos {}",
        resultadoLigacao.getIntervaloIndisponivel(), cliente);

    cliente.setDisponivelAPartirDe(resultadoLigacao.getIntervaloIndisponivel() <= 0 ? null
        : new DateTime().plusMinutes(resultadoLigacao.getIntervaloIndisponivel()));

    tratadorEspecificoCliente.obtemClienteDao(daoFactory).atualiza(cliente);
    return true;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 10;
  }

}
