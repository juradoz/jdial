package al.jdi.dao.beans;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static org.apache.commons.collections.ComparatorUtils.chainedComparator;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.Telefone;
import ch.lambdaj.function.compare.ArgumentComparator;

class DefaultTelefoneDao implements TelefoneDao {

  private final DefaultDao<Telefone> dao;

  DefaultTelefoneDao(Session session) {
    this.dao = new DefaultDao<>(session, Telefone.class);
  }

  @Override
  public void adiciona(Telefone telefone) {
    dao.adiciona(telefone);
    telefone.getCliente().getTelefones().add(telefone);
    if (telefone.getCliente().getTelefone() == null)
      telefone.getCliente().setTelefone(telefone);
    new DefaultClienteDao(dao.getSession()).atualiza(telefone.getCliente());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  Collection<Telefone> ordena(Collection<Telefone> result) {
    ArgumentComparator byPrioridade = new ArgumentComparator(on(Telefone.class).getPrioridade());
    ArgumentComparator byId = new ArgumentComparator(on(Telefone.class).getId());
    Comparator orderBy = chainedComparator(byPrioridade, byId);
    return sort(result, on(Telefone.class), orderBy);
  }

  @Override
  public int totalTentativas(boolean excluiCelular, Cliente cliente) {
    int tentativas = 0;
    for (Telefone telefone : cliente.getTelefones())
      tentativas += telefone.getTentativa();
    return tentativas;
  }

  @Override
  public Telefone procura(Mailing mailing, long chaveTelefone, String ddd, String telefone) {
    return (Telefone) dao.getSession().createCriteria(Telefone.class)
        .add(eq("chaveTelefone", chaveTelefone)).add(eq("ddd", ddd)).add(eq("telefone", telefone))
        .uniqueResult();
  }

  @Override
  public void atualiza(Telefone t) {
    dao.atualiza(t);
  }

  @Override
  public List<Telefone> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Telefone procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Telefone u) {
    dao.remove(u);
  }

  @Override
  public Telefone procura(String s) {
    return dao.procura(s);
  }

}
