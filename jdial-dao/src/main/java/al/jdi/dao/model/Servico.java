package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.dao.beans.Dao.CampoBusca;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"nome"})})
public class Servico implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idServico")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @CampoBusca
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
    return new EqualsBuilder().append(id, other.id).isEquals();
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

  public String getNome() {
    return nome;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  public boolean isMonitoravelQrf() {
    return monitoravelQrf;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setMonitoravelQrf(boolean monitoravelQrf) {
    this.monitoravelQrf = monitoravelQrf;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id)
        .append("nome", nome).toString();
  }
}
