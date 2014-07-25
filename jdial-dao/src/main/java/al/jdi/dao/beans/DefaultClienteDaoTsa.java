package al.jdi.dao.beans;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Situacao;
import al.jdi.dao.model.Telefone;

class DefaultClienteDaoTsa implements ClienteDaoTsa {

  private static final Logger logger = getLogger(DefaultClienteDaoTsa.class);

  private final DefaultClienteDao dao;

  DefaultClienteDaoTsa(Session session) {
    this.dao = new DefaultClienteDao(session);
  }

  @Override
  public String getDigitoSaida(Cliente cliente) {
    try {
      logger.debug("Procurando digito portado para cliente {} ddd {} telefone {}", new Object[] {
          cliente, cliente.getTelefone().getDdd(), cliente.getTelefone().getTelefone()});
      String rota =
          (String) dao
              .getSession()
              .createSQLQuery(
                  "SELECT  identificar.rota From "
                      + "Portabilidade.portados "
                      + "inner join Portabilidade.identificar on portados.telreceptora=identificar.rn_operadora "
                      + "where teldoadora=:tel limit 1")
              .setString("tel",
                  cliente.getTelefone().getDdd() + cliente.getTelefone().getTelefone())
              .uniqueResult();

      if (!isBlank(rota)) {
        logger.debug("Encontrei digito portado para cliente {} ddd {} telefone {} digito {}",
            new Object[] {cliente, cliente.getTelefone().getDdd(),
                cliente.getTelefone().getTelefone(), rota});
        return rota;
      }

      String prefixo = cliente.getTelefone().getTelefone().substring(0, 4);
      String sufixo = cliente.getTelefone().getTelefone().substring(4);

      logger
          .debug(
              "Procurando digito portabilidade para cliente {} ddd {} telefone {} prefixo {} sufixo {}",
              new Object[] {cliente, cliente.getTelefone().getDdd(),
                  cliente.getTelefone().getTelefone(), prefixo, sufixo});

      rota =
          (String) dao
              .getSession()
              .createSQLQuery(
                  "SELECT identificar.rota "
                      + "From Portabilidade.prefgeral "
                      + " Inner Join Portabilidade.identificar on prefgeral.rn = identificar.rn_operadora "
                      + " where CN=:ddd " + " and Prefixo=:prefixo "
                      + " and :sufixo between MCDU_Inicial and MCDU_Final limit 1")
              .setString("ddd", cliente.getTelefone().getDdd()).setString("prefixo", prefixo)
              .setString("sufixo", sufixo).uniqueResult();

      if (!isBlank(rota)) {
        logger
            .debug(
                "Encontrado digito portabilidade para cliente {} ddd {} telefone {} prefixo {} sufixo {} digito {}",
                new Object[] {cliente, cliente.getTelefone().getDdd(),
                    cliente.getTelefone().getTelefone(), prefixo, sufixo, rota});
        return rota;
      }

      logger
          .debug(
              "Nao encontrado digito portabilidade para cliente {} ddd {} telefone {} prefixo {} sufixo {}",
              new Object[] {cliente, cliente.getTelefone().getDdd(),
                  cliente.getTelefone().getTelefone(), prefixo, sufixo});
      return "";
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return "";
    }
  }

  @Override
  public void insereResultadoTsa(Cliente cliente, ResultadoLigacao resultadoLigacao,
      Telefone telefone, DateTime inicioDiscagem, Situacao situacao, int motivo,
      int motivoFinalizacao, String nomeBaseDados, int operadorDiscador, int motivoCampanha) {
    int codDetCamp = cliente.getInformacaoCliente().getChave();
    int codCampanha = 0;
    int codCliente = 0;
    String[] info = cliente.getInformacaoCliente().getInformacoesAdicionais().split("#");
    for (int i = 0; i < info.length; i++)
      switch (i) {
        case 0:
          try {
            codCliente = Integer.parseInt(info[i]);
          } catch (Exception e) {
            logger.error(e.getMessage(), e);
          }
          break;
        case 1:
          try {
            codCampanha = Integer.parseInt(info[i]);
          } catch (Exception e) {
            logger.error(e.getMessage(), e);
          }
          break;
      }

    if (codDetCamp == 0 || codCliente == 0)
      return;

    updateDetCampanha(resultadoLigacao, cliente, situacao, motivo, motivoFinalizacao,
        operadorDiscador, codDetCamp);

    Integer origem =
        (Integer) dao.getSession()
            .createSQLQuery("select Origem from Operador.Clientes where codigo = :codCliente")
            .setInteger("codCliente", codCliente).uniqueResult();

    logger.debug("Insert DetCliente para {} ...", cliente);
    dao.getSession()
        .createSQLQuery(
            "insert into Operador.DetCliente (codigo, cliente, situacao, motivo, dataHoraUC, operador, celular, Supervisor, origem, Filtro, TelefoneAtendeu) values "
                + "(:codCampanha, :codCliente, :situacao, :motivo, Now(), :operador, :celular, 0, :origem, :filtro, 0)")
        .setInteger("codCampanha", codCampanha)
        .setInteger("codCliente", codCliente)
        .setInteger("situacao", situacao.getCodigo())
        .setInteger("motivo", motivo)
        .setInteger("operador", operadorDiscador)
        .setBoolean("celular", telefone.isCelular())
        .setInteger("origem", origem)
        .setInteger("filtro",
            cliente.getMailing().getCampanha().isFiltroAtivo() ? cliente.getFiltro() : 0)
        .executeUpdate();
    logger.debug("Insert DetCliente para {} com sucesso!", cliente);

    String hql = "SELECT LAST_INSERT_ID() as ID";
    Query query = dao.getSession().createSQLQuery(hql);
    BigInteger id = (BigInteger) query.uniqueResult();

    hql =
        "update Operador.Telefones set dataHora = Now(), finaliza = 20 where codigo = :codTelefone";
    query = dao.getSession().createSQLQuery(hql);
    query.setLong("codTelefone", telefone.getChaveTelefone());
    logger.debug("Update Telefones para {}...", cliente);
    query.executeUpdate();
    logger.debug("Update Telefones para {} com sucesso!", cliente);

    hql =
        "insert into Operador.Chamadas (finaliza, telefone, operador, cliente, dataHora, detCliente, campanha, inicioDiscagem, Power) values (20, :codTelefone, :operador, :codCliente, Now(), :id, :codCampanha, :inicioDiscagem, 0)";
    query = dao.getSession().createSQLQuery(hql);
    query.setLong("codTelefone", telefone.getChaveTelefone());
    query.setInteger("operador", operadorDiscador);
    query.setInteger("codCliente", codCliente);
    query.setInteger("id", id.intValue());
    query.setInteger("codCampanha", codCampanha);
    query.setTimestamp("inicioDiscagem", inicioDiscagem.toDate());
    logger.debug("Insert Chamadas para {} ...", cliente);
    query.executeUpdate();
    logger.debug("Insert Chamadas para {} com sucesso!", cliente);
  }

  @Override
  public boolean isDnc(Cliente cliente, String nomeBaseDados) {
    String hql =
        "select count(Codigo) from Operador.DNC where ddd = :ddd and fone = :fone and Desbloqueado is null";
    BigInteger qtdDnc =
        (BigInteger) dao.getSession().createSQLQuery(hql)
            .setString("ddd", cliente.getTelefone().getDdd())
            .setString("fone", cliente.getTelefone().getTelefone()).uniqueResult();
    logger.debug("qtdDnc = {} para cliente {} ddd {} telefone {}", new Object[] {qtdDnc, cliente,
        cliente.getTelefone().getDdd(), cliente.getTelefone()});

    if (qtdDnc.intValue() != 0)
      return true;

    hql = "select count(codigo) from Operador.BlackList where DDD = :ddd and Fone = :fone";
    BigInteger qtdBlacklist =
        (BigInteger) dao.getSession().createSQLQuery(hql)
            .setString("ddd", cliente.getTelefone().getDdd())
            .setString("fone", cliente.getTelefone().getTelefone()).uniqueResult();
    logger.debug("qtdBlacklist = {} para cliente {} ddd {} telefone {}", new Object[] {
        qtdBlacklist, cliente, cliente.getTelefone().getDdd(), cliente.getTelefone()});
    return qtdBlacklist.intValue() != 0;
  }

  @Override
  public void liberaNaBaseDoCliente(Cliente cliente, String nomeBaseDados, int operadorDiscador) {
    String hql =
        "update Operador.DetCampanha set operadorCtt = 0 where CodDetCamp = :codDetCamp and operadorCtt = 3";
    Query query = dao.getSession().createSQLQuery(hql);
    query.setInteger("codDetCamp", cliente.getInformacaoCliente().getChave());
    query.executeUpdate();
    logger.debug("Liberando operadorCtt para {} com sucesso!", cliente);
  }

  @Override
  public void limpaReserva(Cliente cliente, int operadorDiscador, String nomeBaseDados) {
    dao.limpaReserva(cliente, operadorDiscador, nomeBaseDados);
    liberaNaBaseDoCliente(cliente, EMPTY, operadorDiscador);
  }

  @Override
  public void limpaReservas(Campanha campanha, String nomeBaseDados, String nomeBase, int operador) {
    dao.limpaReservas(campanha, nomeBaseDados, nomeBase, operador);
    String hql =
        "update Operador.DetCampanha "
            + "inner join InformacaoCliente on Operador.DetCampanha.CodDetCamp = InformacaoCliente.chave "
            + "inner join Cliente on InformacaoCliente.idCliente = Cliente.idCliente "
            + "inner join Mailing on Cliente.idMailing = Mailing.idMailing "
            + "set Operador.DetCampanha.operadorCtt = 0 "
            + "where Mailing.idCampanha = :idCampanha " + "and Mailing.ativo = 1 "
            + "and Operador.DetCampanha.operadorCtt = :operadorDiscador";
    Query query = dao.getSession().createSQLQuery(hql);
    query.setLong("idCampanha", campanha.getId());
    query.setInteger("operadorDiscador", operador);
    query.executeUpdate();
  }

  @Override
  public int limpezaTemporaria(Campanha campanha, String nomeBaseDados, String nomeBase) {
    String sql =
        "update "
            + "Operador.DetCampanha "
            + "  inner join InformacaoCliente on Operador.DetCampanha.CodDetCamp = InformacaoCliente.chave "
            + "  inner join Cliente on Cliente.idCliente = InformacaoCliente.idCliente "
            + "  inner join Mailing on Mailing.idMailing = Cliente.idMailing "
            + "set Cliente.ordemDaFila = :data_set " + "where "
            + "  Mailing.idCampanha = :idCampanha " + "  and Mailing.ativo = 1 "
            + "  and Operador.DetCampanha.situacao = 0 "
            + "  and Operador.DetCampanha.operadorCtt = 0 "
            + "  and Cliente.ordemDaFila <> :data_where";
    Query query = dao.getSession().createSQLQuery(sql);
    DateTime data_set = new DateTime(2001, 01, 01, 00, 00);
    query.setTimestamp("data_set", data_set.toDate());
    query.setLong("idCampanha", campanha.getId());
    query.setTimestamp("data_where", data_set.toDate());
    return query.executeUpdate();
  }

  @Override
  public boolean reservaNaBaseDoCliente(Cliente cliente, int operadorDiscador, String nomeBaseDados) {
    String hql =
        "update Operador.DetCampanha set operadorCtt = :operadorDiscador where CodDetCamp = :codDetCamp and operadorCtt in (0, :operadorDiscador2)";
    Query query = dao.getSession().createSQLQuery(hql);
    query.setInteger("operadorDiscador", operadorDiscador);
    query.setInteger("operadorDiscador2", operadorDiscador);
    query.setInteger("codDetCamp", cliente.getInformacaoCliente().getChave());
    return query.executeUpdate() > 0;
  }

  void updateDetCampanha(ResultadoLigacao resultadoLigacao, Cliente cliente, Situacao situacao,
      int motivo, int motivoFinalizacao, int operador, int codDetCamp) {

    int motivoFinal = motivo;
    if (situacao.equals(Situacao.FINALIZACAO)) {
      if (motivoFinalizacao != 0)
        motivoFinal = motivoFinalizacao;
    }

    String hql =
        "update Operador.DetCampanha set situacao = :situacao, motivo = :motivo, dataHoraUC = Now(), operador = :operador %s where codDetCamp = :codDetCamp";
    hql =
        String.format(hql, resultadoLigacao.isIncrementaQtdReag() ? ", QtdReag = QtdReag + 1 "
            : EMPTY);

    logger.debug("Incremento de QtdReag: {}", resultadoLigacao.isIncrementaQtdReag());

    Query query = dao.getSession().createSQLQuery(hql);
    query.setInteger("situacao", situacao.getCodigo());
    query.setInteger("motivo", motivoFinal);
    query.setInteger("operador", operador);
    query.setInteger("codDetCamp", codDetCamp);
    logger.debug("Update DetCampanha para {}...", cliente);
    query.executeUpdate();
    logger.debug("Update DetCampanha para {} com sucesso!", cliente);
  }

  @Override
  public Collection<Cliente> obtemAGGs(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador) {
    return dao.obtemAGGs(quantidade, campanha, nomeBaseDados, nomeBase, operadorDiscador);
  }

  @Override
  public Collection<Cliente> obtemLivres(int quantidade, Campanha campanha, String nomeBaseDados,
      String nomeBase, int operadorDiscador) {
    return dao.obtemLivres(quantidade, campanha, nomeBaseDados, nomeBase, operadorDiscador);
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
