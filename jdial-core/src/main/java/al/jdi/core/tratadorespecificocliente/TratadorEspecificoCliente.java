package al.jdi.core.tratadorespecificocliente;

import org.joda.time.DateTime;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public interface TratadorEspecificoCliente {

  boolean isDnc(DaoFactory daoFactory, Cliente cliente, String baseDados);

  void notificaFimTentativa(Configuracoes configuracoes, DaoFactory daoFactory, Ligacao ligacao,
      Cliente cliente, Campanha campanha, DateTime dataBanco, Telefone telefoneOriginal,
      ResultadoLigacao resultadoLigacao, boolean inutilizaComMotivoDiferenciado);

  void notificaFinalizacao(Configuracoes configuracoes, DaoFactory daoFactory, Ligacao ligacao,
      Cliente cliente, Campanha campanha, DateTime dataBanco, Telefone telefoneOriginal,
      ResultadoLigacao resultadoLigacao, boolean inutilizaComMotivoDiferenciado);

  ClienteDao obtemClienteDao(Configuracoes configuracoes, DaoFactory daoFactory);

  boolean reservaNaBaseDoCliente(Configuracoes configuracoes, DaoFactory daoFactory, Cliente cliente);

}
