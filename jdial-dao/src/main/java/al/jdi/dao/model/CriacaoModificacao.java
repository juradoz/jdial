package al.jdi.dao.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

public class CriacaoModificacao {
  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
  private final DateTime criacao = new DateTime();
  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
  private DateTime modificacao = new DateTime();

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CriacaoModificacao other = (CriacaoModificacao) obj;
    return new EqualsBuilder().append(criacao, other.criacao)
        .append(modificacao, other.modificacao).isEquals();
  }

  public DateTime getCriacao() {
    return criacao;
  }

  public DateTime getModificacao() {
    return modificacao;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(criacao).append(modificacao).toHashCode();
  }

  public void setModificacao(DateTime modificacao) {
    this.modificacao = modificacao;
  }
}
