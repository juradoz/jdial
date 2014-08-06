package al.jdi.core.modelo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import al.jdi.dao.model.Telefone;

public class Ligacao {

  public static class Builder {
    private final Discavel discavel;
    private final DateTime criacao;
    private int motivoFinalizacao;
    private DateTime inicio;
    private DateTime termino;

    public Builder(Discavel discavel) {
      this(discavel, new DateTime());
    }

    public Builder(Discavel discavel, DateTime criacao) {
      this.discavel = discavel;
      this.criacao = criacao;
    }

    public Ligacao build() {
      return new Ligacao(discavel, motivoFinalizacao, criacao, inicio, termino);
    }

    public Builder setInicio(DateTime inicio) {
      this.inicio = inicio;
      return this;
    }

    public Builder setMotivoFinalizacao(int motivoFinalizacao) {
      this.motivoFinalizacao = motivoFinalizacao;
      return this;
    }

    public Builder setTermino(DateTime termino) {
      this.termino = termino;
      return this;
    }

  }

  private final DateTime inicioLocal = new DateTime();
  private DateTime inicioChamadaLocal;
  private final Discavel discavel;
  private int motivoFinalizacao;
  private final DateTime criacao;
  private DateTime inicio;
  private DateTime atendimento;
  private DateTime termino;
  private String agente;
  private boolean foiPraFila;
  private boolean inutilizaComMotivoDiferenciado = false;
  private Telefone telefoneOriginal;

  public Telefone getTelefoneOriginal() {
    return telefoneOriginal;
  }

  public void setTelefoneOriginal(Telefone telefoneOriginal) {
    this.telefoneOriginal = telefoneOriginal;
  }

  private Ligacao(Discavel discavel, int motivoFinalizacao, DateTime criacao, DateTime inicio,
      DateTime termino) {
    this.discavel = discavel;
    this.motivoFinalizacao = motivoFinalizacao;
    this.criacao = criacao;
    this.inicio = inicio;
    this.termino = termino;
  }

  public DateTime getAtendimento() {
    return atendimento;
  }

  public DateTime getCriacao() {
    return criacao;
  }

  public Discavel getDiscavel() {
    return discavel;
  }

  public DateTime getInicio() {
    return inicio;
  }

  public int getMotivoFinalizacao() {
    return motivoFinalizacao;
  }

  public DateTime getTermino() {
    return termino;
  }

  public boolean isAtendida() {
    return atendimento != null;
  }

  public boolean isNoAgente() {
    return agente != null;
  }

  public void setAtendimento(DateTime atendimento) {
    this.atendimento = atendimento;
  }

  void setInicio(DateTime inicio) {
    this.inicio = inicio;
  }

  public void setMotivoFinalizacao(int motivoFinalizacao) {
    this.motivoFinalizacao = motivoFinalizacao;
  }

  public void setTermino(DateTime termino) {
    this.termino = termino;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  public String getAgente() {
    return agente;
  }

  public void setAgente(String agente) {
    this.agente = agente;
  }

  public boolean isFoiPraFila() {
    return foiPraFila;
  }

  public void setFoiPraFila(boolean foiPraFila) {
    this.foiPraFila = foiPraFila;
  }

  public boolean isInutilizaComMotivoDiferenciado() {
    return inutilizaComMotivoDiferenciado;
  }

  public void setInutilizaComMotivoDiferenciado(boolean inutilizaComMotivoDiferenciado) {
    this.inutilizaComMotivoDiferenciado = inutilizaComMotivoDiferenciado;
  }

  public DateTime getInicioChamadaLocal() {
    return inicioChamadaLocal;
  }

  public void setInicioChamadaLocal(DateTime inicioChamadaLocal) {
    this.inicioChamadaLocal = inicioChamadaLocal;
  }

  public DateTime getInicioLocal() {
    return inicioLocal;
  }

}
