package al.jdi.dao;

import java.util.Collection;

import al.jdi.dao.model.Agente;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;

public interface ClienteDao extends Dao<Cliente> {

  void limpaReserva(Cliente cliente, int operadorDiscador, String nomeBaseDados);

  void limpaReservas(Campanha campanha, String nomeBaseDados, String nomeBase, int operador);

  int limpezaTemporaria(Campanha campanha, String nomeBaseDados, String nomeBase);

  Collection<Cliente> listaTudo(Campanha campanha, int maxResults);

  Collection<Cliente> obtemAGGs(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador);

  Collection<Cliente> obtemAGPs(int quantidade, Agente agente, Campanha campanha);

  Collection<Cliente> obtemLivres(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador);

  Collection<Cliente> obtemLivresMhc(int quantidade, Campanha campanha);

  Cliente procura(Mailing mailing, String chave);

  void retornaReservadosOperador(Campanha campanha);

  String getDigitoSaida(Cliente cliente);

}
