package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.TelefoneDao;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.MotivoFinalizacao;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

class ProcessaIncrementaTentativa implements ProcessoDevolucao {

  private static final Logger logger = getLogger(ProcessaIncrementaTentativa.class);

  private final FinalizadorCliente finalizadorCliente;
  private final NotificadorCliente notificadorCliente;

  private ResultadoLigacao resultadoLigacao;

  @Inject
  ProcessaIncrementaTentativa(FinalizadorCliente finalizadorCliente,
      NotificadorCliente notificadorCliente) {
    this.finalizadorCliente = finalizadorCliente;
    this.notificadorCliente = notificadorCliente;
  }

  @Override
  public boolean accept(Tenant tenant, Ligacao ligacao, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    if (!resultadoLigacao.isIncrementaTentativa()) {
      logger.info("Nao vai incrementar tentativa {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Tenant tenant, Ligacao ligacao, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    logger.info("Incrementando tentativa {}", cliente);

    TelefoneDao telefoneDao = daoFactory.getTelefoneDao();
    Telefone telefone = telefoneDao.procura(cliente.getTelefone().getId());
    telefone.incTentativa();
    telefoneDao.atualiza(telefone);
    if (tenant.getConfiguracoes().getLimiteTentativasPorTelefone()) {
      return limitaTentativasPorTelefone(tenant, daoFactory, cliente, telefoneDao);
    }

    return limitaTentativasPorCliente(tenant, daoFactory, cliente, telefoneDao, ligacao);
  }

  private boolean limitaTentativasPorTelefone(Tenant tenant, DaoFactory daoFactory,
      Cliente cliente, TelefoneDao telefoneDao) {
    int limiteTentativas = tenant.getConfiguracoes().getLimiteTentativas();
    int totalTentativas = cliente.getTelefone().getTentativa();
    if (totalTentativas < limiteTentativas) {
      logger.info("Ainda nao estourou tentativas telefone {}: {} de {} {}",
          new Object[] {cliente.getTelefone(), totalTentativas, limiteTentativas, cliente});
      return true;
    }

    logger.info("Finalizando por excesso de tentativas tentativas telefone {}: {} de {} {}",
        new Object[] {cliente.getTelefone(), totalTentativas, limiteTentativas, cliente});
    finalizadorCliente.finalizaPorInutilizacaoSimples(tenant, daoFactory, cliente);
    return true;
  }

  private boolean limitaTentativasPorCliente(Tenant tenant, DaoFactory daoFactory, Cliente cliente,
      TelefoneDao telefoneDao, Ligacao ligacao) {
    int limiteTentativas = tenant.getConfiguracoes().getLimiteTentativas();
    int totalTentativas =
        telefoneDao.totalTentativas(tenant.getConfiguracoes().bloqueiaCelular(), cliente);
    if (totalTentativas < limiteTentativas) {
      logger.info("Ainda nao estourou tentativas cliente: {} de {} {}", new Object[] {
          totalTentativas, limiteTentativas, cliente});
      return true;
    }

    logger.info("Finalizando por excesso de tentativas tentativas cliente: {} de {} {}",
        new Object[] {totalTentativas, limiteTentativas, cliente});

    MotivoFinalizacao motivoFinalizacao =
        daoFactory.getMotivoFinalizacaoDao().procura("Excesso tentativas");

    finalizadorCliente.finaliza(tenant, daoFactory, cliente, motivoFinalizacao);
    logger.info("Finalizado {}", cliente);
    notificadorCliente.notificaFinalizacao(tenant, daoFactory, ligacao, cliente, resultadoLigacao,
        cliente.getTelefone(), false, cliente.getMailing().getCampanha());
    return true;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 6;
  }

}
