package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"idCliente"})}, indexes = {
    @Index(name = "IX_agendamento_idCliente_agendamento", columnList = "idCliente, agendamento"),
    @Index(name = "IX_agendamento_agendamento", columnList = "agendamento")})
public class Agendamento implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idAgendamento")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne
  @JoinColumn(name = "idCliente", nullable = false)
  private Cliente cliente;

  @Column(nullable = false)
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime agendamento;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Agendamento other = (Agendamento) obj;
    return new EqualsBuilder().append(agendamento, other.agendamento).isEquals();
  }

  public DateTime getAgendamento() {
    return agendamento;
  }

  public Cliente getCliente() {
    return cliente;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public Long getId() {
    return id;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(agendamento).toHashCode();
  }

  public void setAgendamento(DateTime agendamento) {
    this.agendamento = agendamento;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("agendamento",
        agendamento).toString();
  }
}
