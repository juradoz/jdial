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
import al.jdi.core.devolveregistro.ModificadorResultado.ResultadosConhecidos;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.ResultadoLigacaoDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.MotivoSistema;
import al.jdi.dao.model.ResultadoLigacao;

public class ModificadorResultadoSemAgentesFakeTest {

  private ModificadorResultadoSemAgentesFake modificadorResultadoSemAgentesFake;

  @Mock
  private Configuracoes configuracoes;
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
  private ResultadoLigacao resultadoLigacaoSemAgentes;
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getResultadoLigacaoDao()).thenReturn(resultadoLigacaoDao);
    when(resultadoLigacaoDao.procura(MotivoSistema.ATENDIDA.getCodigo(), campanha)).thenReturn(
        resultadoLigacaoAtendida);
    when(resultadoLigacaoDao.procura(ResultadosConhecidos.SEM_AGENTES.getCodigo(), campanha))
        .thenReturn(resultadoLigacaoSemAgentes);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(tenant.getCampanha()).thenReturn(campanha);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    modificadorResultadoSemAgentesFake = new ModificadorResultadoSemAgentesFake();
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    assertThat(modificadorResultadoSemAgentesFake.accept(tenant, daoFactory, ligacao,
        resultadoLigacaoAtendida), is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalseUraReversa() throws Exception {
    when(configuracoes.isUraReversa()).thenReturn(true);
    assertThat(modificadorResultadoSemAgentesFake.accept(tenant, daoFactory, ligacao,
        resultadoLigacaoAtendida), is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseResultado() throws Exception {
    assertThat(
        modificadorResultadoSemAgentesFake.accept(tenant, daoFactory, ligacao, resultadoLigacao),
        is(false));
  }

  @Test
  public void acceptDeveriaRetornarFalseNoAgente() throws Exception {
    when(ligacao.isNoAgente()).thenReturn(true);
    assertThat(modificadorResultadoSemAgentesFake.accept(tenant, daoFactory, ligacao,
        resultadoLigacaoAtendida), is(false));
  }

  @Test
  public void modificaDeveriaRetornarSemAgentes() throws Exception {
    assertThat(
        modificadorResultadoSemAgentesFake.modifica(tenant, daoFactory, ligacao, resultadoLigacao),
        is(sameInstance(resultadoLigacaoSemAgentes)));
  }

}
