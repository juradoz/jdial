package al.jdi.dao.beans;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Situacao;
import al.jdi.dao.model.Telefone;

class DefaultClienteDaoTsaCRM implements ClienteDaoTsa {

  private static final Logger logger = LoggerFactory.getLogger(DefaultClienteDaoTsaCRM.class);

  private final DefaultClienteDao dao;

  DefaultClienteDaoTsaCRM(Session session) {
    this.dao = new DefaultClienteDao(session);
  }

  @Override
  public String getDigitoSaida(Cliente cliente) {
    return EMPTY;
  }

  @Override
  public void insereResultadoTsa(Cliente cliente, ResultadoLigacao resultadoLigacao,
      Telefone telefone, DateTime inicioDiscagem, Situacao situacao, int motivoDiscador,
      int motivoFinalizacao, String nomeBaseDados, int operadorDiscador, int motivoCampanha) {
    int codigo = cliente.getInformacaoCliente().getChave();

    int codCampanha = cliente.getInformacaoCliente().getSplitCodCampanha();
    int codCliente = cliente.getInformacaoCliente().getSplitCodCliente();
    if (codigo == 0 || codCliente == 0)
      return;

    updateDetCampanha(resultadoLigacao, cliente, situacao, motivoDiscador, motivoFinalizacao,
        operadorDiscador, codigo, nomeBaseDados);

    logger.debug("Insert Ocorrencias para {} ...", cliente);
    dao.getSession()
        .createSQLQuery(
            String.format("insert into %s.Ocorrencias SET " + "Data = Now(), " + "Hora = Now(), "
                + "HoraFim = Now(), " + "CodOp = :operador, " + "CodTrouble = '72', "
                + "NomeOper = 'Preditivo', " + "Obs = '', " + "Resolvido = 'S', "
                + "NomeGrava = '', " + "Email = '15', " + "CodPro = '0', " + "Manual = '1', "
                + "Campanha = :codCampanha, " + "CodCli = :codCliente1, "
                + "CodMot = :motivoCampanha, " + "CodDetMot = :motivoDiscador, "
                + "Finaliza = 11, " + "AUX01 = '', " + "VDN = '', " + "Split = '', "
                + "Indicacao = 0, " + "CodigoCliente = :codCliente2, " + "Processo = '0', "
                + "Agenda = 0, " + "CodigoClienteOld = 0", nomeBaseDados))
        .setInteger("operador", operadorDiscador).setInteger("codCampanha", codCampanha)
        .setInteger("codCliente1", codCliente).setInteger("motivoCampanha", motivoCampanha)
        .setInteger("motivoDiscador", motivoDiscador).setInteger("codCliente2", codCliente)
        .executeUpdate();
    logger.debug("Insert Ocorrencias para {} com sucesso!", cliente);

    String hql = "SELECT LAST_INSERT_ID() as ID";
    Query query = dao.getSession().createSQLQuery(hql);
    BigInteger id = (BigInteger) query.uniqueResult();

    hql =
        String.format("update %s.Telefones set " + "dataHoraUC = Now(), " + "motivo = 11 "
            + "where codigo = :codTelefone", nomeBaseDados);
    query = dao.getSession().createSQLQuery(hql);
    query.setLong("codTelefone", telefone.getChaveTelefone());
    logger.debug("Update Telefones para {}...", cliente);
    query.executeUpdate();
    logger.debug("Update Telefones para {} com sucesso!", cliente);

    hql =
        String.format("insert into %s.Chamadas SET " + "motivo = 11, "
            + "telefone = :codTelefone, " + "operador = :operador, " + "cliente = :codCliente, "
            + "dataHora = Now(), " + "Ocorrencia = :id, " + "Power = 0;", nomeBaseDados);
    query = dao.getSession().createSQLQuery(hql);
    query.setLong("codTelefone", telefone.getChaveTelefone());
    query.setInteger("operador", operadorDiscador);
    query.setInteger("codCliente", codCliente);
    query.setInteger("id", id.intValue());
    logger.debug("Insert Chamadas para {} ...", cliente);
    query.executeUpdate();
    logger.debug("Insert Chamadas para {} com sucesso!", cliente);
  }

  @Override
  public boolean isDnc(Cliente cliente, String nomeBaseDados) {
    String hql =
        "select count(Codigo) from Operador.DNC " + "where " + "  ddd = :ddd and "
            + "  fone = :fone and " + "  Desbloqueado is null";
    BigInteger qtdDnc =
        (BigInteger) dao.getSession().createSQLQuery(hql)
            .setString("ddd", cliente.getTelefone().getDdd())
            .setString("fone", cliente.getTelefone().getTelefone()).uniqueResult();
    logger.debug("qtdDnc = {} para cliente {} ddd {} telefone {}", new Object[] {qtdDnc, cliente,
        cliente.getTelefone().getDdd(), cliente.getTelefone()});

    return qtdDnc.intValue() != 0;
  }

  @Override
  public void liberaNaBaseDoCliente(Cliente cliente, String nomeBaseDados, int operadorDiscador) {
    String hql =
        String.format("update %s.DetCampanha set " + "operadorCtt = 0 "
            + "where Codigo = :codigo and " + "operadorCtt = :operador", nomeBaseDados);
    Query query = dao.getSession().createSQLQuery(hql);
    query.setInteger("operador", operadorDiscador);
    query.setInteger("codigo", cliente.getInformacaoCliente().getChave());
    query.executeUpdate();
    logger.debug("Liberando operadorCtt para {} com sucesso!", cliente);
  }

  @Override
  public void limpaReserva(Cliente cliente, int operadorDiscador, String nomeBaseDados) {
    dao.limpaReserva(cliente, operadorDiscador, nomeBaseDados);
    liberaNaBaseDoCliente(cliente, nomeBaseDados, operadorDiscador);
  }

  @Override
  public void limpaReservas(Campanha campanha, String nomeBaseDados, String nomeBase,
      int operadorDiscador) {
    dao.limpaReservas(campanha, nomeBaseDados, nomeBase, operadorDiscador);
    String hql =
        String
            .format(
                "update %s.DetCampanha "
                    + "  inner join InformacaoCliente on %s.DetCampanha.Codigo = InformacaoCliente.chave and InformacaoCliente.nomeBase = :nomeBase"
                    + "  inner join Cliente on InformacaoCliente.idCliente = Cliente.idCliente "
                    + "  inner join Mailing on Cliente.idMailing = Mailing.idMailing "
                    + "set %s.DetCampanha.operadorCtt = 0 " + "where "
                    + "  Mailing.idCampanha = :idCampanha " + "  and Mailing.ativo = 1 "
                    + "  and %s.DetCampanha.operadorCtt = :operador", nomeBaseDados, nomeBaseDados,
                nomeBaseDados, nomeBaseDados);
    Query query = dao.getSession().createSQLQuery(hql);
    query.setString("nomeBase", nomeBase);
    query.setLong("idCampanha", campanha.getId());
    query.setInteger("operador", operadorDiscador);
    query.executeUpdate();
  }

  @Override
  public int limpezaTemporaria(Campanha campanha, String nomeBaseDados, String nomeBase) {
    String sql =
        String
            .format(
                "update "
                    + "%s.DetCampanha "
                    + "  inner join InformacaoCliente on %s.DetCampanha.Codigo = InformacaoCliente.chave and InformacaoCliente.nomeBase = :nomeBase"
                    + "  inner join Cliente on Cliente.idCliente = InformacaoCliente.idCliente "
                    + "  inner join Mailing on Mailing.idMailing = Cliente.idMailing "
                    + "set Cliente.ordemDaFila = :data_set " + "where "
                    + "  Mailing.idCampanha = :idCampanha " + "  and Mailing.ativo = 1 "
                    + "  and %s.DetCampanha.situacao = 0 "
                    + "  and %s.DetCampanha.operadorCtt = 0 "
                    + "  and Cliente.ordemDaFila <> :data_where", nomeBaseDados, nomeBaseDados,
                nomeBaseDados, nomeBaseDados);
    Query query = dao.getSession().createSQLQuery(sql);
    DateTime data_set = new DateTime(2001, 01, 01, 00, 00);
    query.setString("nomeBase", nomeBase);
    query.setTimestamp("data_set", data_set.toDate());
    query.setLong("idCampanha", campanha.getId());
    query.setTimestamp("data_where", data_set.toDate());
    return query.executeUpdate();
  }

  @Override
  public boolean reservaNaBaseDoCliente(Cliente cliente, int operadorDiscador, String nomeBaseDados) {
    String hql =
        String.format("update %s.DetCampanha set " + "  operadorCtt = :operadorDiscador "
            + "where " + "  Codigo = :codigo and " + "  operadorCtt in (0, :operadorDiscador2)",
            nomeBaseDados);
    Query query = dao.getSession().createSQLQuery(hql);
    query.setInteger("operadorDiscador", operadorDiscador);
    query.setInteger("operadorDiscador2", operadorDiscador);
    query.setInteger("codigo", cliente.getInformacaoCliente().getChave());
    return query.executeUpdate() > 0;
  }

  void updateDetCampanha(ResultadoLigacao resultadoLigacao, Cliente cliente, Situacao situacao,
      int motivo, int motivoFinalizacao, int operador, int codigo, String nomeBaseDados) {

    int motivoFinal = motivo;
    if (situacao.equals(Situacao.FINALIZACAO)) {
      if (motivoFinalizacao != 0)
        motivoFinal = motivoFinalizacao;
    }

    String hql =
        "update %s.DetCampanha set " + "  situacao = :situacao, " + "  motivo = :motivo, "
            + "  dataHoraUC = Now(), " + "  operadorCtt = :operador " + "where "
            + "  codigo = :codigo";
    hql = String.format(hql, nomeBaseDados);

    logger.debug("Incremento de QtdReag: {}", resultadoLigacao.isIncrementaQtdReag());

    Query query = dao.getSession().createSQLQuery(hql);
    query.setInteger("situacao", situacao.getCodigo());
    query.setInteger("motivo", motivoFinal);
    query.setInteger("operador", operador);
    query.setInteger("codigo", codigo);
    logger.debug("Update DetCampanha para {}...", cliente);
    query.executeUpdate();
    logger.debug("Update DetCampanha para {} com sucesso!", cliente);
  }

  @Override
  public Collection<Cliente> obtemAGGs(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador) {
    List<Integer> idMailings = dao.obtemIdMailings(campanha);

    if (idMailings.isEmpty())
      return Collections.<Cliente>emptyList();

    String hql =
        "select Cliente.idCliente from Cliente "
            + "  inner join InformacaoCliente on Cliente.idCliente = InformacaoCliente.idCliente and InformacaoCliente.nomeBase = :nomeBase "
            + "  inner join %s.DetCampanha on InformacaoCliente.chave = %s.DetCampanha.Codigo "
            + "  inner join Agendamento on Cliente.idCliente = Agendamento.idCliente "
            + "%s " // inner join Filtro_Mailing on Cliente.idMailing =
            // Filtro_Mailing.idMailing
            + "Where "
            + "  (Cliente.disponivelAPartirDe is null or Cliente.disponivelAPartirDe <= Now()) "
            + "  And Cliente.idEstadoCliente = 1 " + "  And Agendamento.agendamento <= Now() "
            + "  And Agendamento.idAgente is null "
            + "  And %s.DetCampanha.OperadorCtt in (0, :operador) "
            + "  And %s.DetCampanha.Situacao in (0, 1, 8) " + "%s " // And Cliente.idMailing in
                                                                    // (:idMailings) : And
            // Filtro_Mailing.idMailing in (:idMailings)
            + "%s "// : And Cliente.filtro in (:filtros)
            + "order by Cliente.ordemDaFila asc , Cliente.ordem asc " + "limit :limit";

    hql =
        String
            .format(
                hql,
                nomeBaseDados,
                nomeBaseDados,
                dao.isFiltroExclusivo(campanha) ? "inner join Filtro_Mailing on Cliente.idMailing = Filtro_Mailing.idMailing"
                    : "", nomeBaseDados, nomeBaseDados,
                dao.isFiltroExclusivo(campanha) ? "And Filtro_Mailing.idMailing in (:idMailings)"
                    : "And Cliente.idMailing in (:idMailings)",
                dao.possuiFiltro(campanha) ? "And Cliente.filtro in (:filtros)" : "");

    Query query =
        dao.getSession().createSQLQuery(hql).setString("nomeBase", nomeBase)
            .setParameterList("idMailings", idMailings).setInteger("operador", operadorDiscador)
            .setInteger("limit", quantidade);

    query =
        dao.possuiFiltro(campanha) ? query
            .setParameterList("filtros", dao.getFiltroAsInt(campanha)) : query;

    DateTime inicio = new DateTime();
    LinkedList<Cliente> result = new LinkedList<Cliente>();
    for (Object idCliente : query.list())
      result.add(procura(((Integer) idCliente).longValue()));
    logger.info("obtemAgendados demorou {}ms", new Duration(inicio, new DateTime()).getMillis());
    return result;
  }

  @Override
  public Collection<Cliente> obtemLivres(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador) {
    List<Integer> idMailings = dao.obtemIdMailings(campanha);

    if (idMailings.isEmpty())
      return Collections.<Cliente>emptyList();

    String hql =
        "select Cliente.idCliente from Cliente "
            + "inner join InformacaoCliente on Cliente.idCliente = InformacaoCliente.idCliente and InformacaoCliente.nomeBase = :nomeBase "
            + "left join Agendamento on Cliente.idCliente = Agendamento.idCliente "
            + "inner join %s.DetCampanha on InformacaoCliente.chave = %s.DetCampanha.Codigo "
            + "%s " // : inner join Filtro_Mailing on Cliente.idMailing =
            // Filtro_Mailing.idMailing
            + "Where " + "Agendamento.idAgendamento is null "
            + "And (Cliente.disponivelAPartirDe is null or Cliente.disponivelAPartirDe <= Now()) "
            + "And Cliente.idEstadoCliente = 1 "
            + "And %s.DetCampanha.OperadorCtt in (0, :operador) "
            + "And %s.DetCampanha.Situacao <= 1 " + "%s " // And Cliente.idMailing in (:idMailings)
                                                          // : And
            // Filtro_Mailing.idMailing in (:idMailings)
            + "%s " // : And Cliente.filtro in (:filtros)
            + "order by Cliente.ordemDaFila asc , Cliente.ordem asc " + "limit :limit";

    hql =
        String
            .format(
                hql,
                nomeBaseDados,
                nomeBaseDados,
                dao.isFiltroExclusivo(campanha) ? "inner join Filtro_Mailing on Cliente.idMailing = Filtro_Mailing.idMailing"
                    : "", nomeBaseDados, nomeBaseDados,
                dao.isFiltroExclusivo(campanha) ? "And Filtro_Mailing.idMailing in (:idMailings)"
                    : "And Cliente.idMailing in (:idMailings)",
                dao.possuiFiltro(campanha) ? "And Cliente.filtro in (:filtros)" : "");

    Query query =
        dao.getSession().createSQLQuery(hql).setString("nomeBase", nomeBase)
            .setParameterList("idMailings", idMailings).setInteger("operador", operadorDiscador)
            .setInteger("limit", quantidade);

    query =
        dao.possuiFiltro(campanha) ? query
            .setParameterList("filtros", dao.getFiltroAsInt(campanha)) : query;

    DateTime inicio = new DateTime();
    LinkedList<Cliente> result = new LinkedList<Cliente>();
    for (Object idCliente : query.list())
      result.add(procura(((Integer) idCliente).longValue()));
    logger.info("obtemLivres demorou {}ms", new Duration(inicio, new DateTime()).getMillis());
    return result;
  }

  @Override
  public Collection<Cliente> listaTudo(Campanha campanha, int maxResults) {
    return dao.listaTudo(campanha, maxResults);
  }

  @Override
  public Cliente procura(Mailing mailing, String chave) {
    return dao.procura(mailing, chave);
  }

  @Override
  public void retornaReservadosOperador(Campanha campanha) {
    dao.retornaReservadosOperador(campanha);
  }

  @Override
  public void adiciona(Cliente t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(Cliente t) {
    dao.atualiza(t);
  }

  @Override
  public List<Cliente> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public Cliente procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(Cliente u) {
    dao.remove(u);
  }

  @Override
  public Cliente procura(String s) {
    return dao.procura(s);
  }

}
