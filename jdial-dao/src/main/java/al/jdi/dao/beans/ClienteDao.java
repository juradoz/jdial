package al.jdi.dao.beans;

import java.util.Collection;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;

public interface ClienteDao extends Dao<Cliente> {

  void limpaReserva(Cliente cliente, int operadorDiscador, String nomeBaseDados);

  void limpaReservas(Campanha campanha, String nomeBaseDados, String nomeBase, int operador);

  int limpezaTemporaria(Campanha campanha, String nomeBaseDados, String nomeBase);

  Collection<Cliente> obtemAGGs(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador);

  Collection<Cliente> obtemLivres(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador);

  String getDigitoSaida(Cliente cliente);

}
