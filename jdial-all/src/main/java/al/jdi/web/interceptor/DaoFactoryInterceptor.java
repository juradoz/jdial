package al.jdi.web.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import al.jdi.web.component.DaoFactoryRequest;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;

@Intercepts
@RequestScoped
public class DaoFactoryInterceptor {

  private final DaoFactoryRequest daoFactoryRequest;

  @Deprecated
  public DaoFactoryInterceptor() {
    this(null);
  }

  @Inject
  public DaoFactoryInterceptor(DaoFactoryRequest daoFactoryRequest) {
    this.daoFactoryRequest = daoFactoryRequest;
  }

  @AroundCall
  public void intercept(SimpleInterceptorStack stack) {
    daoFactoryRequest.get().beginTransaction();
    stack.next();
    daoFactoryRequest.get().commit();
  }
}
