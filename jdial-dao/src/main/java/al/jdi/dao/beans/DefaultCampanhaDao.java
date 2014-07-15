package al.jdi.dao.beans;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Definicao;
import al.jdi.dao.model.DefinicaoPadrao;

class DefaultCampanhaDao implements CampanhaDao {

  private final DefaultDao<Campanha> dao;

  public DefaultCampanhaDao(Session session) {
    this(new DefaultDao<Campanha>(session, Campanha.class));
  }

  DefaultCampanhaDao(DefaultDao<Campanha> dao) {
    this.dao = dao;
  }

  @Override
  public void adiciona(Campanha campanha) {
    dao.adiciona(campanha);
    for (DefinicaoPadrao definicaoPadrao : new DefaultDao<DefinicaoPadrao>(dao.getSession(),
        DefinicaoPadrao.class).listaTudo()) {
      Definicao definicao = new Definicao();
      definicao.setCampanha(campanha);
      definicao.setPropriedade(definicaoPadrao.getPropriedade());
      definicao.setValor(definicaoPadrao.getValor());
      campanha.getDefinicao().add(definicao);
    }
    atualiza(campanha);
  }

  @Override
  public Campanha procura(String nome) {
    return dao.procura(nome);
  }

  @Override
  public void atualiza(Campanha t) {
    dao.atualiza(t);
  }

  @Override
  public List<Campanha> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Campanha procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Campanha u) {
    dao.remove(u);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Campanha> listaAtivas() {
    return dao.getSession().createCriteria(Campanha.class).add(Restrictions.eq("ativa", true))
        .list();
  }

}
