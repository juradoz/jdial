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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cascade;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"nome"})}, indexes = {@Index(
    name = "IX_campanha_nome", columnList = "nome")})
public class Campanha implements DaoObject {
  @Id
  @GeneratedValue
  private Long idCampanha;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @Column(nullable = false)
  private String nome;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "campanha")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<Mailing> mailing = new LinkedList<Mailing>();

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "campanha")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<Definicao> definicao = new LinkedList<Definicao>();

  @Column(nullable = false)
  private boolean filtroAtivo = false;

  @Column(nullable = false)
  private int codigoFiltro = 0;

  @ManyToOne
  @JoinColumn(name = "idGrupo")
  private Grupo grupo;

  @ManyToOne
  @JoinColumn(name = "idServico", nullable = false)
  private Servico servico;

  private String descricao;

  public Campanha() {}

  public Campanha(Long idCampanha) {
    this.idCampanha = idCampanha;
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
    return new EqualsBuilder().append(idCampanha, other.idCampanha).isEquals();
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

  public Grupo getGrupo() {
    return grupo;
  }

  public Long getIdCampanha() {
    return idCampanha;
  }

  public Collection<Mailing> getMailing() {
    return mailing;
  }

  public String getNome() {
    return nome;
  }

  public Servico getServico() {
    return servico;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(idCampanha).toHashCode();
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

  public void setIdCampanha(Long idCampanha) {
    this.idCampanha = idCampanha;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setServico(Servico servico) {
    this.servico = servico;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("nome", nome)
        .toString();
  }

  public int getCodigoFiltro() {
    return codigoFiltro;
  }

  public void setCodigoFiltro(int codigoFiltro) {
    this.codigoFiltro = codigoFiltro;
  }

}
