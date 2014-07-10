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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import al.jdi.dao.beans.Dao.CampoBusca;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"nome"})})
public class EstadoCliente implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idEstadoCliente")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @CampoBusca
  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private boolean disponivelDiscagem;

  private String descricao;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof EstadoCliente))
      return false;
    EstadoCliente other = (EstadoCliente) obj;
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

  public boolean isDisponivelDiscagem() {
    return disponivelDiscagem;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setDisponivelDiscagem(boolean disponivelDiscagem) {
    this.disponivelDiscagem = disponivelDiscagem;
  }

  public void setId(Long id) {
    this.id = id;
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
