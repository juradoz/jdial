package al.jdi.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import javax.inject.Provider;
import javax.telephony.ProviderEvent;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.estoque.Estoque;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoes;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.cti.DialerCtiManager;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Servico;

public class JDialTest {

  private static final String CAMPANHA = "CAMPANHA";
  private static final String NOME_BASE_DADOS = "NOME_BASE_DADOS";
  private static final String NOME_BASE = "NOME_BASE";
  private static final int OPERADOR = 1;
  private static final Double FATOR_K = 1.0;
  private static final Integer QTD_LIGACOES = 1;
  private static final Integer QTD_LIVRES = 1;
  private static final Integer QTD_LIGACOES_NAO_ATENDIDAS = 0;
  private static final DateTime DATA_BANCO = new DateTime();
  private final String versao = "VERSAO";

  private DefaultJDial jDial;

  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Engine.Factory engineFactory;
  @Mock
  private GerenciadorAgentes gerenciadorAgentes;
  @Mock
  private GerenciadorLigacoes gerenciadorLigacoes;
  @Mock
  private Estoque estoqueLivres;
  @Mock
  private Estoque estoqueAgendados;
  @Mock
  private Discavel.Factory discavelFactory;
  @Mock
  private Provider<DaoFactory> daoFactoryProvider;
  @Mock
  private TratadorEspecificoCliente tratadorEspecificoCliente;
  @Mock
  private GerenciadorFatorK gerenciadorFatorK;
  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Dao<Campanha> campanhaDao;
  @Mock
  private Campanha campanha;
  @Mock
  private ClienteDao clienteDao;
  @Mock
  private Cliente cliente;
  @Mock
  private Discavel discavel;
  @Mock
  private Servico servico;
  @Mock
  private Logger logger;
  @Mock
  private DialerCtiManager dialerCtiManager;
  @Mock
  private ProviderEvent event;

  @Before
  public void setUp() throws Exception {
    initMocks(this);

    when(daoFactoryProvider.get()).thenReturn(daoFactory);
    when(daoFactory.getCampanhaDao()).thenReturn(campanhaDao);
    when(daoFactory.getDataBanco()).thenReturn(DATA_BANCO);
    when(campanhaDao.procura(CAMPANHA)).thenReturn(campanha);
    when(campanha.getNome()).thenReturn(CAMPANHA);
    when(campanha.getServico()).thenReturn(servico);
    when(tratadorEspecificoCliente.obtemClienteDao(daoFactory)).thenReturn(clienteDao);

    when(configuracoes.getNomeCampanha()).thenReturn(CAMPANHA);
    when(configuracoes.getNomeBaseDados()).thenReturn(NOME_BASE_DADOS);
    when(configuracoes.getNomeBase()).thenReturn(NOME_BASE);
    when(configuracoes.getOperador()).thenReturn(OPERADOR);

    when(gerenciadorFatorK.getFatorK()).thenReturn(FATOR_K);
    when(gerenciadorLigacoes.getQuantidadeLigacoes()).thenReturn(QTD_LIGACOES);
    when(gerenciadorLigacoes.getQuantidadeLigacoesNaoAtendidas()).thenReturn(
        QTD_LIGACOES_NAO_ATENDIDAS);
    when(gerenciadorAgentes.getLivres()).thenReturn(QTD_LIVRES);

    when(estoqueLivres.obtemRegistros((int) (FATOR_K * QTD_LIVRES) - QTD_LIGACOES_NAO_ATENDIDAS))
        .thenReturn(Arrays.asList(cliente));
    when(estoqueAgendados.obtemRegistros((int) (FATOR_K * QTD_LIVRES) - QTD_LIGACOES_NAO_ATENDIDAS))
        .thenReturn(Arrays.asList(cliente));

    when(discavelFactory.create(cliente)).thenReturn(discavel);

    jDial =
        new DefaultJDial(logger, configuracoes, engineFactory, versao, gerenciadorAgentes,
            gerenciadorLigacoes, estoqueLivres, estoqueAgendados, discavelFactory,
            daoFactoryProvider, tratadorEspecificoCliente, gerenciadorFatorK, dialerCtiManager);
  }

  @Test
  public void limpaReservaDeveriaLimar() throws Exception {
    jDial.limpaReservas(configuracoes, daoFactoryProvider, tratadorEspecificoCliente);
    verify(clienteDao, times(2)).limpaReservas(campanha, NOME_BASE_DADOS, NOME_BASE, OPERADOR);
  }

  @Test
  public void rodadaNaoDeveriaObterClientesSemAgentes() throws Exception {
    when(gerenciadorAgentes.getLivres()).thenReturn(0);
    jDial.rodada(daoFactory, estoqueLivres);
    verify(estoqueLivres, never()).obtemRegistros(Mockito.anyInt());
  }

  @Test
  public void rodadaDeveriaObterCliente() throws Exception {
    jDial.rodada(daoFactory, estoqueLivres);
    verify(estoqueLivres).obtemRegistros(1);
  }

  @Test
  public void rodadaDeveriaDiscar() throws Exception {
    jDial.rodada(daoFactory, estoqueLivres);
    ArgumentCaptor<Ligacao> captor = ArgumentCaptor.forClass(Ligacao.class);
    verify(gerenciadorLigacoes).disca(captor.capture(), Mockito.eq(servico));
    assertThat(captor.getValue().getDiscavel(), is(sameInstance(discavel)));
    assertThat(captor.getValue().getInicio(), is(DATA_BANCO));
  }

  @Test
  public void runNaoDeveriaExecutar() throws Exception {
    when(configuracoes.getSistemaAtivo()).thenReturn(false);
    jDial.run();
    verify(daoFactoryProvider, times(1)).get();
  }

  @Test
  public void runDeveriaExecutar() throws Exception {
    when(configuracoes.getSistemaAtivo()).thenReturn(true);
    jDial.providerInService(event);
    jDial.run();
    verify(daoFactoryProvider, times(2)).get();
  }

}
