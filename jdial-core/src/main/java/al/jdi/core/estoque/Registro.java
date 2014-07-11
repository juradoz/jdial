package al.jdi.core.estoque;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import al.jdi.dao.model.Cliente;

class Registro {

  private static final Logger logger = LoggerFactory.getLogger(Registro.class);

  private final DateTime criacao;
  private final Cliente cliente;

  Registro(Cliente cliente) {
    this(new DateTime(), cliente);
  }

  Registro(DateTime criacao, Cliente cliente) {
    this.criacao = criacao;
    this.cliente = cliente;
    logger.debug("Iniciando {}", this);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Registro other = (Registro) obj;
    return new EqualsBuilder().append(cliente, other.cliente).isEquals();
  }

  Cliente getCliente() {
    return cliente;
  }

  DateTime getCriacao() {
    return criacao;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(cliente).toHashCode();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
