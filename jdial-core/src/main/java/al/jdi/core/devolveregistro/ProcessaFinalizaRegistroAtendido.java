package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.MotivoFinalizacao;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaFinalizaRegistroAtendido implements ProcessoDevolucao {

  private static final Logger logger = getLogger(ProcessaFinalizaRegistroAtendido.class);

  private final FinalizadorCliente finalizadorCliente;
  private final NotificadorCliente notificadorCliente;

  @Inject
  ProcessaFinalizaRegistroAtendido(FinalizadorCliente finalizadorCliente,
      NotificadorCliente notificadorCliente) {
    this.finalizadorCliente = finalizadorCliente;
    this.notificadorCliente = notificadorCliente;
  }

  @Override
  public boolean accept(Tenant tenant, Ligacao ligacao, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    return tenant.getConfiguracoes().getFinalizaRegistroAtendido() && ligacao.isAtendida();
  }

  @Override
  public boolean executa(Tenant tenant, Ligacao ligacao, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    Cliente cliente = ligacao.getDiscavel().getCliente();
    MotivoFinalizacao motivo = daoFactory.getMotivoFinalizacaoDao().procura("Atendimento");
    finalizadorCliente.finaliza(tenant, daoFactory, cliente, motivo);
    logger.info("Finalizado {}", cliente);
    notificadorCliente.notificaFinalizacao(tenant, daoFactory, ligacao, cliente, resultadoLigacao,
        cliente.getTelefone(), false, cliente.getMailing().getCampanha());
    return false;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 5;
  }

}
