package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mindrot.jbcrypt.BCrypt;

import al.jdi.dao.beans.Dao.CampoBusca;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})}, indexes = {@Index(
    name = "IX_usuario_login", columnList = "login")})
public class Usuario implements DaoObject {

  public enum TipoPerfil {
    SUPERVISOR, ADMINISTRADOR;
  }

  @Id
  @GeneratedValue
  @Column(name = "idUsuario")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @CampoBusca
  @Column(nullable = false)
  private String login;

  @Column(nullable = false)
  private String senha;

  private String nome;

  @Column(name = "tipoPerfil", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private TipoPerfil tipoPerfil;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Usuario other = (Usuario) obj;
    return new EqualsBuilder().append(id, other.id).isEquals();
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public Long getId() {
    return id;
  }

  public String getLogin() {
    return login;
  }

  public String getNome() {
    return nome;
  }

  public String getSenha() {
    return senha;
  }

  public TipoPerfil getTipoPerfil() {
    return tipoPerfil;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public void setTipoPerfil(TipoPerfil tipoPerfil) {
    this.tipoPerfil = tipoPerfil;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id)
        .append("login", login).toString();
  }

  public String criptografaSenha(String senha) {
    return BCrypt.hashpw(senha, BCrypt.gensalt());
  }

  public boolean verificaSenha(String senha) {
    return BCrypt.checkpw(senha, this.senha);
  }
}
