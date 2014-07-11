package net.danieljurado.dialer.tratadorespecificocliente;

import net.danieljurado.dialer.modelo.Ligacao;

import org.joda.time.DateTime;

import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public interface TratadorEspecificoCliente {

  boolean isDnc(DaoFactory daoFactory, Cliente cliente, String baseDados);

  void notificaFimTentativa(DaoFactory daoFactory, Ligacao ligacao, Cliente cliente,
      Campanha campanha, DateTime dataBanco, Telefone telefoneOriginal,
      ResultadoLigacao resultadoLigacao, boolean inutilizaComMotivoDiferenciado);

  void notificaFinalizacao(DaoFactory daoFactory, Ligacao ligacao, Cliente cliente,
      Campanha campanha, DateTime dataBanco, Telefone telefoneOriginal,
      ResultadoLigacao resultadoLigacao, boolean inutilizaComMotivoDiferenciado);

  ClienteDao obtemClienteDao(DaoFactory daoFactory);

  boolean reservaNaBaseDoCliente(DaoFactory daoFactory, Cliente cliente);

}
