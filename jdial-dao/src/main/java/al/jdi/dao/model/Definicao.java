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

@Entity
@Table(
    uniqueConstraints = {@UniqueConstraint(columnNames = {"idCampanha", "propriedade", "valor"})},
    indexes = {@Index(name = "IX_definicao_idCampanha_propriedade",
        columnList = "idCampanha, propriedade")})
public class Definicao implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idDefinicao")
  private long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne
  @JoinColumn(name = "idCampanha", nullable = false)
  private Campanha campanha;

  @Column(nullable = false)
  private String propriedade;

  @Column(nullable = false)
  private String valor;
  
  public Definicao() {
  }

  public Definicao(Campanha campanha, String propriedade, String valor) {
    this.campanha = campanha;
    this.propriedade = propriedade;
    this.valor = valor;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Definicao other = (Definicao) obj;
    return new EqualsBuilder().append(campanha, other.campanha)
        .append(propriedade, other.propriedade).append(valor, other.valor).isEquals();
  }

  public Campanha getCampanha() {
    return campanha;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public long getId() {
    return id;
  }

  public String getPropriedade() {
    return propriedade;
  }

  public String getValor() {
    return valor;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(campanha).append(propriedade).append(valor).toHashCode();
  }

  public void setCampanha(Campanha campanha) {
    this.campanha = campanha;
  }

  public void setId(long id) {
    this.id = id;
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
        .append(campanha).append("propriedade", propriedade).append("valor", valor)
        .toString();
  }
}
