package al.jdi.dao;

import java.util.List;

import al.jdi.dao.model.Servico;

public interface ServicoDao extends Dao<Servico> {

  List<Servico> monitoraveisQrf();

  Servico procura(String nome);

}
