package al.jdi.dao.beans;

import java.util.Collection;
import java.util.List;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Mailing;

public interface MailingDao extends Dao<Mailing> {

  Collection<Mailing> listaAtivos();

  List<Mailing> listaTudo(Campanha campanha);
  
  void limpaTelefoneAtual(Mailing mailing);

}
