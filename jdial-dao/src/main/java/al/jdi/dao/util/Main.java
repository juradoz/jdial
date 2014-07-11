package al.jdi.dao.util;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;

public class Main {

  @Inject
  private Logger logger;
  @Inject
  private DaoFactory daoFactory;

  public void test(@Observes ContainerInitialized event) {
    try {
      daoFactory.beginTransaction();
      List<Campanha> campanhas = daoFactory.getCampanhaDao().listaTudo();
      for (Campanha campanha : campanhas) {
        logger.info("{}", campanha);
      }
      daoFactory.commit();
    } finally {
      daoFactory.close();
    }
  }
}
