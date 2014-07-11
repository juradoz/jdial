package al.jdi.dao.beans;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import al.jdi.dao.model.DaoObject;

public interface Dao<T extends DaoObject> {

  @Retention(RUNTIME)
  @Target({FIELD})
  public @interface CampoBusca {
  }

  void adiciona(T t);

  void atualiza(T t);

  List<T> listaTudo();

  T procura(Long id);

  T procura(String s);

  void remove(T u);

}
