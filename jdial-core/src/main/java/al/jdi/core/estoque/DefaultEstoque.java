package al.jdi.core.estoque;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.core.devolveregistro.DevolveRegistro;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.modelo.Providencia;
import al.jdi.core.modelo.Providencia.ClienteSemTelefoneException;
import al.jdi.core.modelo.Providencia.Codigo;
import al.jdi.core.modelo.Providencia.NaoPodeReiniciarRodadaTelefoneException;
import al.jdi.core.modelo.Providencia.SemProximoTelefoneException;
import al.jdi.core.modelo.Providencia.SomenteCelularException;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.EstadoCliente;
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.Telefone;

class DefaultEstoque implements Estoque, Runnable {

  static class EstoqueImplFactory implements Estoque.Factory {
    @Inject
    private Provider<DaoFactory> daoFactoryProvider;
    @Inject
    private DevolveRegistro devolveRegistro;
    @Inject
    private TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
    @Inject
    private Discavel.Factory discavelFactory;
    @Inject
    private Engine.Factory engineFactory;
    @Inject
    private Collection<Registro> estoque;
    @Inject
    private Map<Codigo, Providencia> providencias;
    @Inject
    private TelefoneFilter telefoneFilter;

    @Override
    public Estoque create(Tenant tenant, ExtraidorClientes extraidorClientes,
        Period intervaloMonitoracao) {
      return new DefaultEstoque(tenant, daoFactoryProvider, devolveRegistro,
          tratadorEspecificoClienteFactory, discavelFactory, engineFactory, estoque,
          extraidorClientes, intervaloMonitoracao, providencias, telefoneFilter);
    }
  }

  @SuppressWarnings("serial")
  public static class ClienteJaEmUsoException extends Exception {
  }

  @SuppressWarnings("serial")
  public static class DncException extends Exception {
  }

  private static final Logger logger = getLogger(DefaultEstoque.class);

  private final Provider<DaoFactory> daoFactoryProvider;
  private final DevolveRegistro devolveRegistro;
  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;
  private final Discavel.Factory discavelFactory;
  private final Collection<Registro> estoque;
  private final ExtraidorClientes extraidorClientes;
  private final Engine.Factory engineFactory;
  private final Period intervaloMonitoracao;
  private final Map<Providencia.Codigo, Providencia> providencias;
  private final TelefoneFilter telefoneFilter;
  private final Tenant tenant;

  private Engine engine;
  private DateTime ultimaLimpezaTemporaria = new DateTime();

  DefaultEstoque(Tenant tenant, Provider<DaoFactory> daoFactoryProvider,
      DevolveRegistro devolveRegistro,
      TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory,
      Discavel.Factory discavelFactory, Engine.Factory engineFactory, Collection<Registro> estoque,
      ExtraidorClientes extraidorClientes, Period intervaloMonitoracao,
      Map<Providencia.Codigo, Providencia> providencias, TelefoneFilter telefoneFilter) {
    this.tenant = tenant;
    this.daoFactoryProvider = daoFactoryProvider;
    this.devolveRegistro = devolveRegistro;
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
    this.discavelFactory = discavelFactory;
    this.estoque = estoque;
    this.extraidorClientes = extraidorClientes;
    this.engineFactory = engineFactory;
    this.intervaloMonitoracao = intervaloMonitoracao;
    this.providencias = providencias;
    this.telefoneFilter = telefoneFilter;
    logger.debug("Iniciando {}...", this);
  }

  private void devolveCliente(DaoFactory daoFactory, Cliente cliente, MotivoSistema motivoSistema) {
    devolveCliente(daoFactory.getDataBanco(), cliente, motivoSistema);
  }

  private void devolveCliente(DateTime instante, Cliente cliente, MotivoSistema motivoSistema) {
    Ligacao ligacao =
        new Ligacao.Builder(discavelFactory.create(tenant, cliente), instante).setInicio(instante)
            .setTermino(instante).setMotivoFinalizacao(motivoSistema.getCodigo()).build();
    devolveRegistro.devolveLigacao(tenant, ligacao);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DefaultEstoque other = (DefaultEstoque) obj;
    return new EqualsBuilder().append(extraidorClientes, other.extraidorClientes).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(extraidorClientes).toHashCode();
  }

  void limpezaTemporaria(DaoFactory daoFactory) {
    if (!ultimaLimpezaTemporaria.plus(Period.minutes(30)).isBeforeNow())
      return;

    ultimaLimpezaTemporaria = new DateTime();
    Campanha campanha =
        daoFactory.getCampanhaDao().procura(tenant.getConfiguracoes().getNomeCampanha());
    logger.info("Limpeza temporaria {}", campanha);
    int registrosLimpos =
        tratadorEspecificoClienteFactory
            .create(tenant, daoFactory)
            .obtemClienteDao()
            .limpezaTemporaria(campanha, tenant.getConfiguracoes().getNomeBaseDados(),
                tenant.getConfiguracoes().getNomeBase());
    logger.info("Foram limpos {} registros {}", registrosLimpos, campanha);
  }

  @Override
  public Collection<Cliente> obtemRegistros(int quantidade) {
    Collection<Cliente> result = new LinkedList<Cliente>();
    synchronized (estoque) {
      int i = 0;
      for (Iterator<Registro> it = estoque.iterator(); it.hasNext() && i < quantidade; i++) {
        Cliente c = it.next().getCliente();
        result.add(c);
        it.remove();
      }
    }
    return result;
  }

  void removeRegistrosVencidos(DaoFactory daoFactory) {

    Period timeout = Period.minutes(tenant.getConfiguracoes().getTempoMaximoRegistroEmMemoria());
    DateTime instante = daoFactory.getDataBanco();
    synchronized (estoque) {
      for (Iterator<Registro> it = estoque.iterator(); it.hasNext();) {
        Registro registro = it.next();
        boolean registroExpirado = registro.getCriacao().plus(timeout).isBeforeNow();
        if (!registroExpirado)
          continue;

        logger.warn("Registro expirado: {} Na memoria ha mais de {}s {}", registro.getCliente(),
            new Duration(registro.getCriacao(), new DateTime()).getStandardSeconds(),
            tenant.getCampanha());
        devolveCliente(instante, registro.getCliente(), MotivoSistema.NAO_UTILIZADO);
        it.remove();
      }
    }
  }

  private void reservaCliente(DaoFactory daoFactory, Cliente cliente) throws DncException,
      ClienteJaEmUsoException {
    logger.debug("Providencia para cliente {} = {} {}", cliente, cliente.getInformacaoCliente()
        .getProvidenciaTelefone(), tenant.getCampanha());

    Providencia.Codigo codigo =
        Providencia.Codigo.fromValue(cliente.getInformacaoCliente().getProvidenciaTelefone());
    Providencia providencia = providencias.get(codigo);
    cliente.setTelefone(providencia.getTelefone(tenant, daoFactory, cliente));

    cliente.getInformacaoCliente().setProvidenciaTelefone(
        Providencia.Codigo.MANTEM_ATUAL.getCodigo());

    if (tratadorEspecificoClienteFactory.create(tenant, daoFactory).isDnc(cliente))
      throw new DncException();

    boolean isConurbada = daoFactory.getAreaAreaDao().isConurbada(cliente.getTelefone());

    cliente.getTelefone().setConurbada(isConurbada);

    String digitoSaida =
        tenant.getConfiguracoes().isDigitoSaidaDoBanco() ? tratadorEspecificoClienteFactory
            .create(tenant, daoFactory).obtemClienteDao().getDigitoSaida(cliente) : EMPTY;

    cliente.setDigitoSaida(digitoSaida);

    EstadoCliente estadoCliente =
        daoFactory.getEstadoClienteDao().procura("Reservado pelo Discador");
    cliente.setEstadoCliente(estadoCliente);
    tratadorEspecificoClienteFactory.create(tenant, daoFactory).obtemClienteDao().atualiza(cliente);
    if (!tratadorEspecificoClienteFactory.create(tenant, daoFactory)
        .reservaNaBaseDoCliente(cliente))
      throw new ClienteJaEmUsoException();
    logger.debug("Cliente {} reservado! {}", cliente, tenant.getCampanha());
  }

  @Override
  public void run() {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      limpezaTemporaria(daoFactory);
      removeRegistrosVencidos(daoFactory);
      limpaMemoriaPorSolicitacao(daoFactory);
      filtraTelefonesInuteis(daoFactory);
      if (!tenant.getConfiguracoes().getSistemaAtivo())
        return;
      verificaEstoques(daoFactory);
    } finally {
      daoFactory.close();
    }
  }

  private void filtraTelefonesInuteis(DaoFactory daoFactory) {
    DateTime instante = daoFactory.getDataBanco();
    synchronized (estoque) {
      for (Iterator<Registro> it = estoque.iterator(); it.hasNext();) {
        Cliente cliente = it.next().getCliente();
        Telefone telefone = cliente.getTelefone();
        List<Telefone> telefonesFiltrados = telefoneFilter.filter(tenant, asList(telefone));
        if (!telefonesFiltrados.isEmpty()) {
          logger.debug("Telefone ainda bom na memoria {} {} {}", telefone, cliente,
              tenant.getCampanha());
          continue;
        }
        logger.warn("Removendo da memoria cliente com telefone {} inutil {} {}", telefone, cliente,
            tenant.getCampanha());
        devolveCliente(instante, cliente, MotivoSistema.NAO_UTILIZADO);
        it.remove();
      }
    }
  }

  void limpaMemoriaPorSolicitacao(DaoFactory daoFactory) {
    Dao<Campanha> campanhaDao = daoFactory.getCampanhaDao();
    Campanha campanha = campanhaDao.procura(tenant.getConfiguracoes().getNomeCampanha());
    if (!campanha.isLimpaMemoria()) {
      logger.debug("Limpeza de memoria nao solicitada. {}", tenant.getCampanha());
      return;
    }

    daoFactory.beginTransaction();
    campanha.setLimpaMemoria(false);
    campanhaDao.atualiza(campanha);
    daoFactory.commit();

    logger.warn("Limpeza de memoria solicitada! {}", tenant.getCampanha());
    DateTime instante = daoFactory.getDataBanco();
    synchronized (estoque) {
      for (Iterator<Registro> it = estoque.iterator(); it.hasNext();) {
        Registro registro = it.next();
        devolveCliente(instante, registro.getCliente(), MotivoSistema.NAO_UTILIZADO);
        it.remove();
      }
    }
    logger.warn("Limpeza de memoria realizada com sucesso! {}", tenant.getCampanha());
  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException();
    engine = engineFactory.create(this, intervaloMonitoracao, true, true);
  }

  @Override
  public void stop() {
    logger.debug("Encerrando {}...", this);
    if (engine == null)
      throw new IllegalStateException("Already stopped");
    engine.stop();
    engine = null;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(extraidorClientes).append(tenant.getCampanha())
        .toString();
  }

  void verificaEstoques(DaoFactory daoFactory) {
    int size;
    synchronized (estoque) {
      size = estoque.size();
    }

    logger.info("Em estoque para {}: {}", this, size);
    if (size >= tenant.getConfiguracoes().getMinimoEstoque())
      return;
    int quantidade = tenant.getConfiguracoes().getMaximoEstoque() - size;

    try {
      Collection<Cliente> clientesDoBanco =
          extraidorClientes.extrai(tenant, daoFactory, quantidade);
      for (Cliente cliente : clientesDoBanco) {
        try {
          daoFactory.beginTransaction();
          try {
            reservaCliente(daoFactory, cliente);
          } finally {
            daoFactory.commit();
          }

          logger.info("Armazenando em {}: {}", this, cliente.toStringFull());
          synchronized (estoque) {
            estoque.add(new Registro(cliente));
          }
        } catch (ClienteSemTelefoneException e) {
          logger.warn("Sem telefones para o cliente {} {}", cliente, tenant.getCampanha());
          devolveCliente(daoFactory, cliente, MotivoSistema.SEM_TELEFONES);
        } catch (SomenteCelularException e) {
          logger.warn("Cliente somente com celulares {} {}", cliente, tenant.getCampanha());
          devolveCliente(daoFactory, cliente, MotivoSistema.SOMENTE_CELULARES);
        } catch (DncException e) {
          logger.warn("Cliente {} consta na lista DNC {}", cliente, tenant.getCampanha());
          devolveCliente(daoFactory, cliente, MotivoSistema.LEI_NAO_PERTURBE);
        } catch (ClienteJaEmUsoException e) {
          logger.error("Nao atualizou cliente {}! Ja deve ter algum operador com ele {}", cliente,
              tenant.getCampanha());
        } catch (SemProximoTelefoneException e) {
          logger.warn("Cliente sem proximoTelefone para providencia {} {}", cliente,
              tenant.getCampanha());
          devolveCliente(daoFactory, cliente, MotivoSistema.SEM_PROXIMO_TELEFONE);
        } catch (NaoPodeReiniciarRodadaTelefoneException e) {
          logger.warn("Ainda nao posso ir para proximo telefone {} {}", cliente,
              tenant.getCampanha());
          devolveCliente(daoFactory, cliente, MotivoSistema.NAO_PODE_IR_PROXIMO_TELEFONE);
        } catch (RuntimeException e) {
          logger.error(e.getMessage(), e);
        }
      }
    } finally {
      if (daoFactory.hasTransaction())
        daoFactory.rollback();
    }

  }
}
