package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import org.hibernate.Session;

import al.jdi.dao.CampanhaDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Definicao;
import al.jdi.dao.model.DefinicaoPadrao;

class CampanhaDaoImpl extends DaoImpl<Campanha> implements CampanhaDao {

  CampanhaDaoImpl(Session session) {
    super(session, Campanha.class);
  }

  @Override
  public void adiciona(Campanha campanha) {
    super.adiciona(campanha);
    for (DefinicaoPadrao definicaoPadrao : new DaoImpl<DefinicaoPadrao>(getSession(),
        DefinicaoPadrao.class).listaTudo()) {
      Definicao definicao = new Definicao();
      definicao.setCampanha(campanha);
      definicao.setNivelAcessoRequerido(definicaoPadrao.getNivelAcessoRequerido());
      definicao.setPropriedade(definicaoPadrao.getPropriedade());
      definicao.setValor(definicaoPadrao.getValor());
      campanha.getDefinicao().add(definicao);
    }
    atualiza(campanha);
  }

  @Override
  public Campanha procura(String nome) {
    return (Campanha) getSession().createCriteria(Campanha.class).add(eq("nome", nome))
        .uniqueResult();
  }

}
