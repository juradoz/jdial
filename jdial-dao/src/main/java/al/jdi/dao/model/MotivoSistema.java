package al.jdi.dao.model;

public enum MotivoSistema {
  ATENDIDA(-1), NAO_UTILIZADO(-2), CONGESTIONAMENTO(-3), LEI_NAO_PERTURBE(-4), CANCELADO_PELO_DISCADOR(
      -5), SEM_TELEFONES(-6), SOMENTE_CELULARES(-7), SEM_PROXIMO_TELEFONE(-8), NAO_PODE_IR_PROXIMO_TELEFONE(
      -9);

  private final int codigo;

  private MotivoSistema(int codigo) {
    this.codigo = codigo;
  }

  public int getCodigo() {
    return codigo;
  }
}
