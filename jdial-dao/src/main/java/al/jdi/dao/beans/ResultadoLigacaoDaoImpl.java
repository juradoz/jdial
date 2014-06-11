package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.ResultadoLigacaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.ResultadoLigacao;

class ResultadoLigacaoDaoImpl extends DaoImpl<ResultadoLigacao> implements ResultadoLigacaoDao {

  ResultadoLigacaoDaoImpl(Session session) {
    super(session, ResultadoLigacao.class);
  }

  @Override
  public ResultadoLigacao procura(Campanha campanha, String nome) {
    return (ResultadoLigacao) getSession().createCriteria(ResultadoLigacao.class)
        .add(eq("campanha", campanha)).add(eq("nome", nome)).uniqueResult();
  }

  @Override
  public ResultadoLigacao procura(int codigo, Campanha campanha) {
    return (ResultadoLigacao) getSession().createCriteria(ResultadoLigacao.class)
        .add(eq("campanha", campanha)).add(eq("codigo", codigo)).uniqueResult();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ResultadoLigacao> listaTudo(Campanha campanha) {
    return getSession().createCriteria(ResultadoLigacao.class).add(eq("campanha", campanha)).list();
  }

}
