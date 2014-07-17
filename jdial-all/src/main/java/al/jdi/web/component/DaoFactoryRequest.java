package al.jdi.web.component;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;

@RequestScoped
public class DaoFactoryRequest {
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
