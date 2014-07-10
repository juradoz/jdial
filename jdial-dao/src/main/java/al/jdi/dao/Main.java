package al.jdi.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;

public class Main {

  @Inject
  private DaoFactory daoFactory;

  @PostConstruct
  public void open() {
    daoFactory.beginTransaction();
  }

  public void teste(@Observes ContainerInitialized event) {
    try {
      List<Campanha> campanhas = daoFactory.getCampanhaDao().listaTudo();
      for (Campanha campanha : campanhas) {
        System.out.println(campanha);
      }
    } finally {
      daoFactory.commit();
    }
  }

  @PreDestroy
  public void close() {
    if (daoFactory.hasTransaction())
      daoFactory.rollback();
    daoFactory.close();
  }

}
