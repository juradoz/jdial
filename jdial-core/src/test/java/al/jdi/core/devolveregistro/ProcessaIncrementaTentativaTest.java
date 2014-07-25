package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.TelefoneDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.MotivoFinalizacao;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Telefone;

public class ProcessaIncrementaTentativaTest {

  private ProcessaIncrementaTentativa processaIncrementaTentativa;

  private static final Long TELEFONE_ID = 1l;
  @Mock
  private Configuracoes configuracoes;
  @Mock
  private FinalizadorCliente finalizadorCliente;
  @Mock
  private NotificadorCliente notificadorCliente;
  @Mock
  private Ligacao ligacao;
  @Mock
  private Cliente cliente;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private TelefoneDao telefoneDao;
  @Mock
  private Telefone telefone;
  @Mock
  private Dao<MotivoFinalizacao> motivoFinalizacaoDao;
  @Mock
  private MotivoFinalizacao motivoFinalizacao;
  @Mock
  private Mailing mailing;
  @Mock
  private Campanha campanha;
  @Mock
  private Tenant tenant;
  @Mock
  private Discavel discavel;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(daoFactory.getTelefoneDao()).thenReturn(telefoneDao);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(telefoneDao.procura(TELEFONE_ID)).thenReturn(telefone);
    when(telefone.getId()).thenReturn(TELEFONE_ID);
    when(daoFactory.getMotivoFinalizacaoDao()).thenReturn(motivoFinalizacaoDao);
    when(motivoFinalizacaoDao.procura("Excesso tentativas")).thenReturn(motivoFinalizacao);
    when(cliente.getMailing()).thenReturn(mailing);
    when(mailing.getCampanha()).thenReturn(campanha);
    when(tenant.getConfiguracoes()).thenReturn(configuracoes);
    when(ligacao.getDiscavel()).thenReturn(discavel);
    when(discavel.getCliente()).thenReturn(cliente);
    processaIncrementaTentativa =
        new ProcessaIncrementaTentativa(finalizadorCliente, notificadorCliente);
  }

  @Test
  public void getOrdemDeveriaRetornar() throws Exception {
    assertThat(processaIncrementaTentativa.getOrdem(), is(6));
  }

  @Test
  public void acceptDeveriaRetornarTrue() throws Exception {
    when(resultadoLigacao.isIncrementaTentativa()).thenReturn(true);
    assertThat(processaIncrementaTentativa.accept(tenant, daoFactory, ligacao, resultadoLigacao),
        is(true));
  }

  @Test
  public void acceptDeveriaRetornarFalse() throws Exception {
    when(resultadoLigacao.isIncrementaTentativa()).thenReturn(false);
    assertThat(processaIncrementaTentativa.accept(tenant, daoFactory, ligacao, resultadoLigacao),
        is(false));
  }

  @Test
  public void executaDeveriaIncrementar() throws Exception {
    assertThat(processaIncrementaTentativa.executa(tenant, daoFactory, ligacao, resultadoLigacao),
        is(true));
    verify(telefone).incTentativa();
  }

  @Test
  public void executaDeveriaAtualizar() throws Exception {
    assertThat(processaIncrementaTentativa.executa(tenant, daoFactory, ligacao, resultadoLigacao),
        is(true));
    verify(telefoneDao).atualiza(telefone);
  }

  // TODO : Testar e separar limitadores tentativas

}
