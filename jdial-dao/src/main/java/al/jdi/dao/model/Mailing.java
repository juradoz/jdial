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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import al.jdi.dao.beans.Dao.CampoBusca;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"idCampanha", "nome"})})
public class Mailing implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idMailing")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne
  @JoinColumn(name = "idCampanha", nullable = false)
  private Campanha campanha;

  @ManyToMany(mappedBy = "mailing", fetch = FetchType.EAGER)
  private final Collection<Filtro> filtro = new LinkedList<Filtro>();

  @CampoBusca
  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private boolean ativo;

  @Column
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime dataInicial;

  @Column
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime dataFinal;

  private String descricao;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Mailing other = (Mailing) obj;
    return new EqualsBuilder().append(id, other.id).isEquals();
  }

  public Campanha getCampanha() {
    return campanha;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public Collection<Filtro> getFiltro() {
    return filtro;
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

  public boolean isAtivo() {
    return ativo;
  }

  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
  }

  public void setCampanha(Campanha campanha) {
    this.campanha = campanha;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id)
        .append(nome).toString();
  }

  public DateTime getDataInicial() {
    return dataInicial;
  }

  public void setDataInicial(DateTime dataInicial) {
    this.dataInicial = dataInicial;
  }

  public DateTime getDataFinal() {
    return dataFinal;
  }

  public void setDataFinal(DateTime dataFinal) {
    this.dataFinal = dataFinal;
  }
}
