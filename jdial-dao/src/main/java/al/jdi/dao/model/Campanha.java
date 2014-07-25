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

  @Column(nullable = false)
  private boolean filtroAtivo = false;

  @Column(nullable = false)
  private int codigoFiltro = 0;

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

  public Campanha() {}

  private Campanha(Long id, CriacaoModificacao criacaoModificacao, String nome,
      boolean filtroAtivo, int codigoFiltro, Grupo grupo, Rota rota, Servico servico,
      String descricao, boolean limpaMemoria, boolean ativa) {
    super();
    this.id = id;
    this.criacaoModificacao = criacaoModificacao;
    this.nome = nome;
    this.filtroAtivo = filtroAtivo;
    this.codigoFiltro = codigoFiltro;
    this.grupo = grupo;
    this.rota = rota;
    this.servico = servico;
    this.descricao = descricao;
    this.limpaMemoria = limpaMemoria;
    this.ativa = ativa;
  }

  public boolean isAtiva() {
    return ativa;
  }

  public void setAtiva(boolean ativa) {
    this.ativa = ativa;
  }


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

  public String getDescricao() {
    return descricao;
  }

  public Grupo getGrupo() {
    return grupo;
  }

  public Long getId() {
    return id;
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

  public int getCodigoFiltro() {
    return codigoFiltro;
  }

  public void setCodigoFiltro(int codigoFiltro) {
    this.codigoFiltro = codigoFiltro;
  }

  public Campanha clone() {
    return new Campanha(id, criacaoModificacao, nome, filtroAtivo, codigoFiltro, grupo, rota,
        servico, descricao, limpaMemoria, ativa);
  }

}
