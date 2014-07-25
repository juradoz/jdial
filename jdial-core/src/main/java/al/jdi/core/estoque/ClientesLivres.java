package al.jdi.core.estoque;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import al.jdi.core.estoque.EstoqueModule.Livres;
import al.jdi.core.tenant.Tenant;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;

@Livres
class ClientesLivres implements ExtraidorClientes {

  private static final Logger logger = getLogger(ClientesLivres.class);

  private final TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory;

  @Inject
  ClientesLivres(TratadorEspecificoCliente.Factory tratadorEspecificoClienteFactory) {
    this.tratadorEspecificoClienteFactory = tratadorEspecificoClienteFactory;
    logger.debug("Iniciando {}", this);
  }

  @Override
  public Collection<Cliente> extrai(Tenant tenant, DaoFactory daoFactory, int quantidade) {
    return tratadorEspecificoClienteFactory
        .create(tenant, daoFactory)
        .obtemClienteDao()
        .obtemLivres(quantidade,
            daoFactory.getCampanhaDao().procura(tenant.getConfiguracoes().getNomeCampanha()),
            tenant.getConfiguracoes().getNomeBaseDados(), tenant.getConfiguracoes().getNomeBase(),
            tenant.getConfiguracoes().getOperador());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
