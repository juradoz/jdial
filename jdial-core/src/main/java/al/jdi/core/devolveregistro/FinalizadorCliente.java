package al.jdi.core.devolveregistro;

import javax.inject.Inject;

import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.TelefoneDao;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.EstadoCliente;
import al.jdi.dao.model.HistoricoCliente;
import al.jdi.dao.model.MotivoFinalizacao;
import al.jdi.dao.model.Telefone;

class FinalizadorCliente {

  public static class ClienteFinalizadoException extends RuntimeException {
    private static final long serialVersionUID = -358709782586621212L;
  }

  private final Logger logger;
  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
  private final TelefoneFilter telefoneFilter;

  @Inject
  FinalizadorCliente(Logger logger,
      TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory,
      TelefoneFilter telefoneFilter) {
    this.logger = logger;
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
    this.telefoneFilter = telefoneFilter;
  }

  void finaliza(Configuracoes configuracoes, DaoFactory daoFactory, Cliente cliente,
      MotivoFinalizacao motivoFinalizacao) {
    EstadoCliente estadoCliente = daoFactory.getEstadoClienteDao().procura("Finalizado");
    cliente.setEstadoCliente(estadoCliente);

    cliente.getAgendamento().clear();
    tratadorEspecificoClienteFactory.create(configuracoes, daoFactory).obtemClienteDao()
        .atualiza(cliente);

    HistoricoCliente historicoCliente = new HistoricoCliente();
    historicoCliente.setCliente(cliente);
    historicoCliente.setEstadoCliente(estadoCliente);
    historicoCliente.setMotivoFinalizacao(motivoFinalizacao);
    historicoCliente.setDescricao(motivoFinalizacao == null ? "Sem motivo de finalizacao" : String
        .format("Finalizacao por %s", motivoFinalizacao.getNome()));
    daoFactory.getHistoricoClienteDao().adiciona(historicoCliente);
  }

  void finalizaPorInutilizacaoSimples(Configuracoes configuracoes, DaoFactory daoFactory,
      Cliente cliente) throws ClienteFinalizadoException {
    TelefoneDao telefoneDao = daoFactory.getTelefoneDao();
    Telefone telefone = telefoneDao.procura(cliente.getTelefone().getId());
    telefone.setUtil(false);
    telefoneDao.atualiza(telefone);
    if (telefoneFilter.filter(configuracoes, cliente.getTelefones()).size() > 0) {
      logger.info(
          "Nao vai finalizar por inutilizacao simples pois ainda possui outros telefones {}",
          cliente);
      return;
    }

    MotivoFinalizacao motivoFinalizacao =
        daoFactory.getMotivoFinalizacaoDao().procura("Sem telefones");

    finaliza(configuracoes, daoFactory, cliente, motivoFinalizacao);

    throw new ClienteFinalizadoException();
  }
}
