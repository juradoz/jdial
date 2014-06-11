package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class AreaArea implements DaoObject {

  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @Column(nullable = false)
  private String siglaUf;

  @Column(nullable = false)
  private String siglaCnl;

  @Column(nullable = false)
  private String codigoCnl;

  @Column(nullable = false)
  private String nomeLocalidade;

  @Column(nullable = false)
  private String nomeMunicipio;

  @Column(nullable = false)
  private String codAreaTarifacao;

  @Column(nullable = false)
  private String ddd;

  @Column(nullable = false)
  private String prefixo;

  @Column(nullable = false)
  private String prestadora;

  @Column(nullable = false)
  private String faixaInicial;

  @Column(nullable = false)
  private String faixaFinal;

  @Column(nullable = false)
  private String latitude;

  @Column(nullable = false)
  private String hemisferio;

  @Column(nullable = false)
  private String longitude;

  @Column(nullable = false)
  private String siglaCnlAreaLocal;

  @Column(nullable = false)
  private boolean conurbada = false;

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AreaArea other = (AreaArea) obj;
    return new EqualsBuilder().append(id, other.id).isEquals();
  }

}
