package al.jdi.core.modelo;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import al.jdi.core.tenant.Tenant;
import al.jdi.dao.model.Cliente;

class DiscavelTsaCRM implements Discavel {

  private final Tenant tenant;
  private Cliente cliente;

  DiscavelTsaCRM(Tenant tenant, Cliente cliente) {
    this.tenant = tenant;
    this.cliente = cliente;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DiscavelTsaCRM other = (DiscavelTsaCRM) obj;
    return new EqualsBuilder().append(cliente, other.cliente).isEquals();
  }

  @Override
  public String getChave() {
    return String.format("Preditivo#%d#%d#%d#%d#%d", cliente.getInformacaoCliente()
        .getSplitCodCampanha(), cliente.getInformacaoCliente().getSplitCodCliente(), cliente
        .getInformacaoCliente().getChave(), cliente.getTelefone().getChaveTelefone(), cliente
        .getMailing().getCampanha().isFiltroAtivo() ? cliente.getFiltro() : 0);
  }

  @Override
  public Cliente getCliente() {
    return cliente;
  }

  @Override
  public String getDdd() {
    return cliente.getTelefone().getDdd();
  }

  @Override
  public String getTelefone() {
    return cliente.getTelefone().getTelefone();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(cliente).toHashCode();
  }

  @Override
  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

  @Override
  public String getDigitoSaida() {
    return cliente.getDigitoSaida();
  }

  @Override
  public String getDestino() {
    if (isBlank(cliente.getDigitoSaida()))
      return destinoPadrao();
    return destinoCustom();
  }

  private String destinoCustom() {
    if (isChamadaLocal())
      return cliente.getDigitoSaida().concat(cliente.getTelefone().getTelefone());
    return cliente.getDigitoSaida().concat(tenant.getConfiguracoes().digitoSaidaCustomPrefixoDDD())
        .concat(cliente.getTelefone().getDdd()).concat(cliente.getTelefone().getTelefone());
  }

  private String destinoPadrao() {
    if (isChamadaLocal()) {
      if (cliente.getTelefone().isCelular()) {
        return tenant.getConfiguracoes().digitoSaidaPadraoCelularLocal()
            .concat(cliente.getTelefone().getTelefone());
      } else {
        return tenant.getConfiguracoes().digitoSaidaPadraoFixoLocal()
            .concat(cliente.getTelefone().getTelefone());
      }
    }

    if (cliente.getTelefone().isCelular()) {
      return tenant.getConfiguracoes().digitoSaidaPadraoCelularDDD()
          .concat(cliente.getTelefone().getDdd()).concat(cliente.getTelefone().getTelefone());
    } else {
      return tenant.getConfiguracoes().digitoSaidaPadraoFixoDDD()
          .concat(cliente.getTelefone().getDdd()).concat(cliente.getTelefone().getTelefone());
    }
  }

  private boolean isChamadaLocal() {
    return tenant.getConfiguracoes().dddLocalidade().equals(cliente.getTelefone().getDdd());
  }

}
