package al.jdi.dao.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})}, indexes = {@Index(
    name = "IX_usuario_login", columnList = "login")})
public class Usuario implements DaoObject {
  @Transient
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Id
  @GeneratedValue
  private Long idUsuario;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @Column(nullable = false)
  private String login;

  @Column(nullable = false)
  private String senha;

  private String nome;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Usuario other = (Usuario) obj;
    return new EqualsBuilder().append(idUsuario, other.idUsuario).isEquals();
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public Long getIdUsuario() {
    return idUsuario;
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(idUsuario).toHashCode();
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setSenha(String senha) {
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(senha.getBytes());

      this.senha = Base64.encodeBase64String(digest.digest());
    } catch (NoSuchAlgorithmException e) {
      logger.error(e.getMessage(), e);
      this.senha = senha;
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("login", login)
        .toString();
  }
}
