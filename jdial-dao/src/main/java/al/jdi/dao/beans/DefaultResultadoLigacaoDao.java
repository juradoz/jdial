package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.ResultadoLigacao;

class DefaultResultadoLigacaoDao implements ResultadoLigacaoDao {

  private final DefaultDao<ResultadoLigacao> dao;

  DefaultResultadoLigacaoDao(Session session) {
    dao = new DefaultDao<>(session, ResultadoLigacao.class);
  }

  @Override
  public ResultadoLigacao procura(Campanha campanha, String nome) {
    return (ResultadoLigacao) dao.getSession().createCriteria(ResultadoLigacao.class)
        .add(eq("campanha", campanha)).add(eq("nome", nome)).uniqueResult();
  }

  @Override
  public ResultadoLigacao procura(int codigo, Campanha campanha) {
    return (ResultadoLigacao) dao.getSession().createCriteria(ResultadoLigacao.class)
        .add(eq("campanha", campanha)).add(eq("codigo", codigo)).uniqueResult();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ResultadoLigacao> listaTudo(Campanha campanha) {
    return dao.getSession().createCriteria(ResultadoLigacao.class).add(eq("campanha", campanha))
        .list();
  }

  @Override
  public void adiciona(ResultadoLigacao t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(ResultadoLigacao t) {
    dao.atualiza(t);
  }

  @Override
  public List<ResultadoLigacao> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public ResultadoLigacao procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(ResultadoLigacao u) {
    dao.remove(u);
  }

  @Override
  public ResultadoLigacao procura(String s) {
    return dao.procura(s);
  }

}
