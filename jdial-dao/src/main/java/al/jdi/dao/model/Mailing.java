package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"idCampanha", "nome"})})
public class Mailing implements DaoObject {
  @Id
  @GeneratedValue
  private Long idMailing;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne
  @JoinColumn(name = "idCampanha", nullable = false)
  private Campanha campanha;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private boolean ativo;

  @Column
  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
  private DateTime dataInicial;

  @Column
  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
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
    return new EqualsBuilder().append(idMailing, other.idMailing).isEquals();
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

  public Long getIdMailing() {
    return idMailing;
  }

  public String getNome() {
    return nome;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(idMailing).toHashCode();
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

  public void setIdMailing(Long idMailing) {
    this.idMailing = idMailing;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("nome", nome)
        .toString();
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
