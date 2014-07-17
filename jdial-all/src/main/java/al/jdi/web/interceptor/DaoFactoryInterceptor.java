package al.jdi.web.interceptor;

import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.web.component.DaoFactoryRequest;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;

@Intercepts
public class DaoFactoryInterceptor {

  private final DaoFactory daoFactory;

  @Deprecated
  public DaoFactoryInterceptor() {
    this.daoFactory = null;
  }

  @Inject
  public DaoFactoryInterceptor(DaoFactoryRequest daoFactoryRequest) {
    this.daoFactory = daoFactoryRequest.get();
  }

  @AroundCall
  public void intercept(SimpleInterceptorStack stack) {
    try {
      daoFactory.beginTransaction();
      stack.next();
      daoFactory.commit();
    } finally {
      if (daoFactory.hasTransaction())
        daoFactory.rollback();
      daoFactory.close();
    }
  }
}
