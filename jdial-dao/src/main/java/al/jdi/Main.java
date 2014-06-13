package al.jdi;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.jdi.dao.DaoFactory;
import al.jdi.dao.model.ResultadoLigacao;

public class Main {

  private final DaoFactory daoFactory;

  @Inject
  public Main(DaoFactory daoFactory) {
    this.daoFactory = daoFactory;
  }

  public void main(@Observes ContainerInitialized event) {
    for (ResultadoLigacao resultadoLigacao : daoFactory.getResultadoLigacaoDao().listaTudo()) {
      System.out.println(resultadoLigacao);
    }
  }
}
