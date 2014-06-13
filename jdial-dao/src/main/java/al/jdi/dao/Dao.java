package al.jdi.dao;

import java.util.List;

import al.jdi.dao.model.DaoObject;

public interface Dao<T extends DaoObject> {

  void adiciona(T t);

  void atualiza(T t);

  List<T> listaTudo();

  T procura(Long id);

  void remove(T u);

}
