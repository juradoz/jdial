package al.jdi.dao.beans;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ge;

import java.util.Collection;

import org.hibernate.Session;
import org.joda.time.DateTime;

import al.jdi.dao.HistoricoLigacaoDao;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.HistoricoLigacao;
import al.jdi.dao.model.ResultadoLigacao;

class HistoricoLigacaoDaoImpl extends DaoImpl<HistoricoLigacao> implements HistoricoLigacaoDao {

  HistoricoLigacaoDaoImpl(Session session) {
    super(session, HistoricoLigacao.class);
  }

  @Override
  public void adiciona(HistoricoLigacao historicoLigacao) {
    super.adiciona(historicoLigacao);
    historicoLigacao.getTelefone().getHistoricoLigacao().add(historicoLigacao);
    new TelefoneDaoImpl(getSession()).atualiza(historicoLigacao.getTelefone());
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<HistoricoLigacao> procura(Cliente cliente) {
    return getSession().createCriteria(HistoricoLigacao.class).add(eq("cliente", cliente)).list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<HistoricoLigacao> procura(Cliente cliente, ResultadoLigacao resultadoLigacao) {
    return getSession().createCriteria(HistoricoLigacao.class).add(eq("cliente", cliente))
        .add(eq("resultadoLigacao", resultadoLigacao)).list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<HistoricoLigacao> procura(Cliente cliente, ResultadoLigacao resultadoLigacao,
      DateTime desde) {
    return getSession().createCriteria(HistoricoLigacao.class).add(eq("cliente", cliente))
        .add(eq("resultadoLigacao", resultadoLigacao)).add(ge("inicio", desde)).list();
  }

}
