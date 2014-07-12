package al.jdi.core;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class MainTest {

  @Mock
  private Service configuracoesService;
  @Mock
  private Service devolveRegistroService;
  @Mock
  private Service estoqueLivresService;
  @Mock
  private Service estoqueLivresAgendados;
  @Mock
  private Service gerenciadorAgentesService;
  @Mock
  private Service gerenciadorLigacoesService;
  @Mock
  private Service gerenciadorFatorKService;
  @Mock
  private Service dialerService;

  private Main main;

  @Before
  public void setUp() {
    initMocks(this);
    main =
        new Main(configuracoesService, devolveRegistroService, estoqueLivresService,
            estoqueLivresAgendados, gerenciadorAgentesService, gerenciadorLigacoesService,
            gerenciadorFatorKService, dialerService);
  }

  @Test
  public void start() throws Exception {
    main.start();
    verify(configuracoesService).start();
    verify(devolveRegistroService).start();
    verify(estoqueLivresService).start();
    verify(estoqueLivresAgendados).start();
    verify(gerenciadorAgentesService).start();
    verify(gerenciadorLigacoesService).start();
    verify(gerenciadorFatorKService).start();
    verify(dialerService).start();
  }

}
