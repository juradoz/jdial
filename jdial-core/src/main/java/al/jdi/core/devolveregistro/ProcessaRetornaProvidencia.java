package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.modelo.Providencia;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaRetornaProvidencia implements ProcessoDevolucao {

  private static final Logger logger = getLogger(ProcessaRetornaProvidencia.class);

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 13;
  }

  @Override
  public boolean accept(Tenant tenant, Ligacao ligacao, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    ResultadoLigacao resultadoSemProximoTelefone =
        daoFactory.getResultadoLigacaoDao().procura(MotivoSistema.SEM_PROXIMO_TELEFONE.getCodigo(),
            cliente.getMailing().getCampanha());

    if (!resultadoLigacao.equals(resultadoSemProximoTelefone)) {
      logger.info("Nao vai retornar providencia de {} {}", cliente.getInformacaoCliente()
          .getProvidenciaTelefone(), cliente);
      return false;
    }

    return true;
  }

  @Override
  public boolean executa(Tenant tenant, Ligacao ligacao, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    logger.info("Retornando providencia de {} {}", cliente.getInformacaoCliente()
        .getProvidenciaTelefone(), cliente);
    cliente.getInformacaoCliente().setProvidenciaTelefone(
        Providencia.Codigo.MANTEM_ATUAL.getCodigo());
    daoFactory.getInformacaoClienteDao().atualiza(cliente.getInformacaoCliente());
    return true;
  }

}
