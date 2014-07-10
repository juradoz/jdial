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
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class HistoricoCliente implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idHistoricoCliente")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne
  @JoinColumn(name = "idCliente", nullable = false)
  private Cliente cliente;

  @ManyToOne
  @JoinColumn(name = "idEstadoCliente", nullable = false)
  private EstadoCliente estadoCliente;

  @ManyToOne
  @JoinColumn(name = "idAgente", nullable = true)
  private Agente agente;

  @ManyToOne
  @JoinColumn(name = "idMotivoFinalizacao", nullable = true)
  private MotivoFinalizacao motivoFinalizacao;

  @Column(nullable = true)
  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
  private DateTime agendamento;

  private String descricao;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HistoricoCliente other = (HistoricoCliente) obj;
    return new EqualsBuilder().append(id, other.id).isEquals();
  }

  public DateTime getAgendamento() {
    return agendamento;
  }

  public Agente getAgente() {
    return agente;
  }

  public Cliente getCliente() {
    return cliente;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public EstadoCliente getEstadoCliente() {
    return estadoCliente;
  }

  public Long getId() {
    return id;
  }

  public MotivoFinalizacao getMotivoFinalizacao() {
    return motivoFinalizacao;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  public void setAgendamento(DateTime agendamento) {
    this.agendamento = agendamento;
  }

  public void setAgente(Agente agente) {
    this.agente = agente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setEstadoCliente(EstadoCliente estadoCliente) {
    this.estadoCliente = estadoCliente;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setMotivoFinalizacao(MotivoFinalizacao motivoFinalizacao) {
    this.motivoFinalizacao = motivoFinalizacao;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
