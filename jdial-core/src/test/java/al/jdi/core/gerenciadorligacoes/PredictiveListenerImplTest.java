package al.jdi.core.gerenciadorligacoes;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoesImpl;
import al.jdi.core.gerenciadorligacoes.PredictiveListenerImpl;

public class PredictiveListenerImplTest {

  private static final int CALL_ID = 1;

  private static final int CAUSE = 2;

  private static final String AGENTE = "AGENTE";

  @Mock
  private GerenciadorLigacoesImpl owner;
  @Mock
  private Exception e;

  private PredictiveListenerImpl predictiveListenerImpl;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    predictiveListenerImpl = new PredictiveListenerImpl(owner);
  }

  @Test
  public void chamadaAtendidaHandleException() throws Exception {
    doThrow(new RuntimeException()).when(owner).chamadaAtendida(predictiveListenerImpl, CALL_ID);
    predictiveListenerImpl.chamadaAtendida(CALL_ID);
    verify(owner).chamadaAtendida(predictiveListenerImpl, CALL_ID);
  }

  @Test
  public void chamadaEmFilaHandleException() throws Exception {
    doThrow(new RuntimeException()).when(owner).chamadaEmFila(predictiveListenerImpl, CALL_ID);
    predictiveListenerImpl.chamadaEmFila(CALL_ID);
    verify(owner).chamadaEmFila(predictiveListenerImpl, CALL_ID);
  }

  @Test
  public void chamadaEncerradaHandleException() throws Exception {
    doThrow(new RuntimeException()).when(owner).chamadaEncerrada(predictiveListenerImpl, CALL_ID,
        CAUSE);
    predictiveListenerImpl.chamadaEncerrada(CALL_ID, CAUSE);
    verify(owner).chamadaEncerrada(predictiveListenerImpl, CALL_ID, CAUSE);
  }

  @Test
  public void chamadaErroHandleException() throws Exception {
    doThrow(new RuntimeException()).when(owner).chamadaErro(predictiveListenerImpl, e);
    predictiveListenerImpl.chamadaErro(e);
    verify(owner).chamadaErro(predictiveListenerImpl, e);
  }

  @Test
  public void chamadaIniciadaHandleException() throws Exception {
    doThrow(new RuntimeException()).when(owner).chamadaIniciada(predictiveListenerImpl, CALL_ID);
    predictiveListenerImpl.chamadaIniciada(CALL_ID);
    verify(owner).chamadaIniciada(predictiveListenerImpl, CALL_ID);
  }

  @Test
  public void chamadaInvalidaHandleException() throws Exception {
    doThrow(new RuntimeException()).when(owner).chamadaInvalida(predictiveListenerImpl, CALL_ID,
        CAUSE);
    predictiveListenerImpl.chamadaInvalida(CALL_ID, CAUSE);
    verify(owner).chamadaInvalida(predictiveListenerImpl, CALL_ID, CAUSE);
  }

  @Test
  public void chamadaNoAgenteHandleException() throws Exception {
    doThrow(new RuntimeException()).when(owner).chamadaNoAgente(predictiveListenerImpl, CALL_ID,
        AGENTE);
    predictiveListenerImpl.chamadaNoAgente(CALL_ID, AGENTE);
    verify(owner).chamadaNoAgente(predictiveListenerImpl, CALL_ID, AGENTE);
  }

}
