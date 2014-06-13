package al.jdi.dao.beans;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.Collection;

import org.hibernate.Session;

import al.jdi.dao.ClienteDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;

class ClienteDaoPortoImpl extends ClienteDaoImpl implements ClienteDao {

  ClienteDaoPortoImpl(Session session) {
    super(session);
  }

  @Override
  public Collection<Cliente> obtemAGGs(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador) {
    return super.obtemLivres(quantidade, campanha, "Operador", EMPTY, 3);
  }

}
