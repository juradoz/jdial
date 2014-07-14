package al.jdi.dao.beans;

import java.util.List;

import al.jdi.dao.model.Campanha;

public interface CampanhaDao extends Dao<Campanha> {
  List<Campanha> listaAtivas();
}
