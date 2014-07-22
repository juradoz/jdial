package al.jdi.web.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Specializes
@RequestScoped
public class TilesPathResolver extends DefaultPathResolver {

  @Inject
  protected TilesPathResolver(FormatResolver resolver) {
    super(resolver);
  }

  @Override
  protected String getPrefix() {
    return "/";
  }

  @Override
  protected String getExtension() {
    return "tiles";
  }
}
