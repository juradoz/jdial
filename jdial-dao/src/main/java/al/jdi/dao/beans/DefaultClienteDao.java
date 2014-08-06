package al.jdi.dao.beans;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.EstadoCliente;

class DefaultClienteDao implements ClienteDao {

  public static final String LivresSemFiltro =
      normalizeSpace("select distinct Cliente.idCliente from Cliente "
          + "  inner join Mailing on Mailing.idMailing = Cliente.idMailing "
          + "  inner join Campanha on Campanha.idCampanha = Mailing.idCampanha "
          + "  inner join InformacaoCliente on InformacaoCliente.idCliente = Cliente.idCliente "
          + "  inner join Operador.DetCampanha on InformacaoCliente.chave = Operador.DetCampanha.CodDetCamp "
          + "  left join Agendamento on Cliente.idCliente = Agendamento.idCliente " + "where "
          + "  Campanha.idCampanha = :idCampanha And " + "  Agendamento.idAgendamento is null And "
          + "  Mailing.ativo = 1 And "
          + "  (Mailing.dataInicial is null or Mailing.dataInicial <= Now()) And "
          + "  (Mailing.dataFinal is null or Mailing.dataFinal >= Now()) And "
          + "  (Cliente.disponivelAPartirDe is null or Cliente.disponivelAPartirDe <= Now()) And "
          + "  Cliente.idEstadoCliente = 1 And "
          + "  Operador.DetCampanha.OperadorCtt in (0, 3) And "
          + "  Operador.DetCampanha.Situacao <= 1 "
          + "order by Cliente.ordemDaFila asc , Cliente.ordem asc " + "limit :limit");

  public static final String LivresComFiltro =
      normalizeSpace("select distinct Cliente.idCliente from Cliente "
          + "  inner join Mailing on Mailing.idMailing = Cliente.idMailing "
          + "  inner join Operador.FiltrosDet on Operador.FiltrosDet.idCliente = Cliente.idCliente "
          + "  inner join InformacaoCliente on InformacaoCliente.idCliente = Cliente.idCliente "
          + "  inner join Operador.DetCampanha on InformacaoCliente.chave = Operador.DetCampanha.CodDetCamp "
          + "  left join Agendamento on Cliente.idCliente = Agendamento.idCliente " + "where "
          + "  Operador.FiltrosDet.Filtro = :codigoFiltro And "
          + "  Agendamento.idAgendamento is null And " + "  Mailing.ativo = 1 And "
          + "  (Mailing.dataInicial is null or Mailing.dataInicial <= Now()) And "
          + "  (Mailing.dataFinal is null or Mailing.dataFinal >= Now()) And "
          + "  (Cliente.disponivelAPartirDe is null or Cliente.disponivelAPartirDe <= Now()) And "
          + "  Cliente.idEstadoCliente = 1 And "
          + "  Operador.DetCampanha.OperadorCtt in (0, 3) And "
          + "  Operador.DetCampanha.Situacao <= 1 "
          + "order by Cliente.ordemDaFila asc , FiltrosDet.ordem asc " + "limit :limit");

  public static final String AgendadosSemFiltro =
      normalizeSpace("select distinct Cliente.idCliente from Cliente "
          + "  inner join Mailing on Mailing.idMailing = Cliente.idMailing "
          + "  inner join Campanha on Campanha.idCampanha = Mailing.idCampanha "
          + "  inner join InformacaoCliente on InformacaoCliente.idCliente = Cliente.idCliente "
          + "  inner join Operador.DetCampanha on InformacaoCliente.chave = Operador.DetCampanha.CodDetCamp "
          + "  inner join Agendamento on Cliente.idCliente = Agendamento.idCliente " + "Where "
          + "  Campanha.idCampanha = :idCampanha And " + "  InformacaoCliente.nomeBase = '' And "
          + "  Agendamento.agendamento <= Now() And " + "  Mailing.ativo = 1 And "
          + "  (Mailing.dataInicial is null or Mailing.dataInicial <= Now()) And "
          + "  (Mailing.dataFinal is null or Mailing.dataFinal >= Now()) And "
          + "  (Cliente.disponivelAPartirDe is null or Cliente.disponivelAPartirDe <= Now()) And "
          + "  Cliente.idEstadoCliente = 1 And "
          + "  Operador.DetCampanha.OperadorCtt in (0, 3) And "
          + "  Operador.DetCampanha.Situacao in (0, 1, 8) "
          + "order by Cliente.ordemDaFila asc , Cliente.ordem asc " + "limit :limit");

  public static final String AgendadosComFiltro =
      normalizeSpace("select distinct Cliente.idCliente from Cliente "
          + "  inner join Mailing on Mailing.idMailing = Cliente.idMailing "
          + "  inner join Operador.FiltrosDet on Operador.FiltrosDet.idCliente = Cliente.idCliente "
          + "  inner join InformacaoCliente on InformacaoCliente.idCliente = Cliente.idCliente "
          + "  inner join Operador.DetCampanha on InformacaoCliente.chave = Operador.DetCampanha.CodDetCamp "
          + "  inner join Agendamento on Cliente.idCliente = Agendamento.idCliente " + "Where "
          + "  Operador.FiltrosDet.Filtro = :codigoFiltro And "
          + "  InformacaoCliente.nomeBase = '' And " + "  Agendamento.agendamento <= Now() And "
          + "  Mailing.ativo = 1 And "
          + "  (Mailing.dataInicial is null or Mailing.dataInicial <= Now()) And "
          + "  (Mailing.dataFinal is null or Mailing.dataFinal >= Now()) And "
          + "  (Cliente.disponivelAPartirDe is null or Cliente.disponivelAPartirDe <= Now()) And "
          + "  Cliente.idEstadoCliente = 1 And "
          + "  Operador.DetCampanha.OperadorCtt in (0, 3) And "
          + "  Operador.DetCampanha.Situacao in (0, 1, 8) "
          + "order by Cliente.ordemDaFila asc , FiltrosDet.ordem asc " + "limit :limit");

  private static final Logger logger = getLogger(DefaultClienteDao.class);

  private final DefaultDao<Cliente> dao;

  DefaultClienteDao(Session session) {
    this.dao = new DefaultDao<Cliente>(session, Cliente.class);
  }

  @Override
  public void limpaReserva(Cliente cliente, int operadorDiscador, String nomeBaseDados) {
    DefaultDao<EstadoCliente> estadoclienteDao =
        new DefaultDao<EstadoCliente>(dao.getSession(), EstadoCliente.class);
    EstadoCliente estadoClienteAtivo = estadoclienteDao.procura("Ativo");

    EstadoCliente estadoClienteReservado = estadoclienteDao.procura("Reservado pelo Discador");

    cliente = procura(cliente.getId());

    if (!cliente.getEstadoCliente().equals(estadoClienteReservado))
      return;

    cliente.setEstadoCliente(estadoClienteAtivo);
    atualiza(cliente);
  }

  @Override
  public void limpaReservas(Campanha campanha, String nomeBaseDados, String nomeBase, int operador) {
    DefaultDao<EstadoCliente> estadoclienteDao =
        new DefaultDao<EstadoCliente>(dao.getSession(), EstadoCliente.class);
    EstadoCliente ativo = estadoclienteDao.procura("Ativo");
    EstadoCliente reservado = estadoclienteDao.procura("Reservado pelo Discador");
    dao.getSession()
        .createSQLQuery(
            "update Cliente c " + "inner join Mailing m on c.idMailing = m.idMailing "
                + "set c.idEstadoCliente = :ativo, " + "c.ultimaMudancaEstado = Now() "
                + "where c.idEstadoCliente = :reservado " + "and m.idCampanha = :campanha")
        .setLong("ativo", ativo.getId()).setLong("reservado", reservado.getId())
        .setLong("campanha", campanha.getId()).executeUpdate();
  }

  @Override
  public int limpezaTemporaria(Campanha campanha, String nomeBaseDados, String nomeBase) {
    return 0;
  }

  @Override
  public Collection<Cliente> obtemAGGs(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador) {
    Query query;
    if (campanha.isFiltroAtivo()) {
      logger.info("Query {}: {}", "AgendadosComFiltro", AgendadosComFiltro);
      query =
          getSession().createSQLQuery(AgendadosComFiltro).setInteger("codigoFiltro",
              campanha.getCodigoFiltro());
    } else {
      logger.info("Query {}: {}", "AgendadosSemFiltro", AgendadosSemFiltro);
      query =
          getSession().createSQLQuery(AgendadosSemFiltro).setLong("idCampanha", campanha.getId());
    }

    query = query.setInteger("limit", quantidade);

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
    Query query;
    if (campanha.isFiltroAtivo()) {
      logger.info("Query {}: {}", "LivresComFiltro", LivresComFiltro);
      query =
          getSession().createSQLQuery(LivresComFiltro).setInteger("codigoFiltro",
              campanha.getCodigoFiltro());
    } else {
      logger.info("Query {}: {}", "LivresSemFiltro", LivresSemFiltro);
      query = getSession().createSQLQuery(LivresSemFiltro).setLong("idCampanha", campanha.getId());
    }

    query = query.setInteger("limit", quantidade);

    DateTime inicio = new DateTime();
    LinkedList<Cliente> result = new LinkedList<Cliente>();
    for (Object idCliente : query.list())
      result.add(procura(((Integer) idCliente).longValue()));
    logger.info("obtemLivres demorou {}ms", new Duration(inicio, new DateTime()).getMillis());
    return result;
  }

  protected boolean possuiFiltro(Campanha campanha) {
    return campanha.isFiltroAtivo();
  }

  @Override
  public String getDigitoSaida(Cliente cliente) {
    return EMPTY;
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
  public Cliente procura(String s) {
    return dao.procura(s);
  }

  @Override
  public void remove(Cliente u) {
    dao.remove(u);
  }

  protected Session getSession() {
    return dao.getSession();
  }

}
