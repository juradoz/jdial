package al.jdi.dao.model;

import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.dao.beans.Dao.CampoBusca;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"codigo"})})
public class Filtro implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idFiltro")
  private long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @CampoBusca
  private String nome;

  private String descricao;

  @ManyToOne
  @JoinColumn(name = "idCampanha", nullable = false)
  private Campanha campanha;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(joinColumns = @JoinColumn(name = "idFiltro"), inverseJoinColumns = @JoinColumn(
      name = "idMailing"))
  private final Collection<Mailing> mailing = new LinkedList<Mailing>();

  @Column(nullable = false)
  private int codigo;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Filtro other = (Filtro) obj;
    return new EqualsBuilder().append(codigo, other.codigo).isEquals();
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

  public long getId() {
    return id;
  }

  public Collection<Mailing> getMailing() {
    return mailing;
  }

  public String getNome() {
    return nome;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(codigo).toHashCode();
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

  public void setId(long id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id)
        .append("campanha", campanha).append("nome", nome).append("codigo", codigo).toString();
  }
}
