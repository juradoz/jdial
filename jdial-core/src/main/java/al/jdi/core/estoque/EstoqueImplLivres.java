package al.jdi.core.estoque;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jdial.common.Engine;
import org.joda.time.Period;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.devolveregistro.DevolveRegistro;
import al.jdi.core.estoque.EstoqueModule.Livres;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.Providencia;
import al.jdi.core.modelo.ModeloModule.DiscavelTsa;
import al.jdi.core.modelo.Providencia.Codigo;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;

@Livres
class EstoqueImplLivres extends EstoqueImpl {

  @Inject
  EstoqueImplLivres(Configuracoes configuracoes, Provider<DaoFactory> daoFactoryProvider,
      DevolveRegistro devolveRegistro, TratadorEspecificoCliente tratadorEspecificoCliente,
      @DiscavelTsa Discavel.Factory discavelFactory, Engine.Factory engineFactory,
      Collection<Registro> estoque, @Livres ExtraidorClientes extraidorClientes,
      @Livres Estoque estoqueAgendados, @Livres Period intervaloMonitoracao,
      Map<Codigo, Providencia> providencias, TelefoneFilter telefoneFilter) {
    super(configuracoes, daoFactoryProvider, devolveRegistro, tratadorEspecificoCliente,
        discavelFactory, engineFactory, estoque, extraidorClientes, estoqueAgendados,
        intervaloMonitoracao, providencias, telefoneFilter);
  }

}
