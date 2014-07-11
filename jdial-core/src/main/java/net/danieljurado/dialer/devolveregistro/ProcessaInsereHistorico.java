package net.danieljurado.dialer.devolveregistro;

import javax.inject.Inject;

import net.danieljurado.dialer.modelo.Ligacao;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.HistoricoLigacao;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaInsereHistorico implements ProcessoDevolucao {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  ProcessaInsereHistorico() {}

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    if (!resultadoLigacao.isInsereHistorico()) {
      logger.info("Nao vai inserir historico {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    try {
      logger.info("Inserindo historico {} {}", resultadoLigacao, cliente);
      HistoricoLigacao historicoLigacao = new HistoricoLigacao();
      historicoLigacao.setAtendimento(ligacao.getAtendimento() == null ? null : ligacao
          .getAtendimento());
      historicoLigacao.setCliente(cliente);
      historicoLigacao.setInicio(ligacao.getInicio());
      historicoLigacao.setResultadoLigacao(resultadoLigacao);
      historicoLigacao.setTelefone(cliente.getTelefone());
      historicoLigacao.setTermino(ligacao.getTermino() == null ? new DateTime() : ligacao
          .getTermino());
      historicoLigacao.setAgente(ligacao.getAgente());
      daoFactory.getHistoricoLigacaoDao().adiciona(historicoLigacao);
      logger.info("Inseriu historico {} {}", resultadoLigacao, cliente);
      return true;
    } catch (Exception e) {
      logger.error("Erro na insercao do historico: {} {}", e.getMessage(), cliente);
      return true;
    }
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 0;
  }

}
