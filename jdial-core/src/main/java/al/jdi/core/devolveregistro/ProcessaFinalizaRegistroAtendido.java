package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.MotivoFinalizacao;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaFinalizaRegistroAtendido implements ProcessoDevolucao {

  private final Logger logger;
  private final Configuracoes configuracoes;
  private final FinalizadorCliente finalizadorCliente;
  private final NotificadorCliente notificadorCliente;

  @Inject
  ProcessaFinalizaRegistroAtendido(Logger logger, Configuracoes configuracoes,
      FinalizadorCliente finalizadorCliente, NotificadorCliente notificadorCliente) {
    this.logger = logger;
    this.configuracoes = configuracoes;
    this.finalizadorCliente = finalizadorCliente;
    this.notificadorCliente = notificadorCliente;
  }

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    return configuracoes.getFinalizaRegistroAtendido() && ligacao.isAtendida();
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    MotivoFinalizacao motivo = daoFactory.getMotivoFinalizacaoDao().procura("Atendimento");
    finalizadorCliente.finaliza(daoFactory, cliente, motivo);
    logger.info("Finalizado {}", cliente);
    notificadorCliente.notificaFinalizacao(daoFactory, ligacao, cliente, resultadoLigacao,
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
