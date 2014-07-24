package al.jdi.web.component;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;

@Vetoed
public class DaoFactoryRequest {

  static class Producer {
    @Produces
    @RequestScoped
    public DaoFactoryRequest produce(DaoFactory daoFactory) {
      return new DaoFactoryRequest(daoFactory);
    }

    public void dispose(@Disposes DaoFactoryRequest daoFactoryRequest) {
      daoFactoryRequest.get().close();
    }
  }

  private final DaoFactory daoFactory;

  @Deprecated
  public DaoFactoryRequest() {
    this(null);
  }

  @Inject
  public DaoFactoryRequest(DaoFactory daoFactory) {
    this.daoFactory = daoFactory;
  }

  public DaoFactory get() {
    return daoFactory;
  }
}
