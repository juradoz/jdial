package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ge;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.joda.time.DateTime;

import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.HistoricoLigacao;
import al.jdi.dao.model.ResultadoLigacao;

class DefaultHistoricoLigacaoDao implements HistoricoLigacaoDao {

  private final DefaultDao<HistoricoLigacao> dao;

  DefaultHistoricoLigacaoDao(Session session) {
    this.dao = new DefaultDao<>(session, HistoricoLigacao.class);
  }

  @Override
  public void adiciona(HistoricoLigacao historicoLigacao) {
    dao.adiciona(historicoLigacao);
    historicoLigacao.getTelefone().getHistoricoLigacao().add(historicoLigacao);
    new DefaultTelefoneDao(dao.getSession()).atualiza(historicoLigacao.getTelefone());
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<HistoricoLigacao> procura(Cliente cliente) {
    return dao.getSession().createCriteria(HistoricoLigacao.class).add(eq("cliente", cliente))
        .list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<HistoricoLigacao> procura(Cliente cliente, ResultadoLigacao resultadoLigacao) {
    return dao.getSession().createCriteria(HistoricoLigacao.class).add(eq("cliente", cliente))
        .add(eq("resultadoLigacao", resultadoLigacao)).list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<HistoricoLigacao> procura(Cliente cliente, ResultadoLigacao resultadoLigacao,
      DateTime desde) {
    return dao.getSession().createCriteria(HistoricoLigacao.class).add(eq("cliente", cliente))
        .add(eq("resultadoLigacao", resultadoLigacao)).add(ge("inicio", desde)).list();
  }

  @Override
  public void atualiza(HistoricoLigacao t) {
    dao.atualiza(t);
  }

  @Override
  public List<HistoricoLigacao> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public HistoricoLigacao procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(HistoricoLigacao u) {
    dao.remove(u);
  }

  @Override
  public HistoricoLigacao procura(String s) {
    return dao.procura(s);
  }

}
