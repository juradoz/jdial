package al.jdi.core.gerenciadorfatork;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.inject.Provider;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.common.Engine;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.beans.DaoFactory;

public class DefaultGerenciadorFatorKTest {

  private static final double FATORK_MAXIMO = 5;
  private static final double FATORK_MINIMO = 3;

  private DefaultGerenciadorFatorK gerenciadorFatorKImpl;

  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Provider<DaoFactory> daoFactoryProvider;
  @Mock
  private Engine.Factory engineFactory;
  @Mock
  private Engine engine;
  @Mock
  private DaoFactory daoFactory;

  @Before
  public void setUp() throws Exception {
    initMocks(this);

    when(daoFactoryProvider.get()).thenReturn(daoFactory);
    when(configuracoes.getFatorKMinimo()).thenReturn((int) FATORK_MINIMO);
    when(configuracoes.getFatorKMaximo()).thenReturn((int) FATORK_MAXIMO);
    when(
        engineFactory.create(Mockito.any(Runnable.class), Mockito.any(Period.class),
            Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(engine);

    gerenciadorFatorKImpl =
        new DefaultGerenciadorFatorK(configuracoes, daoFactoryProvider, engineFactory);
  }

  @Test
  public void deveriaRetornarFKMaxSeVazio() {
    assertThat(gerenciadorFatorKImpl.getFatorK(), is(equalTo(FATORK_MAXIMO)));
  }

  @Test
  public void deveriaRetornarFatorKMinSe2IniciadasE1Atendidas() {
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaAtendida();
    gerenciadorFatorKImpl.run();
    assertThat(gerenciadorFatorKImpl.getFatorK(), is(equalTo(FATORK_MINIMO)));
  }

  @Test
  public void deveriaRetornarFatorKMinSe4IniciadasE3Atendidas() {
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();

    gerenciadorFatorKImpl.chamadaAtendida();
    gerenciadorFatorKImpl.chamadaAtendida();

    gerenciadorFatorKImpl.run();
    assertThat(gerenciadorFatorKImpl.getFatorK(), is(equalTo(FATORK_MINIMO)));
  }

  @Test
  public void deveriaRetornar4Se4IniciadasE1Atendidas() {
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaAtendida();
    gerenciadorFatorKImpl.run();
    assertThat(gerenciadorFatorKImpl.getFatorK(), is(equalTo(4.0)));
  }

  @Test
  public void deveriaRetornar4Se8IniciadasE2Atendidas() {
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();

    gerenciadorFatorKImpl.chamadaAtendida();
    gerenciadorFatorKImpl.chamadaAtendida();
    gerenciadorFatorKImpl.run();
    assertThat(gerenciadorFatorKImpl.getFatorK(), is(equalTo(4.0)));
  }

  @Test
  public void deveriaRetornarFatorKMaxSe6IniciadasE1Atendidas() {
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaAtendida();
    gerenciadorFatorKImpl.run();
    assertThat(gerenciadorFatorKImpl.getFatorK(), is(equalTo(FATORK_MAXIMO)));
  }

  @Test
  public void deveriaRetornarFatorKMaxSe12IniciadasE2Atendidas() {
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();
    gerenciadorFatorKImpl.chamadaIniciada();

    gerenciadorFatorKImpl.chamadaAtendida();
    gerenciadorFatorKImpl.chamadaAtendida();
    gerenciadorFatorKImpl.run();
    assertThat(gerenciadorFatorKImpl.getFatorK(), is(equalTo(FATORK_MAXIMO)));
  }

  @Test
  public void runDeveriaManterApenasUltimas10Atendidas() {
    for (int i = 0; i < 20; i++) {
      gerenciadorFatorKImpl.atendidas.add(i);
    }
    gerenciadorFatorKImpl.run();
    assertThat(gerenciadorFatorKImpl.atendidas.size(), is(equalTo(10)));
  }

  @Test
  public void runDeveriaManterApenasUltimas10Iniciadas() {
    for (int i = 0; i < 20; i++) {
      gerenciadorFatorKImpl.iniciadas.add(i);
    }
    gerenciadorFatorKImpl.run();
    assertThat(gerenciadorFatorKImpl.iniciadas.size(), is(equalTo(10)));
  }

}
