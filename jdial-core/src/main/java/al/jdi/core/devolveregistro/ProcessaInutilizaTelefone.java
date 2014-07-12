package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaInutilizaTelefone implements ProcessoDevolucao {

  private final Logger logger;
  private final FinalizadorCliente finalizadorCliente;

  @Inject
  ProcessaInutilizaTelefone(Logger logger, FinalizadorCliente finalizadorCliente) {
    this.logger = logger;
    this.finalizadorCliente = finalizadorCliente;
  }

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    if (!resultadoLigacao.isInutilizaTelefone()
        && resultadoLigacao.getQuantidadeDesteResultadoInutilizaTelefone() <= 0) {
      logger.info("Nao vai inutilizar telefone {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    if (resultadoLigacao.isInutilizaTelefone()) {
      logger.info("Inutilizando telefone por isInutilizaTelefone {}", cliente);
      finalizadorCliente.finalizaPorInutilizacaoSimples(daoFactory, cliente);
      return true;
    }

    if (daoFactory.getHistoricoLigacaoDao().procura(cliente, resultadoLigacao).size() >= resultadoLigacao
        .getQuantidadeDesteResultadoInutilizaTelefone()) {
      logger.info("Vai inutilizar telefone por quantidadeDesteResultadoInutilizaTelefone {}",
          cliente);
      finalizadorCliente.finalizaPorInutilizacaoSimples(daoFactory, cliente);
      ligacao.setInutilizaComMotivoDiferenciado(true);
    }
    return true;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 7;
  }

}
