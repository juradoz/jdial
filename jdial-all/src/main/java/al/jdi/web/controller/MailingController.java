package al.jdi.web.controller;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static org.apache.commons.collections.ComparatorUtils.naturalComparator;
import static org.apache.commons.collections.ComparatorUtils.reversedComparator;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.splitByWholeSeparator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;

import javax.inject.Inject;

import al.jdi.dao.beans.TelefoneDao;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.Telefone;
import al.jdi.dao.model.Usuario.TipoPerfil;
import al.jdi.web.component.DaoFactoryRequest;
import al.jdi.web.interceptor.DBLogInterceptor.LogAcesso;
import al.jdi.web.interceptor.Permissao;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Permissao(TipoPerfil.ADMINISTRADOR)
@Controller
public class MailingController {
  private final DaoFactoryRequest daoFactoryRequest;
  private final Result result;

  @Deprecated
  public MailingController() {
    this(null, null);
  }

  @Inject
  public MailingController(DaoFactoryRequest daoFactoryRequest, Result result) {
    this.daoFactoryRequest = daoFactoryRequest;
    this.result = result;
  }

  @LogAcesso
  public void add(Mailing mailing) {
    Campanha campanha =
        daoFactoryRequest.get().getCampanhaDao().procura(mailing.getCampanha().getId());
    campanha.setLimpaMemoria(true);
    daoFactoryRequest.get().getCampanhaDao().atualiza(campanha);
    daoFactoryRequest.get().getMailingDao().adiciona(mailing);
    result.use(Results.logic()).redirectTo(MailingController.class).list(mailing.getCampanha());
  }

  @Put
  @Path("/mailing")
  public void adicionar() {
    result.use(Results.logic()).redirectTo(MailingController.class).formularioMailing("add", null);
  }

  @Post
  @LogAcesso
  public void ajaxAtivarDesativar(Mailing mailing) {
    mailing = daoFactoryRequest.get().getMailingDao().procura(mailing.getId());
    mailing.setAtivo(!mailing.isAtivo());
    mailing.getCampanha().setLimpaMemoria(true);
    daoFactoryRequest.get().getCampanhaDao().atualiza(mailing.getCampanha());
    daoFactoryRequest.get().getMailingDao().atualiza(mailing);
    if (mailing.isAtivo())
      result.forwardTo(this).ativado();
    else
      result.forwardTo(this).desativado();
  }

  @LogAcesso
  public void ativado() {}

  public Collection<Campanha> campanhas() {
    return sort(daoFactoryRequest.get().getCampanhaDao().listaTudo(), on(Campanha.class).getNome());
  }

  @LogAcesso
  @Delete
  @Path("/mailing/{mailing.id}")
  public void delete(Mailing mailing) {
    mailing = daoFactoryRequest.get().getMailingDao().procura(mailing.getId());
    mailing.getCampanha().setLimpaMemoria(true);
    daoFactoryRequest.get().getCampanhaDao().atualiza(mailing.getCampanha());
    daoFactoryRequest.get().getMailingDao().remove(mailing);
    result.use(Results.logic()).redirectTo(MailingController.class).list(mailing.getCampanha());
  }

  @LogAcesso
  public void desativado() {}

  @LogAcesso
  public void edit(Mailing mailing) {
    Campanha campanha =
        daoFactoryRequest.get().getCampanhaDao().procura(mailing.getCampanha().getId());
    campanha.setLimpaMemoria(true);
    daoFactoryRequest.get().getCampanhaDao().atualiza(campanha);
    daoFactoryRequest.get().getMailingDao().atualiza(mailing);
    result.use(Results.logic()).redirectTo(MailingController.class).list(mailing.getCampanha());
  }

  @Get
  @Path("mailing/{mailing.id}")
  public void editar(Mailing mailing) {
    mailing = daoFactoryRequest.get().getMailingDao().procura(mailing.getId());
    result.use(Results.logic()).forwardTo(MailingController.class)
        .formularioMailing("edit", mailing);
  }

  public void formularioMailing(String action, Mailing mailing) {
    result.include("campanhaList", campanhas());
    result.include("formAction", action);
    result.include("formAction", action);
    result.include("mailing", mailing);
  }

  @Get
  @Path("/mailing/purge/{mailing.id}")
  public void formularioPurge(Mailing mailing) {
    mailing = daoFactoryRequest.get().getMailingDao().procura(mailing.getId());
    result.include("mailing", mailing);
  }

  @SuppressWarnings("unchecked")
  @Path("/mailing/campanha/{campanha.id}")
  public Collection<Mailing> list(Campanha campanha) {
    campanha = daoFactoryRequest.get().getCampanhaDao().procura(campanha.getId());
    result.include("campanha", campanha);
    return sort(daoFactoryRequest.get().getMailingDao().listaTudo(campanha), on(Mailing.class)
        .getId(), reversedComparator(naturalComparator()));
  }

  @LogAcesso
  @Post
  public void purge(Mailing mailing, UploadedFile file) throws IOException {
    int qtdLinhas = 0, qtdExpurgados = 0, qtdNaoEncontrados = 0, qtdErros = 0;

    if (file == null || file.getFile() == null) {
      LinkedList<String> errors = new LinkedList<String>();
      errors.add("Selecione um arquivo!");
      result.include("errors", errors);
      result.forwardTo(MailingController.class).formularioPurge(mailing);
      return;
    }

    mailing = daoFactoryRequest.get().getMailingDao().procura(mailing.getId());
    TelefoneDao telefoneDao = daoFactoryRequest.get().getTelefoneDao();
    BufferedReader bw = new BufferedReader(new InputStreamReader(file.getFile()));
    try {
      while (bw.ready()) {
        String linha = bw.readLine();
        qtdLinhas++;
        long chaveTelefone = 0;
        String ddd = EMPTY, tel = EMPTY;
        String[] tokens = splitByWholeSeparator(linha, ";");
        for (int i = 0; i < tokens.length; i++) {
          switch (i) {
            case 0:
              try {
                chaveTelefone = Integer.parseInt(tokens[i]);
              } catch (NumberFormatException e) {
              }
              break;
            case 1:
              ddd = tokens[i];
              break;
            case 2:
              tel = tokens[i];
              break;
          }
        }
        if (chaveTelefone <= 0 || ddd.isEmpty() || tel.isEmpty()) {
          qtdErros++;
          continue;
        }

        Telefone telefone = telefoneDao.procura(mailing, chaveTelefone, ddd, tel);
        if (telefone == null) {
          qtdNaoEncontrados++;
          continue;
        }

        telefone.setUtil(false);
        telefoneDao.atualiza(telefone);
        qtdExpurgados++;
      }
      LinkedList<String> errors = new LinkedList<String>();
      errors.add("Total no arquivo: " + qtdLinhas);
      errors.add("Total Expurgados: " + qtdExpurgados);
      errors.add("Total nao encontrados: " + qtdNaoEncontrados);
      errors.add("Total Erros: " + qtdErros);
      result.include("errors", errors);
      mailing.getCampanha().setLimpaMemoria(true);
      daoFactoryRequest.get().getCampanhaDao().atualiza(mailing.getCampanha());
      result.forwardTo(MailingController.class).formularioPurge(mailing);
    } finally {
      bw.close();
    }
  }

  @LogAcesso
  @Post
  public void limpaTelefoneAtual(Mailing mailing) {
    mailing = daoFactoryRequest.get().getMailingDao().procura(mailing.getId());
    daoFactoryRequest.get().getMailingDao().limpaTelefoneAtual(mailing);
    LinkedList<String> messages = new LinkedList<String>();
    messages.add("Sucesso!");
    result.include("messages", messages);
    result.use(Results.logic()).redirectTo(MailingController.class).list(mailing.getCampanha());
  }

}
