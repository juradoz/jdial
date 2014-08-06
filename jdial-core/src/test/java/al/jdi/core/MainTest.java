package al.jdi.core;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.common.Service;

public class MainTest {

  private Main main;
  
    @Mock
    private Service devolveRegistroService;
    @Mock
    private ShutdownHook.Factory shutdownHookFactory;
    @Mock
    private Service dialerCtiManagerService;
    @Mock
    private Service tenantService;
  
    @Before
    public void setUp() {
      initMocks(this);
      main =
          new Main(devolveRegistroService, dialerCtiManagerService, tenantService,
              shutdownHookFactory);
    }
  
    @Test
    public void start() throws Exception {
      main.start();
      verify(devolveRegistroService).start();
      verify(dialerCtiManagerService).start();
      verify(tenantService).start();
    }

}
