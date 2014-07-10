package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResultadoLigacao implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idResultadoLigacao")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @Column(nullable = false)
  private int codigo;

  @Column(nullable = false)
  private String nome;

  @ManyToOne
  @JoinColumn(name = "idCampanha", nullable = false)
  private Campanha campanha;

  @Column(nullable = false)
  private int motivo = 0;

  @Column(nullable = false)
  private int motivoFinalizacaoPorQuantidadeResultado = 0;

  private String descricao;

  @Column(nullable = false)
  private boolean visivelRelatorio;

  @Column(nullable = false)
  private int intervaloReagendamento;

  @Column(nullable = false)
  private boolean inutilizaTelefone;

  @Column(nullable = false)
  private boolean ciclaTelefone;

  @Column(nullable = false)
  private boolean incrementaTentativa;

  @Column(nullable = false)
  private boolean vaiParaOFimDaFila;

  @Column(nullable = false)
  private boolean insereHistorico;

  @Column(nullable = false)
  private int intervaloIndisponivel;

  @Column(nullable = false)
  private int quantidadeDesteResultadoInutilizaTelefone;

  @Column(nullable = false)
  private int intervaloDesteResultadoReagenda;

  @Column(nullable = false)
  private boolean notificaFimTentativa;

  @Column(nullable = false)
  private boolean limpaAgendamentos = false;

  @Column(nullable = false)
  private boolean incrementaQtdReag = false;

  @Column(nullable = false)
  private boolean finalizaCliente = false;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ResultadoLigacao other = (ResultadoLigacao) obj;
    return new EqualsBuilder().append(id, other.id).isEquals();
  }

  public Campanha getCampanha() {
    return campanha;
  }

  public int getCodigo() {
    return codigo;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public Long getId() {
    return id;
  }

  public int getMotivo() {
    return motivo;
  }

  public int getMotivoFinalizacaoPorQuantidadeResultado() {
    return motivoFinalizacaoPorQuantidadeResultado;
  }

  public String getNome() {
    return nome;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  public boolean isVisivelRelatorio() {
    return visivelRelatorio;
  }

  public void setCampanha(Campanha campanha) {
    this.campanha = campanha;
  }

  public void setCodigo(int codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setMotivo(int motivo) {
    this.motivo = motivo;
  }

  public void setMotivoFinalizacaoPorQuantidadeResultado(int motivoFinalizacaoPorQuantidadeResultado) {
    this.motivoFinalizacaoPorQuantidadeResultado = motivoFinalizacaoPorQuantidadeResultado;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setVisivelRelatorio(boolean visivelRelatorio) {
    this.visivelRelatorio = visivelRelatorio;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id)
        .append("nome", nome).toString();
  }

  public int getIntervaloReagendamento() {
    return intervaloReagendamento;
  }

  public void setIntervaloReagendamento(int intervaloReagendamento) {
    this.intervaloReagendamento = intervaloReagendamento;
  }

  public boolean isInutilizaTelefone() {
    return inutilizaTelefone;
  }

  public void setInutilizaTelefone(boolean inutilizaTelefone) {
    this.inutilizaTelefone = inutilizaTelefone;
  }

  public boolean isCiclaTelefone() {
    return ciclaTelefone;
  }

  public void setCiclaTelefone(boolean ciclaTelefone) {
    this.ciclaTelefone = ciclaTelefone;
  }

  public boolean isIncrementaTentativa() {
    return incrementaTentativa;
  }

  public void setIncrementaTentativa(boolean incrementaTentativa) {
    this.incrementaTentativa = incrementaTentativa;
  }

  public boolean isVaiParaOFimDaFila() {
    return vaiParaOFimDaFila;
  }

  public void setVaiParaOFimDaFila(boolean vaiParaOFimDaFila) {
    this.vaiParaOFimDaFila = vaiParaOFimDaFila;
  }

  public boolean isInsereHistorico() {
    return insereHistorico;
  }

  public void setInsereHistorico(boolean insereHistorico) {
    this.insereHistorico = insereHistorico;
  }

  public int getIntervaloIndisponivel() {
    return intervaloIndisponivel;
  }

  public void setIntervaloIndisponivel(int intervaloIndisponivel) {
    this.intervaloIndisponivel = intervaloIndisponivel;
  }

  public int getQuantidadeDesteResultadoInutilizaTelefone() {
    return quantidadeDesteResultadoInutilizaTelefone;
  }

  public void setQuantidadeDesteResultadoInutilizaTelefone(
      int quantidadeDesteResultadoInutilizaTelefone) {
    this.quantidadeDesteResultadoInutilizaTelefone = quantidadeDesteResultadoInutilizaTelefone;
  }

  public int getIntervaloDesteResultadoReagenda() {
    return intervaloDesteResultadoReagenda;
  }

  public void setIntervaloDesteResultadoReagenda(int intervaloDesteResultadoReagenda) {
    this.intervaloDesteResultadoReagenda = intervaloDesteResultadoReagenda;
  }

  public boolean isNotificaFimTentativa() {
    return notificaFimTentativa;
  }

  public void setNotificaFimTentativa(boolean notificaFimTentativa) {
    this.notificaFimTentativa = notificaFimTentativa;
  }

  public boolean isLimpaAgendamentos() {
    return limpaAgendamentos;
  }

  public void setLimpaAgendamentos(boolean limpaAgendamentos) {
    this.limpaAgendamentos = limpaAgendamentos;
  }

  public boolean isIncrementaQtdReag() {
    return incrementaQtdReag;
  }

  public void setIncrementaQtdReag(boolean incrementaQtdReag) {
    this.incrementaQtdReag = incrementaQtdReag;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isFinalizaCliente() {
    return finalizaCliente;
  }

  public void setFinalizaCliente(boolean finalizaCliente) {
    this.finalizaCliente = finalizaCliente;
  }
}
