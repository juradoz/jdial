package al.jdi.dao.model;

import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cascade;

import al.jdi.dao.beans.Dao.CampoBusca;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"nome"})}, indexes = {@Index(
    name = "IX_campanha_nome", columnList = "nome")})
public class Campanha implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idCampanha")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @CampoBusca
  @Column(nullable = false)
  private String nome;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "campanha")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<Mailing> mailing = new LinkedList<Mailing>();

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "campanha")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<Filtro> filtro = new LinkedList<Filtro>();

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "campanha")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<Definicao> definicao = new LinkedList<Definicao>();

  @Column(nullable = false)
  private boolean filtroAtivo = false;

  @ManyToOne
  @JoinColumn(name = "idGrupo")
  private Grupo grupo;

  @ManyToOne
  @JoinColumn(name = "idRota", nullable = false)
  private Rota rota;

  @ManyToOne
  @JoinColumn(name = "idServico", nullable = false)
  private Servico servico;

  private String descricao;

  private boolean limpaMemoria = false;
  
  private boolean ativa = false;

  public boolean isAtiva() {
    return ativa;
  }

  public void setAtiva(boolean ativa) {
    this.ativa = ativa;
  }

  public Campanha() {}

  public Campanha(Long idCampanha) {
    this.id = idCampanha;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Campanha other = (Campanha) obj;
    return new EqualsBuilder().append(id, other.id).isEquals();
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public Collection<Definicao> getDefinicao() {
    return definicao;
  }

  public String getDescricao() {
    return descricao;
  }

  public Collection<Filtro> getFiltro() {
    return filtro;
  }

  public Grupo getGrupo() {
    return grupo;
  }

  public Long getId() {
    return id;
  }

  public Collection<Mailing> getMailing() {
    return mailing;
  }

  public String getNome() {
    return nome;
  }

  public Rota getRota() {
    return rota;
  }

  public Servico getServico() {
    return servico;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  public boolean isFiltroAtivo() {
    return filtroAtivo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setFiltroAtivo(boolean filtroAtivo) {
    this.filtroAtivo = filtroAtivo;
  }

  public void setGrupo(Grupo grupo) {
    this.grupo = grupo;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setRota(Rota rota) {
    this.rota = rota;
  }

  public void setServico(Servico servico) {
    this.servico = servico;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id)
        .append("nome", nome).toString();
  }

  public boolean isLimpaMemoria() {
    return limpaMemoria;
  }

  public void setLimpaMemoria(boolean limpaMemoria) {
    this.limpaMemoria = limpaMemoria;
  }

}
