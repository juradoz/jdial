package al.jdi.web.controller;

import java.util.Collection;

import javax.inject.Inject;

import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.DefinicaoPadrao;
import al.jdi.dao.model.Usuario.TipoPerfil;
import al.jdi.web.interceptor.LogInterceptor.LogAcesso;
import al.jdi.web.interceptor.Permissao;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Permissao(TipoPerfil.ADMINISTRADOR)
@Controller
public class DefinicaoPadraoController {
	private final DaoFactory daoFactory;
	private final Result result;

	@Deprecated
	public DefinicaoPadraoController() {
	  this(null, null);
    }

	@Inject
	public DefinicaoPadraoController(DaoFactory daoFactory, Result result) {
		this.daoFactory = daoFactory;
		this.result = result;
	}

	@LogAcesso
	public void add(DefinicaoPadrao definicaoPadrao) {
		daoFactory.getDefinicaoPadraoDao().adiciona(definicaoPadrao);
		result.use(Results.logic()).redirectTo(DefinicaoPadraoController.class)
				.list();
	}

	@Put
	@Path("/definicaoPadrao")
	public void adicionar(DefinicaoPadrao definicaoPadrao) {
		result.use(Results.logic()).redirectTo(DefinicaoPadraoController.class)
				.formularioDefinicaoPadrao("add", definicaoPadrao);
	}

	@LogAcesso
	@Delete
	@Path("/definicaoPadrao/{definicaoPadrao.id}")
	public void delete(DefinicaoPadrao definicaoPadrao) {
		definicaoPadrao = daoFactory.getDefinicaoPadraoDao().procura(
				definicaoPadrao.getId());
		daoFactory.getDefinicaoPadraoDao().remove(definicaoPadrao);
		result.use(Results.logic()).redirectTo(DefinicaoPadraoController.class)
				.list();
	}

	@LogAcesso
	public void edit(DefinicaoPadrao definicaoPadrao) {
		daoFactory.getDefinicaoPadraoDao().atualiza(definicaoPadrao);
		result.use(Results.logic()).redirectTo(DefinicaoPadraoController.class)
				.list();
	}

	@Get
	@Path("/definicaoPadrao/{definicaoPadrao.id}")
	public void editar(DefinicaoPadrao definicaoPadrao) {
		definicaoPadrao = daoFactory.getDefinicaoPadraoDao().procura(
				definicaoPadrao.getId());
		result.use(Results.logic()).forwardTo(DefinicaoPadraoController.class)
				.formularioDefinicaoPadrao("edit", definicaoPadrao);
	}

	public void formularioDefinicaoPadrao(String action,
			DefinicaoPadrao definicaoPadrao) {
		result.include("formAction", action);
		result.include("definicaoPadrao", definicaoPadrao);
	}

	@Get
	@Path("/definicaoPadrao")
	public Collection<DefinicaoPadrao> list() {
		return daoFactory.getDefinicaoPadraoDao().listaTudo();
	}
}
