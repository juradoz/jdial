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
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.ResultadoLigacaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class ModificadorResultadoUraReversaTest {

  private ModificadorResultadoUraReversa modificadorResultadoUraReversa;

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
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(configuracoes.isUraReversa()).thenReturn(true);
    when(daoFactory.getResultadoLigacaoDao()).thenReturn(resultadoLigacaoDao);
    when(resultadoLigacaoDao.procura(-1, campanha)).thenReturn(resultadoLigacaoAtendida);
    when(resultadoLigacaoDao.procura(23, campanha)).thenReturn(resultadoLigacaoSemAgentes);
    when(resultadoLigacaoDao.procura(-10, campanha)).thenReturn(resultadoLigacaoAbandonou);
    when(resultadoLigacaoDao.procura(-11, campanha)).thenReturn(resultadoLigacaoSemInteresse);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(tenant.getCampanha()).thenReturn(campanha);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    modificadorResultadoUraReversa = new ModificadorResultadoUraReversa();
  }

  @Test
  public void acceptDeveriaRetornarTrueSemAgentes() throws Exception {
    assertThat(modificadorResultadoUraReversa.accept(tenant, daoFactory,
        resultadoLigacaoSemAgentes, ligacao), is(true));
  }

  @Test
  public void acceptDeveriaRetornarTrueAtendida() throws Exception {
    assertThat(modificadorResultadoUraReversa.accept(tenant, daoFactory, resultadoLigacaoAtendida,
        ligacao), is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalseUraReversa() throws Exception {
    when(configuracoes.isUraReversa()).thenReturn(false);
    assertThat(modificadorResultadoUraReversa.accept(tenant, daoFactory, resultadoLigacaoAtendida,
        ligacao), is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseResultado() throws Exception {
    assertThat(
        modificadorResultadoUraReversa.accept(tenant, daoFactory, resultadoLigacao, ligacao),
        is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseNoAgente() throws Exception {
    when(ligacao.isNoAgente()).thenReturn(true);
    assertThat(modificadorResultadoUraReversa.accept(tenant, daoFactory, resultadoLigacaoAtendida,
        ligacao), is(false));
  }

  @Test
  public void modificaDeveriaRetornarAbandonou() throws Exception {
    when(ligacao.isFoiPraFila()).thenReturn(true);
    assertThat(
        modificadorResultadoUraReversa.modifica(tenant, daoFactory, resultadoLigacao, ligacao),
        is(sameInstance(resultadoLigacaoAbandonou)));
  }

  @Test
  public void modificaDeveriaRetornarSemInteresse() throws Exception {
    when(ligacao.isFoiPraFila()).thenReturn(false);
    assertThat(
        modificadorResultadoUraReversa.modifica(tenant, daoFactory, resultadoLigacao, ligacao),
        is(sameInstance(resultadoLigacaoSemInteresse)));
  }

}
