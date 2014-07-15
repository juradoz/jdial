package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.ResultadoLigacaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ModificadorResultadoInexistenteFakeTest {

  private ModificadorResultadoInexistenteFake modificadorResultadoInexistenteFake;

  @Mock
  private DaoFactory daoFactory;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private Campanha campanha;
  @Mock
  private ResultadoLigacaoDao resultadoLigacaoDao;
  @Mock
  private ResultadoLigacao resultadoLigacaoAtendida;
  @Mock
  private ResultadoLigacao resultadoLigacaoInexistente;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Logger logger;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getResultadoLigacaoDao()).thenReturn(resultadoLigacaoDao);
    when(resultadoLigacaoDao.procura(-1, campanha)).thenReturn(resultadoLigacaoAtendida);
    when(resultadoLigacaoDao.procura(13, campanha)).thenReturn(resultadoLigacaoInexistente);
    modificadorResultadoInexistenteFake = new ModificadorResultadoInexistenteFake(logger);
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    assertThat(modificadorResultadoInexistenteFake.accept(configuracoes, daoFactory,
        resultadoLigacaoAtendida, ligacao, cliente, campanha), is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalseUraReversa() throws Exception {
    when(configuracoes.isUraReversa()).thenReturn(true);
    assertThat(modificadorResultadoInexistenteFake.accept(configuracoes, daoFactory,
        resultadoLigacaoAtendida, ligacao, cliente, campanha), is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseResultado() throws Exception {
    assertThat(modificadorResultadoInexistenteFake.accept(configuracoes, daoFactory,
        resultadoLigacaoInexistente, ligacao, cliente, campanha), is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseAtendida() throws Exception {
    when(ligacao.isAtendida()).thenReturn(true);
    assertThat(modificadorResultadoInexistenteFake.accept(configuracoes, daoFactory,
        resultadoLigacaoAtendida, ligacao, cliente, campanha), is(false));
  }

  @Test
  public void modificaDeveriaRetornarInexistente() throws Exception {
    assertThat(modificadorResultadoInexistenteFake.modifica(configuracoes, daoFactory,
        resultadoLigacao, ligacao, cliente, campanha),
        is(sameInstance(resultadoLigacaoInexistente)));
  }

}
