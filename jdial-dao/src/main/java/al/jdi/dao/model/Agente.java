package al.jdi.dao.model;

import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"codigo"})})
public class Agente implements DaoObject {
  @Id
  @GeneratedValue
  private Long idAgente;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "agente")
  private final Collection<Agendamento> agendamento = new LinkedList<Agendamento>();

  @Column(nullable = false)
  private String codigo;

  private String nome;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Agente other = (Agente) obj;
    return new EqualsBuilder().append(codigo, other.codigo).append(idAgente, other.idAgente)
        .isEquals();
  }

  public Collection<Agendamento> getAgendamento() {
    return agendamento;
  }

  public String getCodigo() {
    return codigo;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public Long getIdAgente() {
    return idAgente;
  }

  public String getNome() {
    return nome;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(codigo).append(idAgente).toHashCode();
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public void setIdAgente(Long idAgente) {
    this.idAgente = idAgente;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("codigo", codigo)
        .toString();
  }
}
