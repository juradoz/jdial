package al.jdi.web.controller;

import java.util.List;

import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;

@Controller
public class CampanhaController {

  private final Result result;
  private final DaoFactory daoFactory;

  /**
   * @deprecated CDI eyes only
   */
  public CampanhaController() {
    this(null, null);
  }

  @Inject
  public CampanhaController(Result result, DaoFactory daoFactory) {
    this.result = result;
    this.daoFactory = daoFactory;
  }

  @Path("/campanha")
  public void index() {
    result.include("variable", "VRaptor!");
  }

  @Path("/campanhas")
  public List<Campanha> list() {
    return daoFactory.getCampanhaDao().listaTudo();
  }
}
