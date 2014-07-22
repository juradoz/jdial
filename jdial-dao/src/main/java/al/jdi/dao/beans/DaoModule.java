package al.jdi.dao.beans;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;

class DaoModule {

  private final Logger logger;

  @Inject
  public DaoModule(Logger logger) {
    this.logger = logger;
  }

  @RequestScoped
  @Produces
  public DaoFactory create(Logger logger, SessionHandler sessionHandler) {
    DaoFactory daoFactory = new DefaultDaoFactory(logger, sessionHandler);
    logger.debug("Creating {}", daoFactory);
    return daoFactory;
  }

  public void close(@Disposes DaoFactory daoFactory) {
    logger.debug("Disposing {}", daoFactory);
    daoFactory.close();
  }
}
