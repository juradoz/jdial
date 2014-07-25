package al.jdi.dao.beans;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;

import al.jdi.dao.model.DefinicaoPadrao;
import al.jdi.dao.model.EstadoCliente;
import al.jdi.dao.model.Filtro;
import al.jdi.dao.model.Grupo;
import al.jdi.dao.model.HistoricoCliente;
import al.jdi.dao.model.InformacaoCliente;
import al.jdi.dao.model.Log;
import al.jdi.dao.model.MotivoFinalizacao;
import al.jdi.dao.model.RestricaoHorario;
import al.jdi.dao.model.WebLog;

class DefaultDaoFactory implements DaoFactory {

  private final SessionHandler sessionHandler;
  private Session session;
  private Transaction transaction;

  @Inject
  public DefaultDaoFactory(SessionHandler sessionHandler) {
    this.sessionHandler = sessionHandler;
    session = sessionHandler.getSession();
  }

  @Override
  public void beginTransaction() {
    transaction = session.beginTransaction();
  }

  @Override
  public void close() {
    session.close();
  }

  @Override
  public void commit() {
    transaction.commit();
    transaction = null;
  }

  @Override
  public AgendamentoDao getAgendamentoDao() {
    return new DefaultAgendamentoDao(session);
  }

  @Override
  public AgenteDao getAgenteDao() {
    return new DefaultAgenteDao(session);
  }

  @Override
  public CampanhaDao getCampanhaDao() {
    return new DefaultCampanhaDao(session);
  }

  @Override
  public ClienteDao getClienteDao() {
    return new DefaultClienteDao(session);
  }

  @Override
  public ClienteDaoTsa getClienteDaoTsa() {
    return new DefaultClienteDaoTsa(session);
  }

  @Override
  public DateTime getDataBanco() {
    String hql = "select Now()";
    Query query = session.createSQLQuery(hql);
    return new DateTime(query.uniqueResult());
  }

  @Override
  public DefinicaoDao getDefinicaoDao() {
    return new DefaultDefinicaoDao(session);
  }

  @Override
  public Dao<DefinicaoPadrao> getDefinicaoPadraoDao() {
    return new DefaultDao<DefinicaoPadrao>(session, DefinicaoPadrao.class);
  }

  @Override
  public Dao<EstadoCliente> getEstadoClienteDao() {
    return new DefaultEstadoClienteDao(session);
  }

  @Override
  public Dao<Filtro> getFiltroDao() {
    return new DefaultDao<Filtro>(session, Filtro.class);
  }

  @Override
  public Dao<Grupo> getGrupoDao() {
    return new DefaultGrupoDao(session);
  }

  @Override
  public Dao<HistoricoCliente> getHistoricoClienteDao() {
    return new DefaultHistoricoClienteDao(session);
  }

  @Override
  public HistoricoLigacaoDao getHistoricoLigacaoDao() {
    return new DefaultHistoricoLigacaoDao(session);
  }

  @Override
  public Dao<InformacaoCliente> getInformacaoClienteDao() {
    return new DefaultDao<InformacaoCliente>(session, InformacaoCliente.class);
  }

  @Override
  public Dao<Log> getLogDao() {
    return new DefaultDao<Log>(session, Log.class);
  }

  @Override
  public MailingDao getMailingDao() {
    return new DefaultMailingDao(session);
  }

  @Override
  public Dao<MotivoFinalizacao> getMotivoFinalizacaoDao() {
    return new DefaultMotivoFinalizacaoDao(session);
  }

  @Override
  public ResultadoLigacaoDao getResultadoLigacaoDao() {
    return new DefaultResultadoLigacaoDao(session);
  }

  @Override
  public ServicoDao getServicoDao() {
    return new DefaultServicoDao(session);
  }

  @Override
  public TelefoneDao getTelefoneDao() {
    return new DefaultTelefoneDao(session);
  }

  @Override
  public UsuarioDao getUsuarioDao() {
    return new DefaultUsuarioDao(session);
  }

  @Override
  public boolean hasTransaction() {
    return transaction != null;
  }

  @Override
  public void rollback() {
    transaction.rollback();
    transaction = null;
  }

  @Override
  public void trocaSession(String identifier) {
    if (hasTransaction())
      rollback();
    close();
    session = sessionHandler.getSession(identifier);
  }

  @Override
  public ClienteDaoTsa getClienteDaoTsaCRM() {
    return new DefaultClienteDaoTsaCRM(session);
  }

  @Override
  public AreaAreaDao getAreaAreaDao() {
    return new DefaultAreaAreaDao(session);
  }

  @Override
  public Dao<RestricaoHorario> getRestricaoHorarioDao() {
    return new DefaultRestricaoHorarioDao(session);
  }

  @Override
  public Dao<WebLog> getWebLog() {
    return new DefaultDao<>(session, WebLog.class);
  }
}
