package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.ResultadoLigacaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ModificadorResultadoAtendidoFakeTest {

  private ModificadorResultadoAtendidoFake modificadorResultadoAtendidoFake;

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

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    initMocks(this);
    when(daoFactory.getResultadoLigacaoDao()).thenReturn(resultadoLigacaoDao);
    when(resultadoLigacaoDao.procura(-1, campanha)).thenReturn(resultadoLigacaoAtendida);
    when(resultadoLigacaoDao.procura(13, campanha)).thenReturn(resultadoLigacaoInexistente);
    when(ligacao.isAtendida()).thenReturn(true);
    modificadorResultadoAtendidoFake = new ModificadorResultadoAtendidoFake();
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    assertThat(modificadorResultadoAtendidoFake.accept(configuracoes, daoFactory,
        resultadoLigacaoInexistente, ligacao, cliente, campanha), is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalseUraReversa() throws Exception {
    when(configuracoes.isUraReversa()).thenReturn(true);
    assertThat(modificadorResultadoAtendidoFake.accept(configuracoes, daoFactory,
        resultadoLigacaoInexistente, ligacao, cliente, campanha), is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseResultado() throws Exception {
    assertThat(modificadorResultadoAtendidoFake.accept(configuracoes, daoFactory,
        resultadoLigacaoAtendida, ligacao, cliente, campanha), is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseAtendida() throws Exception {
    when(ligacao.isAtendida()).thenReturn(false);
    assertThat(modificadorResultadoAtendidoFake.accept(configuracoes, daoFactory,
        resultadoLigacaoInexistente, ligacao, cliente, campanha), is(false));
  }

  @Test
  public void modificaDeveriaRetornarAtendida() throws Exception {
    assertThat(modificadorResultadoAtendidoFake.modifica(configuracoes, daoFactory,
        resultadoLigacao, ligacao, cliente, campanha), is(sameInstance(resultadoLigacaoAtendida)));
  }

}
