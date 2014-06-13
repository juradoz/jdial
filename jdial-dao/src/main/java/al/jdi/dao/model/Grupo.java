package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"codigo"})}, indexes = {@Index(
    name = "IX_grupo_codigo", columnList = "codigo")})
public class Grupo implements DaoObject {
  @Id
  @GeneratedValue
  private Long idGrupo;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @Column(nullable = false)
  private String codigo;

  private String descricao;

  private boolean visivelOperador = true;

  private boolean semAgentes = false;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Grupo other = (Grupo) obj;
    return new EqualsBuilder().append(idGrupo, other.idGrupo).isEquals();
  }

  public String getCodigo() {
    return codigo;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public Long getIdGrupo() {
    return idGrupo;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(idGrupo).toHashCode();
  }

  public boolean isSemAgentes() {
    return semAgentes;
  }

  public boolean isVisivelOperador() {
    return visivelOperador;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setIdGrupo(Long idGrupo) {
    this.idGrupo = idGrupo;
  }

  public void setSemAgentes(boolean semAgentes) {
    this.semAgentes = semAgentes;
  }

  public void setVisivelOperador(boolean visivelOperador) {
    this.visivelOperador = visivelOperador;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("codigo", codigo)
        .toString();
  }
}
