package al.jdi.dao.beans;

import java.util.Collection;

import org.joda.time.DateTime;

import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.HistoricoLigacao;
import al.jdi.dao.model.ResultadoLigacao;

public interface HistoricoLigacaoDao extends Dao<HistoricoLigacao> {

  Collection<HistoricoLigacao> procura(Cliente cliente);

  Collection<HistoricoLigacao> procura(Cliente cliente, ResultadoLigacao resultadoLigacao);

  Collection<HistoricoLigacao> procura(Cliente cliente, ResultadoLigacao resultadoLigacao,
      DateTime desde);

}
