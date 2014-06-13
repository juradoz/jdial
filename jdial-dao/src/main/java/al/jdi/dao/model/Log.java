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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(indexes = {@Index(name = "IX_log_level_campanha_criacao",
    columnList = "level,idCampanha,criacao")})
public class Log implements DaoObject {
  @Id
  @GeneratedValue
  private long idLog;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne
  @JoinColumn(name = "idCampanha", nullable = false)
  private Campanha campanha;

  @Column(nullable = false)
  private int level;

  private String message;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Log other = (Log) obj;
    return new EqualsBuilder().append(idLog, other.idLog).isEquals();
  }

  public Campanha getCampanha() {
    return campanha;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public long getIdLog() {
    return idLog;
  }

  public int getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(idLog).toHashCode();
  }

  public void setCampanha(Campanha campanha) {
    this.campanha = campanha;
  }

  public void setIdLog(long idLog) {
    this.idLog = idLog;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
