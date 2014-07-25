package al.jdi.core.devolveregistro;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaSemTelefones implements ProcessoDevolucao {

  private static final Logger logger = getLogger(ProcessaSemTelefones.class);

  private final FinalizadorCliente finalizadorCliente;
  private final NotificadorCliente notificadorCliente;

  @Inject
  ProcessaSemTelefones(FinalizadorCliente finalizadorCliente, NotificadorCliente notificadorCliente) {
    this.finalizadorCliente = finalizadorCliente;
    this.notificadorCliente = notificadorCliente;
  }

  @Override
  public boolean accept(Configuracoes configuracoes, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, DaoFactory daoFactory) {
    return ligacao.getMotivoFinalizacao() == MotivoSistema.SEM_TELEFONES.getCodigo();
  }

  @Override
  public boolean executa(Configuracoes configuracoes, Ligacao ligacao, Cliente cliente,
      ResultadoLigacao resultadoLigacao, DaoFactory daoFactory) {
    logger.info("Finalizando por sem telefones {}", cliente);
    finalizadorCliente.finaliza(configuracoes, daoFactory, cliente, daoFactory
        .getMotivoFinalizacaoDao().procura("Sem telefones"));
    notificadorCliente.notificaFinalizacao(configuracoes, daoFactory, ligacao, cliente,
        resultadoLigacao, cliente.getTelefone(), false, cliente.getMailing().getCampanha());
    return false;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 1;
  }

}
