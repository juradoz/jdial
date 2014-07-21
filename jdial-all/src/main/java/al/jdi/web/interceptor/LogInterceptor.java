package al.jdi.web.interceptor;

import static org.apache.commons.lang3.StringUtils.join;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Usuario;
import al.jdi.dao.model.WebLog;
import al.jdi.web.interceptor.LogInterceptor.LogAcesso;
import al.jdi.web.session.UsuarioAutenticadoSession;
import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.controller.ControllerInstance;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;

// @Intercepts(after = DaoFactoryInterceptor.class)
@AcceptsWithAnnotations(LogAcesso.class)
public class LogInterceptor {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD})
  public @interface LogAcesso {
  }

  private final UsuarioAutenticadoSession usuarioAutenticadoSession;
  private final MethodInfo methodInfo;
  private final DaoFactory daoFactory;

  @Deprecated
  public LogInterceptor() {
    this(null, null, null);
  }

  @Inject
  public LogInterceptor(UsuarioAutenticadoSession usuarioAutenticadoSession, MethodInfo methodInfo,
      DaoFactory daoFactory) {
    this.usuarioAutenticadoSession = usuarioAutenticadoSession;
    this.methodInfo = methodInfo;
    this.daoFactory = daoFactory;
  }

  @BeforeCall
  public void intercept(ControllerInstance instance, ControllerMethod method)
      throws InterceptionException {
    Usuario usuario = usuarioAutenticadoSession.getUsuario();

    if (usuario == null) {
      usuario = usuarioAutenticadoSession.getUsuario();
      if (usuario == null)
        return;
    }

    WebLog webLog = new WebLog();
    webLog.setUsuario(usuario);
    webLog.setMessage(String.format("%s/%s(%s)", instance.getController().getClass()
        .getSimpleName(), method.getMethod().getName(),
        join(methodInfo.getParametersValues(), ", ")));
    daoFactory.getWebLog().adiciona(webLog);
  }

}
