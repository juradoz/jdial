package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.DefinicaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Definicao;

class DefinicaoDaoImpl extends DaoImpl<Definicao> implements DefinicaoDao {

  DefinicaoDaoImpl(Session session) {
    super(session, Definicao.class);
  }

  @Override
  public void adiciona(Definicao definicao) {
    super.adiciona(definicao);
    adicionaCampanha(definicao);
  }

  private void adicionaCampanha(Definicao definicao) {
    definicao.getCampanha().getDefinicao().add(definicao);
    new CampanhaDaoImpl(getSession()).atualiza(definicao.getCampanha());
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Definicao> listaTudo(Campanha campanha) {
    return getSession().createCriteria(Definicao.class).add(eq("campanha", campanha)).list();
  }

  @Override
  public Definicao procura(Campanha campanha, String propriedade) {
    return (Definicao) getSession().createCriteria(Definicao.class).add(eq("campanha", campanha))
        .add(eq("propriedade", propriedade)).uniqueResult();
  }

}
