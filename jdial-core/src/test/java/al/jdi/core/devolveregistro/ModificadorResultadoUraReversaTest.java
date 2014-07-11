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
import al.jdi.core.devolveregistro.ModificadorResultadoUraReversa;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.ResultadoLigacaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ModificadorResultadoUraReversaTest {

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
  private Configuracoes configuracoes;
  @Mock
  private ResultadoLigacaoDao resultadoLigacaoDao;
  @Mock
  private ResultadoLigacao resultadoLigacaoAtendida;
  @Mock
  private ResultadoLigacao resultadoLigacaoSemAgentes;
  @Mock
  private ResultadoLigacao resultadoLigacaoAbandonou;
  @Mock
  private ResultadoLigacao resultadoLigacaoSemInteresse;

  private ModificadorResultadoUraReversa modificadorResultadoUraReversa;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(configuracoes.isUraReversa()).thenReturn(true);
    when(daoFactory.getResultadoLigacaoDao()).thenReturn(resultadoLigacaoDao);
    when(resultadoLigacaoDao.procura(-1, campanha)).thenReturn(resultadoLigacaoAtendida);
    when(resultadoLigacaoDao.procura(23, campanha)).thenReturn(resultadoLigacaoSemAgentes);
    when(resultadoLigacaoDao.procura(-10, campanha)).thenReturn(resultadoLigacaoAbandonou);
    when(resultadoLigacaoDao.procura(-11, campanha)).thenReturn(resultadoLigacaoSemInteresse);
    modificadorResultadoUraReversa = new ModificadorResultadoUraReversa(configuracoes);
  }

  @Test
  public void acceptDeveriaRetornarTrueSemAgentes() throws Exception {
    assertThat(modificadorResultadoUraReversa.accept(daoFactory, resultadoLigacaoSemAgentes,
        ligacao, cliente, campanha), is(true));
  }

  @Test
  public void acceptDeveriaRetornarTrueAtendida() throws Exception {
    assertThat(modificadorResultadoUraReversa.accept(daoFactory, resultadoLigacaoAtendida, ligacao,
        cliente, campanha), is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalseUraReversa() throws Exception {
    when(configuracoes.isUraReversa()).thenReturn(false);
    assertThat(modificadorResultadoUraReversa.accept(daoFactory, resultadoLigacaoAtendida, ligacao,
        cliente, campanha), is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseResultado() throws Exception {
    assertThat(modificadorResultadoUraReversa.accept(daoFactory, resultadoLigacao, ligacao,
        cliente, campanha), is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseNoAgente() throws Exception {
    when(ligacao.isNoAgente()).thenReturn(true);
    assertThat(modificadorResultadoUraReversa.accept(daoFactory, resultadoLigacaoAtendida, ligacao,
        cliente, campanha), is(false));
  }

  @Test
  public void modificaDeveriaRetornarAbandonou() throws Exception {
    when(ligacao.isFoiPraFila()).thenReturn(true);
    assertThat(modificadorResultadoUraReversa.modifica(daoFactory, resultadoLigacao, ligacao,
        cliente, campanha), is(sameInstance(resultadoLigacaoAbandonou)));
  }

  @Test
  public void modificaDeveriaRetornarSemInteresse() throws Exception {
    when(ligacao.isFoiPraFila()).thenReturn(false);
    assertThat(modificadorResultadoUraReversa.modifica(daoFactory, resultadoLigacao, ligacao,
        cliente, campanha), is(sameInstance(resultadoLigacaoSemInteresse)));
  }

}
