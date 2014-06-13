package al.jdi.dao;


import org.joda.time.DateTime;

import al.jdi.dao.model.DefinicaoPadrao;
import al.jdi.dao.model.InformacaoCliente;
import al.jdi.dao.model.Log;

public interface DaoFactory {

  void beginTransaction();

  void close();

  void commit();

  AgendamentoDao getAgendamentoDao();

  AgenteDao getAgenteDao();

  AreaAreaDao getAreaAreaDao();

  CampanhaDao getCampanhaDao();

  ClienteDao getClienteDao();

  ClienteDao getClienteDaoPorto();

  ClienteDaoTsa getClienteDaoTsa();

  DateTime getDataBanco();

  DefinicaoDao getDefinicaoDao();

  EstadoClienteDao getEstadoClienteDao();

  GrupoDao getGrupoDao();

  HistoricoLigacaoDao getHistoricoLigacaoDao();

  Dao<InformacaoCliente> getInformacaoClienteDao();

  Dao<Log> getLogDao();

  MailingDao getMailingDao();

  ResultadoLigacaoDao getResultadoLigacaoDao();

  ServicoDao getServicoDao();

  TelefoneDao getTelefoneDao();

  UsuarioDao getUsuarioDao();

  boolean hasTransaction();

  void rollback();

  void trocaSession(String identifier);

  Dao<DefinicaoPadrao> getDefinicaoPadraoDao();

}
