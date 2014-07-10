package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.dao.beans.Dao.CampoBusca;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"propriedade", "valor"})},
    indexes = {@Index(name = "IX_definicaoPadrao_propriedade", columnList = "propriedade")})
public class DefinicaoPadrao implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idDefinicaoPadrao")
  private long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @CampoBusca
  @Column(nullable = false)
  private String propriedade;

  @Column(nullable = false)
  private String valor;

  @Column(nullable = false)
  private int nivelAcessoRequerido;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DefinicaoPadrao other = (DefinicaoPadrao) obj;
    return new EqualsBuilder().append(propriedade, other.propriedade).append(valor, other.valor)
        .isEquals();
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public long getId() {
    return id;
  }

  public int getNivelAcessoRequerido() {
    return nivelAcessoRequerido;
  }

  public String getPropriedade() {
    return propriedade;
  }

  public String getValor() {
    return valor;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(propriedade).append(valor).toHashCode();
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setNivelAcessoRequerido(int nivelAcessoRequerido) {
    this.nivelAcessoRequerido = nivelAcessoRequerido;
  }

  public void setPropriedade(String propriedade) {
    this.propriedade = propriedade;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id)
        .append("propriedade", propriedade).append("valor", valor).toString();
  }
}
