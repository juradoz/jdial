package al.jdi.web.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.WebLog;
import al.jdi.web.session.UsuarioAutenticadoSession;
import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AfterCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.MethodInfo;

@Intercepts(after = DaoFactoryInterceptor.class)
@RequestScoped
public class DBLogInterceptor {
  @Retention(RetentionPolicy.RUNTIME)
  public @interface LogAcesso {
  }

  private final Logger logger;
  private final DaoFactory daoFactory;
  private final UsuarioAutenticadoSession usuarioAutenticadoSession;
  private final ControllerMethod method;
  private final MethodInfo methodInfo;

  @Deprecated
  public DBLogInterceptor() {
    this(null, null, null, null, null);
  }

  @Inject
  public DBLogInterceptor(Logger logger, DaoFactory daoFactory,
      UsuarioAutenticadoSession usuarioAutenticadoSession, ControllerMethod method,
      MethodInfo methodInfo) {
    this.logger = logger;
    this.daoFactory = daoFactory;
    this.usuarioAutenticadoSession = usuarioAutenticadoSession;
    this.method = method;
    this.methodInfo = methodInfo;
  }

  @Accepts
  public boolean accepts(ControllerMethod method) {
    return method.getController().getType().isAnnotationPresent(LogAcesso.class)
        || method.containsAnnotation(LogAcesso.class);
  }

  @AfterCall
  public void afterCall() {
    logger.info("Usuario {}: {}/{}({})", usuarioAutenticadoSession.getUsuario(), method
        .getController().getType().getSimpleName(), method.getMethod().getName(),
        StringUtils.join(methodInfo.getParametersValues()));

    String msg =
        String.format("%s/%s(%s)", method.getController().getType().getSimpleName(), method
            .getMethod().getName(), StringUtils.join(methodInfo.getParametersValues()));

    WebLog webLog = new WebLog();
    webLog.setUsuario(usuarioAutenticadoSession.getUsuario());
    webLog.setMessage(msg);

    daoFactory.getWebLog().adiciona(webLog);
  }
}
