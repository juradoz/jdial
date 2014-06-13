package al.jdi.dao;

import java.util.Collection;
import java.util.List;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Mailing;

public interface MailingDao extends Dao<Mailing> {

  Collection<Mailing> listaAtivos();

  List<Mailing> listaTudo(Campanha campanha);

  Mailing procura(Campanha campanha, String nome);

}
