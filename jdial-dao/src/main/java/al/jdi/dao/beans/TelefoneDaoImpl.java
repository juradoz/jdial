package al.jdi.dao.beans;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static org.apache.commons.collections.ComparatorUtils.chainedComparator;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.TelefoneDao;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.Telefone;
import ch.lambdaj.function.compare.ArgumentComparator;

class TelefoneDaoImpl extends DaoImpl<Telefone> implements TelefoneDao {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  TelefoneDaoImpl(Session session) {
    super(session, Telefone.class);
  }

  @Override
  public void adiciona(Telefone telefone) {
    super.adiciona(telefone);
    telefone.getCliente().getTelefones().add(telefone);
    if (telefone.getCliente().getTelefone() == null)
      telefone.getCliente().setTelefone(telefone);
    new ClienteDaoImpl(getSession()).atualiza(telefone.getCliente());
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public Collection<Telefone> telefonesUteis(boolean excluiCelular, Cliente cliente) {
    String hql = "from Telefone as t where t.cliente = :cliente and t.util is true";
    Query query = getSession().createQuery(hql);
    query.setEntity("cliente", cliente);
    Collection<Telefone> result = new LinkedList<Telefone>();
    Collection<Telefone> telefones = query.list();
    // OrdemDao ordemDao = new OrdemDaoImpl(getSession());
    for (Telefone telefone : telefones) {
      // Ordem ordem = ordemDao.procura(telefone.getTipoTelefone());
      // ordem.isAtivo() &&
      boolean isCelular = telefone.isCelular();
      logger.debug("telefone: {} excluiCelular: {} telefone.isCelular: {}", new Object[] {telefone,
          excluiCelular, isCelular});
      if (!(excluiCelular && isCelular)) {
        logger.debug("Vai adicionar telefone {}", telefone.getTelefone());
        result.add(telefone);
      } else {
        logger.debug("Nao vai adicionar telefone {}", telefone.getTelefone());
      }
    }
    return ordena(result);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  Collection<Telefone> ordena(Collection<Telefone> result) {
    ArgumentComparator byPrioridade = new ArgumentComparator(on(Telefone.class).getPrioridade());
    ArgumentComparator byId = new ArgumentComparator(on(Telefone.class).getIdTelefone());
    Comparator orderBy = chainedComparator(byPrioridade, byId);
    return sort(result, on(Telefone.class), orderBy);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Telefone> telefonesUteis(Cliente cliente) {
    return ordena(getSession().createCriteria(Telefone.class).add(eq("cliente", cliente))
        .add(eq("util", true)).list());
  }

  @Override
  public int totalTentativas(boolean excluiCelular, Cliente cliente) {
    Collection<Telefone> telefones = telefonesUteis(excluiCelular, cliente);
    int tentativas = 0;
    for (Telefone telefone : telefones)
      tentativas += telefone.getTentativa();
    return tentativas;
  }

  @Override
  public Telefone procura(Mailing mailing, long chaveTelefone, String ddd, String telefone) {
    return (Telefone) getSession().createCriteria(Telefone.class)
        .add(eq("chaveTelefone", chaveTelefone)).add(eq("ddd", ddd)).add(eq("telefone", telefone))
        .uniqueResult();
  }

}
