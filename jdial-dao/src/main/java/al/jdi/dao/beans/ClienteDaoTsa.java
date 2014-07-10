package al.jdi.dao.beans;

import org.joda.time.DateTime;

import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Situacao;
import al.jdi.dao.model.Telefone;

public interface ClienteDaoTsa extends ClienteDao {

  void insereResultadoTsa(Cliente cliente, ResultadoLigacao resultadoLigacao, Telefone telefone,
      DateTime inicioDiscagem, Situacao situacao, int motivo, int motivoFinalizacao,
      String nomeBaseDados, int operadorDiscador, int motivoCampanha);

  boolean isDnc(Cliente cliente, String nomeBaseDados);

  void liberaNaBaseDoCliente(Cliente cliente, String nomeBaseDados, int operadorDiscador);

  boolean reservaNaBaseDoCliente(Cliente cliente, int operadorDiscador, String nomeBaseDados);

}
