package net.danieljurado.dialer;

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

import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.estoque.Estoque;
import net.danieljurado.dialer.gerenciadoragentes.GerenciadorAgentes;
import net.danieljurado.dialer.gerenciadorfatork.GerenciadorFatorK;
import net.danieljurado.dialer.gerenciadorligacoes.GerenciadorLigacoes;
import net.danieljurado.dialer.modelo.Discavel;
import net.danieljurado.dialer.modelo.Ligacao;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.jdial.common.Engine;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.Dao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Servico;

public class DialerImplTest {

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

  private DialerImpl dialerImpl;

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

    dialerImpl =
        new DialerImpl(configuracoes, engineFactory, versao, gerenciadorAgentes,
            gerenciadorLigacoes, estoqueLivres, estoqueAgendados, discavelFactory,
            daoFactoryProvider, tratadorEspecificoCliente, gerenciadorFatorK);
  }

  @Test
  public void limpaReservaDeveriaLimar() throws Exception {
    dialerImpl.limpaReservas(configuracoes, daoFactoryProvider, tratadorEspecificoCliente);
    verify(clienteDao, times(2)).limpaReservas(campanha, NOME_BASE_DADOS, NOME_BASE, OPERADOR);
  }

  @Test
  public void rodadaNaoDeveriaObterClientesSemAgentes() throws Exception {
    when(gerenciadorAgentes.getLivres()).thenReturn(0);
    dialerImpl.rodada(daoFactory, estoqueLivres);
    verify(estoqueLivres, never()).obtemRegistros(Mockito.anyInt());
  }

  @Test
  public void rodadaDeveriaObterCliente() throws Exception {
    dialerImpl.rodada(daoFactory, estoqueLivres);
    verify(estoqueLivres).obtemRegistros(1);
  }

  @Test
  public void rodadaDeveriaDiscar() throws Exception {
    dialerImpl.rodada(daoFactory, estoqueLivres);
    ArgumentCaptor<Ligacao> captor = ArgumentCaptor.forClass(Ligacao.class);
    verify(gerenciadorLigacoes).disca(captor.capture(), Mockito.eq(servico));
    assertThat(captor.getValue().getDiscavel(), is(sameInstance(discavel)));
    assertThat(captor.getValue().getInicio(), is(DATA_BANCO));
  }

  @Test
  public void runNaoDeveriaExecutar() throws Exception {
    when(configuracoes.getSistemaAtivo()).thenReturn(false);
    dialerImpl.run();
    verify(daoFactoryProvider, times(1)).get();
  }

  @Test
  public void runDeveriaExecutar() throws Exception {
    when(configuracoes.getSistemaAtivo()).thenReturn(true);
    dialerImpl.run();
    verify(daoFactoryProvider, times(2)).get();
  }

}
