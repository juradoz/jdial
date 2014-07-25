package al.jdi.core.tratadorespecificocliente;

import org.joda.time.DateTime;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public interface TratadorEspecificoCliente {

  public interface Factory {
    TratadorEspecificoCliente create(Tenant tenant, DaoFactory daoFactory);
  }

  boolean isDnc(Cliente cliente);

  void notificaFimTentativa(Tenant tenant, Ligacao ligacao, Cliente cliente, DateTime dataBanco,
      Telefone telefoneOriginal, ResultadoLigacao resultadoLigacao,
      boolean inutilizaComMotivoDiferenciado);

  void notificaFinalizacao(Tenant tenant, Ligacao ligacao, Cliente cliente, DateTime dataBanco,
      Telefone telefoneOriginal, ResultadoLigacao resultadoLigacao,
      boolean inutilizaComMotivoDiferenciado);

  ClienteDao obtemClienteDao();

  boolean reservaNaBaseDoCliente(Cliente cliente);

}
