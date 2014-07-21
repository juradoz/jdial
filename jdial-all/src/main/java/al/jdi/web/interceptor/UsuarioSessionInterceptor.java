package al.jdi.web.interceptor;

import javax.inject.Inject;

import al.jdi.web.session.UsuarioAutenticadoSession;
import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;

@Intercepts
public class UsuarioSessionInterceptor {
  private final Result result;
  private final UsuarioAutenticadoSession usuarioAutenticadoSession;

  @Deprecated
  public UsuarioSessionInterceptor() {
    this(null, null);
  }

  @Inject
  public UsuarioSessionInterceptor(Result result,
      UsuarioAutenticadoSession usuarioAutenticadoSession) {
    this.result = result;
    this.usuarioAutenticadoSession = usuarioAutenticadoSession;
  }

  @Accepts
  public boolean accepts() {
    return usuarioAutenticadoSession.getUsuario() != null;
  }

  @BeforeCall
  public void addToSession() {
    result.include("usuarioAutenticadoSession", usuarioAutenticadoSession);
  }
}
