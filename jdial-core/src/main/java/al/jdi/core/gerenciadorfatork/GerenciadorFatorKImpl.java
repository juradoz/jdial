package al.jdi.core.gerenciadorfatork;

import static ch.lambdaj.Lambda.sum;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdial.common.Engine;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.core.Service;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorKModule.GerenciadorFatorKService;
import al.jdi.dao.beans.DaoFactory;

@Default
@GerenciadorFatorKService
class GerenciadorFatorKImpl implements GerenciadorFatorK, Service, Runnable {

  private static final Logger logger = LoggerFactory.getLogger(GerenciadorFatorKImpl.class);
  private static final int MINUTOS_ACUMULADOS = 10;

  private final Configuracoes configuracoes;
  private final Provider<DaoFactory> daoFactoryProvider;
  private final Engine.Factory engineFactory;

  final List<Integer> iniciadas = Collections.synchronizedList(new LinkedList<Integer>());
  final List<Integer> atendidas = Collections.synchronizedList(new LinkedList<Integer>());

  private Engine engine;

  private int iniciadasDoMinuto;
  private int atendidasDoMinuto;

  @Inject
  GerenciadorFatorKImpl(Configuracoes configuracoes, Provider<DaoFactory> daoFactoryProvider,
      Engine.Factory engineFactory) {
    this.configuracoes = configuracoes;
    this.daoFactoryProvider = daoFactoryProvider;
    this.engineFactory = engineFactory;
    logger.debug("Iniciando {}...", this);
  }

  @Override
  public synchronized void chamadaIniciada() {
    iniciadasDoMinuto++;
    logger.debug("Iniciando {} chamada do minuto.", iniciadasDoMinuto);
  }

  @Override
  public synchronized void chamadaAtendida() {
    atendidasDoMinuto++;
    logger.debug("Atendendo {} chamada do minuto.", atendidasDoMinuto);
  }

  @Override
  public double getFatorK() {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      int fatorKMaximo = configuracoes.getFatorKMaximo();
      int sumAtendidas;
      synchronized (atendidas) {
        sumAtendidas = sum(atendidas).intValue();
      }
      if (sumAtendidas == 0) {
        logger.debug("Sem atendidas. Retornando fatorKMax: {}", fatorKMaximo);
        return fatorKMaximo;
      }

      double sumIniciadas;
      synchronized (iniciadas) {
        sumIniciadas = sum(iniciadas).intValue();
      }

      double calculo = sumIniciadas / sumAtendidas;
      int fatorKMinimo = configuracoes.getFatorKMinimo();

      double result;
      if (calculo < fatorKMinimo)
        result = fatorKMinimo;
      else if (calculo > fatorKMaximo)
        result = fatorKMaximo;
      else
        result = calculo;

      logger.debug("sumIniciadas: {}, sumAtendidas: {}, fatorKMin: {}, fatorKMax: {}, result: {}",
          new Object[] {sumIniciadas, sumAtendidas, fatorKMinimo, fatorKMaximo, result});

      return result;
    } finally {
      daoFactory.close();
    }

  }

  @Override
  public void run() {
    synchronized (this) {
      limpa(iniciadas, iniciadasDoMinuto);
      iniciadasDoMinuto = 0;

      limpa(atendidas, atendidasDoMinuto);
      synchronized (this) {
        atendidasDoMinuto = 0;
      }
    }
  }

  private void limpa(List<Integer> lista, int valor) {
    synchronized (lista) {
      while (lista.size() >= MINUTOS_ACUMULADOS)
        lista.remove(0);
      logger.debug("Computando {} chamadas", valor);
      lista.add(valor);
    }
  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException();
    engine = engineFactory.create(this, Period.minutes(1), true);
    logger.info("Iniciado {}", this);
  }

  @Override
  public void stop() {
    logger.debug("Encerrando {}...", this);
    if (engine == null)
      throw new IllegalStateException();
    engine.stop();
    engine = null;
    logger.info("Encerrado {}", this);

  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }
}
