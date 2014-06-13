package al.jdi.dao;

import java.util.List;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.ResultadoLigacao;

public interface ResultadoLigacaoDao extends Dao<ResultadoLigacao> {

  ResultadoLigacao procura(Campanha campanha, String nome);

  ResultadoLigacao procura(int codigo, Campanha campanha);

  List<ResultadoLigacao> listaTudo(Campanha campanha);

}
