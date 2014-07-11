package net.danieljurado.dialer.devolveregistro;

import java.util.Map;

import javax.inject.Inject;

import net.danieljurado.dialer.modelo.Ligacao;
import net.danieljurado.dialer.modelo.Providencia;
import net.danieljurado.dialer.modelo.Providencia.NaoPodeReiniciarRodadaTelefoneException;
import net.danieljurado.dialer.modelo.Providencia.SemProximoTelefoneException;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

class ProcessaCiclaTelefone implements ProcessoDevolucao {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TratadorEspecificoCliente tratadorEspecificoCliente;
  private final ProcessaFimDaFila processaFimDaFila;
  private final Map<Providencia.Codigo, Providencia> providencias;

  @Inject
  ProcessaCiclaTelefone(TratadorEspecificoCliente tratadorEspecificoCliente,
      ProcessaFimDaFila processaFimDaFila, Map<Providencia.Codigo, Providencia> providencias) {
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
    this.processaFimDaFila = processaFimDaFila;
    this.providencias = providencias;
  }

  @Override
  public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    ligacao.setTelefoneOriginal(cliente.getTelefone());
    if (!resultadoLigacao.isCiclaTelefone()) {
      logger.info("Nao vai ciclar telefone {}", cliente);
      return false;
    }
    return true;
  }

  @Override
  public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
      DaoFactory daoFactory) {
    logger.info("Ciclando telefone {}", cliente);
    Providencia.Codigo codigo =
        Providencia.Codigo.fromValue(cliente.getInformacaoCliente().getProvidenciaTelefone());
    if (codigo.equals(Providencia.Codigo.MANTEM_ATUAL))
      codigo = Providencia.Codigo.PROXIMO_TELEFONE;

    Providencia providencia = providencias.get(codigo);

    try {
      cliente.setTelefone(providencia.getTelefone(daoFactory, cliente));
    } catch (NaoPodeReiniciarRodadaTelefoneException e) {
      logger.info("Nao pode passar pro proximotelefone ainda {}", cliente);
      processaFimDaFila.executa(ligacao, cliente, null, daoFactory);
    } catch (SemProximoTelefoneException e) {
      logger.info("Nao possui proximo telefone {}", cliente);
      processaFimDaFila.executa(ligacao, cliente, null, daoFactory);
    }
    logger.info("Consegui ciclar telefone {}", cliente);
    tratadorEspecificoCliente.obtemClienteDao(daoFactory).atualiza(cliente);
    return true;
  }

  @Override
  public int compareTo(ProcessoDevolucao o) {
    return new CompareToBuilder().append(getOrdem(), o.getOrdem()).toComparison();
  }

  @Override
  public int getOrdem() {
    return 9;
  }

}
