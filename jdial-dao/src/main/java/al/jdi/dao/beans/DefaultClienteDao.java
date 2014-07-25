package al.jdi.dao.beans;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hibernate.criterion.Restrictions.eq;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Collections;
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
import al.jdi.dao.model.Mailing;

class DefaultClienteDao implements ClienteDao {

  private static final Logger logger = getLogger(DefaultClienteDao.class);

  private final DefaultDao<Cliente> dao;

  DefaultClienteDao(Session session) {
    this.dao = new DefaultDao<>(session, Cliente.class);
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
  @SuppressWarnings("unchecked")
  public Collection<Cliente> listaTudo(Campanha campanha, int maxResults) {
    return dao.getSession().createCriteria(Cliente.class).setMaxResults(maxResults)
        .createAlias("mailing", "m").add(eq("m.campanha", campanha)).list();
  }

  @Override
  public Collection<Cliente> obtemAGGs(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador) {
    List<Integer> idMailings = obtemIdMailings(campanha);

    if (idMailings.isEmpty() && !possuiFiltro(campanha))
      return Collections.<Cliente>emptyList();

    String hql =
        "select distinct Cliente.idCliente from Cliente "
            + "  inner join InformacaoCliente on Cliente.idCliente = InformacaoCliente.idCliente"
            + "  inner join Operador.DetCampanha on InformacaoCliente.chave = Operador.DetCampanha.CodDetCamp "
            + "  inner join Agendamento on Cliente.idCliente = Agendamento.idCliente "
            + "  left join Operador.FiltrosDet on Cliente.idCliente = Operador.FiltrosDet.idCliente "
            + "Where "
            + "  (Cliente.disponivelAPartirDe is null or Cliente.disponivelAPartirDe <= Now()) "
            + "  And Cliente.idEstadoCliente = 1 " + "  And InformacaoCliente.nomeBase = '' "
            + "  And Agendamento.agendamento <= Now() " + "  And Agendamento.idAgente is null "
            + "  And Operador.DetCampanha.OperadorCtt in (0, 3) "
            + "  And Operador.DetCampanha.Situacao in (0, 1, 8) " + "%s " // And Cliente.idMailing
                                                                          // in (:idMailings) -- Sem
                                                                          // filtro
                                                                          // And
                                                                          // Operador.FiltrosDet.Filtro
                                                                          // = :codigoFiltro --
                                                                          // Comfiltro
            + "order by Cliente.ordemDaFila asc , Cliente.ordem asc " + "limit :limit";

    hql =
        String.format(hql, !possuiFiltro(campanha) ? "And Cliente.idMailing in (:idMailings) "
            : "And Operador.FiltrosDet.Filtro = :codigoFiltro ");

    Query query = dao.getSession().createSQLQuery(hql).setInteger("limit", quantidade);

    if (!possuiFiltro(campanha))
      query = query.setParameterList("idMailings", idMailings);
    else
      query = query.setInteger("codigoFiltro", campanha.getCodigoFiltro());

    DateTime inicio = new DateTime();
    LinkedList<Cliente> result = new LinkedList<Cliente>();
    for (Object idCliente : query.list())
      result.add(procura(((Integer) idCliente).longValue()));
    logger.info("obtemAgendados demorou {}ms", new Duration(inicio, new DateTime()).getMillis());
    return result;
  }

  @SuppressWarnings("unchecked")
  protected List<Integer> obtemIdMailings(Campanha campanha) {
    String hql =
        "select Mailing.idMailing from Mailing "
            + "where idCampanha = :idCampanha and "
            + "Mailing.ativo = 1 and (Mailing.dataInicial is null or Mailing.dataInicial <= Now()) and "
            + "(Mailing.dataFinal is null or Mailing.dataFinal >= Now())";
    Query query = dao.getSession().createSQLQuery(hql);
    query.setLong("idCampanha", campanha.getId());
    List<Integer> idMailings = query.list();
    return idMailings;
  }

  @Override
  public Collection<Cliente> obtemLivres(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador) {
    List<Integer> idMailings = obtemIdMailings(campanha);

    if (idMailings.isEmpty() && !possuiFiltro(campanha))
      return Collections.<Cliente>emptyList();

    String hql =
        "select distinct Cliente.idCliente from Cliente "
            + "inner join InformacaoCliente on Cliente.idCliente = InformacaoCliente.idCliente "
            + "left join Agendamento on Cliente.idCliente = Agendamento.idCliente "
            + "inner join Operador.DetCampanha on InformacaoCliente.chave = Operador.DetCampanha.CodDetCamp "
            + "left join Operador.FiltrosDet on Cliente.idCliente = Operador.FiltrosDet.idCliente "
            + "Where " + "Agendamento.idAgendamento is null "
            + "And (Cliente.disponivelAPartirDe is null or Cliente.disponivelAPartirDe <= Now()) "
            + "And Cliente.idEstadoCliente = 1 "
            + "And Operador.DetCampanha.OperadorCtt in (0, 3) "
            + "And Operador.DetCampanha.Situacao <= 1 " + "%s " // And Cliente.idMailing in
                                                                // (:idMailings) -- Sem filtro
                                                                // And Operador.FiltrosDet.Filtro =
                                                                // :codigoFiltro -- Com
                                                                // filtro
            + "order by Cliente.ordemDaFila asc , Cliente.ordem asc " + "limit :limit";

    hql =
        String.format(hql, !possuiFiltro(campanha) ? "And Cliente.idMailing in (:idMailings) "
            : "And Operador.FiltrosDet.Filtro = :codigoFiltro ");

    Query query = dao.getSession().createSQLQuery(hql).setInteger("limit", quantidade);

    if (!possuiFiltro(campanha))
      query = query.setParameterList("idMailings", idMailings);
    else
      query = query.setInteger("codigoFiltro", campanha.getCodigoFiltro());

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
  public Cliente procura(Mailing mailing, String chave) {
    return (Cliente) dao.getSession().createCriteria(Cliente.class).add(eq("mailing", mailing))
        .createCriteria("informacaoCliente").add(eq("chave", chave)).uniqueResult();
  }

  @Override
  public void retornaReservadosOperador(Campanha campanha) {
    DefaultDao<EstadoCliente> estadoclienteDao =
        new DefaultDao<EstadoCliente>(dao.getSession(), EstadoCliente.class);
    EstadoCliente ativo = estadoclienteDao.procura("Ativo");
    if (ativo == null)
      return;
    EstadoCliente reservado = estadoclienteDao.procura("Reservado pelo Operador");
    if (reservado == null)
      return;
    DateTime limite = new DateTime().minusHours(2);
    dao.getSession()
        .createSQLQuery(
            "update Cliente c " + "inner join Telefone t on t.idCliente = c.idCliente "
                + "inner join Mailing m on c.idMailing = m.idMailing "
                + "set c.idEstadoCliente = :idEstadoClienteAtivo, "
                + "c.ultimaMudancaEstado = Now() " + "where " + "m.idCampanha = :idCampanha "
                + "and c.idEstadoCliente = :idEstadoClienteReservadoOperador "
                + "and c.ultimaMudancaEstado = :limiteReserva")
        .setLong("idEstadoClienteAtivo", ativo.getId()).setLong("idCampanha", campanha.getId())
        .setLong("idEstadoClienteReservadoOperador", reservado.getId())
        .setTimestamp("limiteReserva", limite.toDate()).executeUpdate();
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
