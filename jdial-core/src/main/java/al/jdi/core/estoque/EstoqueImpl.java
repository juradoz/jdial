package al.jdi.core.estoque;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jdial.common.Engine;
import org.jdial.common.Service;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.devolveregistro.DevolveRegistro;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.modelo.Providencia;
import al.jdi.core.modelo.Providencia.ClienteSemTelefoneException;
import al.jdi.core.modelo.Providencia.NaoPodeReiniciarRodadaTelefoneException;
import al.jdi.core.modelo.Providencia.SemProximoTelefoneException;
import al.jdi.core.modelo.Providencia.SomenteCelularException;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.EstadoCliente;
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.Telefone;

class EstoqueImpl implements Estoque, Runnable, Service {

  @SuppressWarnings("serial")
  public static class ClienteJaEmUsoException extends Exception {
  }

  @SuppressWarnings("serial")
  public static class DncException extends Exception {
  }

  private final Logger logger;
  private final Configuracoes configuracoes;
  private final Provider<DaoFactory> daoFactoryProvider;
  private final DevolveRegistro devolveRegistro;
  private final TratadorEspecificoCliente tratadorEspecificoCliente;
  private final Discavel.Factory discavelFactory;
  private final Collection<Registro> estoque;
  private final ExtraidorClientes extraidorClientes;
  private final Engine.Factory engineFactory;
  private final Period intervaloMonitoracao;
  private final Map<Providencia.Codigo, Providencia> providencias;
  private final TelefoneFilter telefoneFilter;

  private Engine engine;
  private DateTime ultimaLimpezaTemporaria = new DateTime();

  EstoqueImpl(Logger logger, Configuracoes configuracoes, Provider<DaoFactory> daoFactoryProvider,
      DevolveRegistro devolveRegistro, TratadorEspecificoCliente tratadorEspecificoCliente,
      Discavel.Factory discavelFactory, Engine.Factory engineFactory, Collection<Registro> estoque,
      ExtraidorClientes extraidorClientes, Period intervaloMonitoracao,
      Map<Providencia.Codigo, Providencia> providencias, TelefoneFilter telefoneFilter) {
    this.logger = logger;
    this.configuracoes = configuracoes;
    this.daoFactoryProvider = daoFactoryProvider;
    this.devolveRegistro = devolveRegistro;
    this.tratadorEspecificoCliente = tratadorEspecificoCliente;
    this.discavelFactory = discavelFactory;
    this.estoque = estoque;
    this.extraidorClientes = extraidorClientes;
    this.engineFactory = engineFactory;
    this.intervaloMonitoracao = intervaloMonitoracao;
    this.providencias = providencias;
    this.telefoneFilter = telefoneFilter;
    logger.debug("Iniciando {} para {}...", this, extraidorClientes);
  }

  @Override
  public boolean contemCliente(Cliente cliente) {
    synchronized (estoque) {
      return new HashSet<Cliente>(extract(estoque, on(Registro.class).getCliente()))
          .contains(cliente);
    }
  }

  private void devolveCliente(DaoFactory daoFactory, Cliente cliente, MotivoSistema motivoSistema) {
    devolveCliente(daoFactory.getDataBanco(), cliente, motivoSistema);
  }

  private void devolveCliente(DateTime instante, Cliente cliente, MotivoSistema motivoSistema) {
    Ligacao ligacao =
        new Ligacao.Builder(discavelFactory.create(cliente), instante).setInicio(instante)
            .setTermino(instante).setMotivoFinalizacao(motivoSistema.getCodigo()).build();
    devolveRegistro.devolveLigacao(ligacao);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EstoqueImpl other = (EstoqueImpl) obj;
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
    Campanha campanha = daoFactory.getCampanhaDao().procura(configuracoes.getNomeCampanha());
    logger.info("Limpeza temporaria para campanha {}", campanha);
    int registrosLimpos =
        tratadorEspecificoCliente.obtemClienteDao(daoFactory).limpezaTemporaria(campanha,
            configuracoes.getNomeBaseDados(), configuracoes.getNomeBase());
    logger.info("Foram limpos {} registros", registrosLimpos);
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

    Period timeout = Period.minutes(configuracoes.getTempoMaximoRegistroEmMemoria());
    DateTime instante = daoFactory.getDataBanco();
    synchronized (estoque) {
      for (Iterator<Registro> it = estoque.iterator(); it.hasNext();) {
        Registro registro = it.next();
        boolean registroExpirado = registro.getCriacao().plus(timeout).isBeforeNow();
        if (!registroExpirado)
          continue;

        logger.warn("Registro expirado: {} Na memoria ha mais de {}s", registro.getCliente(),
            new Duration(registro.getCriacao(), new DateTime()).getStandardSeconds());
        devolveCliente(instante, registro.getCliente(), MotivoSistema.NAO_UTILIZADO);
        it.remove();
      }
    }
  }

  private void reservaCliente(DaoFactory daoFactory, Cliente cliente) throws DncException,
      ClienteJaEmUsoException {
    logger.debug("Providencia para cliente {} = {}", cliente, cliente.getInformacaoCliente()
        .getProvidenciaTelefone());

    Providencia.Codigo codigo =
        Providencia.Codigo.fromValue(cliente.getInformacaoCliente().getProvidenciaTelefone());
    Providencia providencia = providencias.get(codigo);
    cliente.setTelefone(providencia.getTelefone(daoFactory, cliente));

    cliente.getInformacaoCliente().setProvidenciaTelefone(
        Providencia.Codigo.MANTEM_ATUAL.getCodigo());

    if (tratadorEspecificoCliente.isDnc(daoFactory, cliente, configuracoes.getNomeBaseDados()))
      throw new DncException();

    boolean isConurbada = daoFactory.getAreaAreaDao().isConurbada(cliente.getTelefone());

    cliente.getTelefone().setConurbada(isConurbada);

    String digitoSaida =
        configuracoes.isDigitoSaidaDoBanco() ? tratadorEspecificoCliente
            .obtemClienteDao(daoFactory).getDigitoSaida(cliente) : EMPTY;

    cliente.setDigitoSaida(digitoSaida);

    EstadoCliente estadoCliente =
        daoFactory.getEstadoClienteDao().procura("Reservado pelo Discador");
    cliente.setEstadoCliente(estadoCliente);
    tratadorEspecificoCliente.obtemClienteDao(daoFactory).atualiza(cliente);
    if (!tratadorEspecificoCliente.reservaNaBaseDoCliente(daoFactory, cliente))
      throw new ClienteJaEmUsoException();
    logger.debug("Cliente {} reservado!", cliente);
  }

  @Override
  public void run() {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      limpezaTemporaria(daoFactory);
      removeRegistrosVencidos(daoFactory);
      limpaMemoriaPorSolicitacao(daoFactory);
      filtraTelefonesInuteis(daoFactory);
      if (!configuracoes.getSistemaAtivo())
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
        List<Telefone> telefonesFiltrados = telefoneFilter.filter(asList(telefone));
        if (!telefonesFiltrados.isEmpty()) {
          logger.debug("Telefone ainda bom na memoria {} {}", telefone, cliente);
          continue;
        }
        logger.warn("Removendo da memoria cliente com telefone {} inutil {}", telefone, cliente);
        devolveCliente(instante, cliente, MotivoSistema.NAO_UTILIZADO);
        it.remove();
      }
    }
  }

  void limpaMemoriaPorSolicitacao(DaoFactory daoFactory) {
    Dao<Campanha> campanhaDao = daoFactory.getCampanhaDao();
    Campanha campanha = campanhaDao.procura(configuracoes.getNomeCampanha());
    if (!campanha.isLimpaMemoria()) {
      logger.debug("Limpeza de memoria nao solicitada.");
      return;
    }

    campanha.setLimpaMemoria(false);
    campanhaDao.atualiza(campanha);

    logger.warn("Limpeza de memoria solicitada!");
    DateTime instante = daoFactory.getDataBanco();
    synchronized (estoque) {
      for (Iterator<Registro> it = estoque.iterator(); it.hasNext();) {
        Registro registro = it.next();
        devolveCliente(instante, registro.getCliente(), MotivoSistema.NAO_UTILIZADO);
        it.remove();
      }
    }
    logger.warn("Limpeza de memoria realizada com sucesso!");
  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException();
    engine = engineFactory.create(this, intervaloMonitoracao, true, true);
    logger.info("Iniciado {} para {}...", this, extraidorClientes);
  }

  @Override
  public void stop() {
    logger.debug("Encerrando {} para {}...", this, extraidorClientes);
    if (engine == null)
      throw new IllegalStateException("Already stopped");
    engine.stop();
    engine = null;
    logger.info("Encerrado {} para {}", this, extraidorClientes);
  }

  @Override
  public String toString() {
    return extraidorClientes.toString();
  }

  void verificaEstoques(DaoFactory daoFactory) {
    int size;
    synchronized (estoque) {
      size = estoque.size();
    }

    logger.info("Em estoque para {}: {}", extraidorClientes, size);
    if (size >= configuracoes.getMinimoEstoque())
      return;
    int quantidade = configuracoes.getMaximoEstoque() - size;

    try {
      Collection<Cliente> clientesDoBanco = extraidorClientes.extrai(daoFactory, quantidade);
      for (Cliente cliente : clientesDoBanco) {
        try {
          daoFactory.beginTransaction();
          try {
            reservaCliente(daoFactory, cliente);
          } finally {
            daoFactory.commit();
          }

          logger.info("Armazenando em {}: {}", new Object[] {this, cliente.toStringFull()});
          synchronized (estoque) {
            estoque.add(new Registro(cliente));
          }
        } catch (ClienteSemTelefoneException e) {
          logger.warn("Erro na obtencao de telefone para o cliente {}", cliente);
          devolveCliente(daoFactory, cliente, MotivoSistema.SEM_TELEFONES);
        } catch (SomenteCelularException e) {
          logger.warn("Cliente somente com celulares {}", cliente);
          devolveCliente(daoFactory, cliente, MotivoSistema.SOMENTE_CELULARES);
        } catch (DncException e) {
          logger.warn("Cliente {} consta na lista DNC", cliente);
          devolveCliente(daoFactory, cliente, MotivoSistema.LEI_NAO_PERTURBE);
        } catch (ClienteJaEmUsoException e) {
          logger.error("Nao atualizou cliente {}! Ja deve ter algum operador com ele...", cliente);
        } catch (SemProximoTelefoneException e) {
          logger.warn("Cliente sem proximoTelefone para providencia {}", cliente);
          devolveCliente(daoFactory, cliente, MotivoSistema.SEM_PROXIMO_TELEFONE);
        } catch (NaoPodeReiniciarRodadaTelefoneException e) {
          logger.warn("Ainda nao posso ir para proximo telefone {}", cliente);
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
