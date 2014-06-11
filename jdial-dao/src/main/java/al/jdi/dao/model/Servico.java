package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"nome"})})
public class Servico implements DaoObject {
  @Id
  @GeneratedValue
  private Long idServico;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @Column(nullable = false)
  private String nome;

  private String descricao;

  @Column(nullable = false)
  private boolean monitoravelQrf = false;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Servico other = (Servico) obj;
    return new EqualsBuilder().append(idServico, other.idServico).isEquals();
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public Long getIdServico() {
    return idServico;
  }

  public String getNome() {
    return nome;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(idServico).toHashCode();
  }

  public boolean isMonitoravelQrf() {
    return monitoravelQrf;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setIdServico(Long idServico) {
    this.idServico = idServico;
  }

  public void setMonitoravelQrf(boolean monitoravelQrf) {
    this.monitoravelQrf = monitoravelQrf;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("nome", nome)
        .toString();
  }
}
