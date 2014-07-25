package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaIndisponibilizaTemporariamente implements ProcessoDevolucao {

  private static final Logger logger = getLogger(ProcessaIndisponibilizaTemporariamente.class);

  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;

  @Inject
  ProcessaIndisponibilizaTemporariamente(
      TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory) {
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
  }

  @Override
  public boolean accept(Configuracoes configuracoes, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, DaoFactory daoFactory) {
    return true;
  }

  @Override
  public boolean executa(Configuracoes configuracoes, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, DaoFactory daoFactory) {
    logger.info("Indisponibilizando por {} minutos {}",
        resultadoLigacao.getIntervaloIndisponivel(), cliente);

    cliente.setDisponivelAPartirDe(resultadoLigacao.getIntervaloIndisponivel() <= 0 ? null
        : new DateTime().plusMinutes(resultadoLigacao.getIntervaloIndisponivel()));

    tratadorEspecificoClienteFactory.create(configuracoes, daoFactory).obtemClienteDao()
        .atualiza(cliente);
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
