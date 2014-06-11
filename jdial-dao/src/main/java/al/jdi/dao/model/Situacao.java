package al.jdi.dao.model;

public enum Situacao {
  TENTATIVA(1), FINALIZACAO(2);

  private int codigo;

  private Situacao(int codigo) {
    this.codigo = codigo;
  }

  public int getCodigo() {
    return codigo;
  }
}
