package al.jdi.web.controller;

import java.util.List;

import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import br.com.caelum.vraptor.Controller;

@Controller
public class CampanhaController {
  private final DaoFactory daoFactory;

  @Deprecated
  public CampanhaController() {
    this.daoFactory = null;
  }

  @Inject
  public CampanhaController(DaoFactory daoFactory) {
    this.daoFactory = daoFactory;
  }

  List<Campanha> list() {
    return daoFactory.getCampanhaDao().listaTudo();
  }
}
