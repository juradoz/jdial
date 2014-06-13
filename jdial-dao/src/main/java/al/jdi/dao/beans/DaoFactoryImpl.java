package al.jdi.dao.beans;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;

import al.jdi.dao.AgendamentoDao;
import al.jdi.dao.AgenteDao;
import al.jdi.dao.AreaAreaDao;
import al.jdi.dao.CampanhaDao;
import al.jdi.dao.ClienteDao;
import al.jdi.dao.ClienteDaoTsa;
import al.jdi.dao.Dao;
import al.jdi.dao.DaoFactory;
import al.jdi.dao.DefinicaoDao;
import al.jdi.dao.EstadoClienteDao;
import al.jdi.dao.GrupoDao;
import al.jdi.dao.HistoricoLigacaoDao;
import al.jdi.dao.MailingDao;
import al.jdi.dao.ResultadoLigacaoDao;
import al.jdi.dao.ServicoDao;
import al.jdi.dao.SessionHandler;
import al.jdi.dao.TelefoneDao;
import al.jdi.dao.UsuarioDao;
import al.jdi.dao.model.DefinicaoPadrao;
import al.jdi.dao.model.InformacaoCliente;
import al.jdi.dao.model.Log;

class DaoFactoryImpl implements DaoFactory {

  private final SessionHandler sessionHandler;
  private Session session;
  private Transaction transaction;

  @Inject
  public DaoFactoryImpl(SessionHandler sessionHandler) {
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
    return new AgendamentoDaoImpl(session);
  }

  @Override
  public AgenteDao getAgenteDao() {
    return new AgenteDaoImpl(session);
  }

  @Override
  public CampanhaDao getCampanhaDao() {
    return new CampanhaDaoImpl(session);
  }

  @Override
  public ClienteDao getClienteDao() {
    return new ClienteDaoImpl(session);
  }

  @Override
  public ClienteDao getClienteDaoPorto() {
    return new ClienteDaoPortoImpl(session);
  }

  @Override
  public ClienteDaoTsa getClienteDaoTsa() {
    return new ClienteDaoTsaImpl(session);
  }

  @Override
  public DateTime getDataBanco() {
    String hql = "select Now()";
    Query query = session.createSQLQuery(hql);
    return new DateTime(query.uniqueResult());
  }


  @Override
  public DefinicaoDao getDefinicaoDao() {
    return new DefinicaoDaoImpl(session);
  }

  @Override
  public Dao<DefinicaoPadrao> getDefinicaoPadraoDao() {
    return new DaoImpl<DefinicaoPadrao>(session, DefinicaoPadrao.class);
  }

  @Override
  public EstadoClienteDao getEstadoClienteDao() {
    return new EstadoClienteDaoImpl(session);
  }

  @Override
  public GrupoDao getGrupoDao() {
    return new GrupoDaoImpl(session);
  }

  @Override
  public HistoricoLigacaoDao getHistoricoLigacaoDao() {
    return new HistoricoLigacaoDaoImpl(session);
  }

  @Override
  public Dao<InformacaoCliente> getInformacaoClienteDao() {
    return new DaoImpl<InformacaoCliente>(session, InformacaoCliente.class);
  }

  @Override
  public Dao<Log> getLogDao() {
    return new DaoImpl<Log>(session, Log.class);
  }

  @Override
  public MailingDao getMailingDao() {
    return new MailingDaoImpl(session);
  }

  @Override
  public ResultadoLigacaoDao getResultadoLigacaoDao() {
    return new ResultadoLigacaoDaoImpl(session);
  }

  @Override
  public ServicoDao getServicoDao() {
    return new ServicoDaoImpl(session);
  }

  @Override
  public TelefoneDao getTelefoneDao() {
    return new TelefoneDaoImpl(session);
  }

  @Override
  public UsuarioDao getUsuarioDao() {
    return new UsuarioDaoImpl(session);
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
  public AreaAreaDao getAreaAreaDao() {
    return new AreaAreaDaoImpl(session);
  }
}
