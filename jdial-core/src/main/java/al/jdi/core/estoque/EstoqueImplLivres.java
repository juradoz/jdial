package al.jdi.core.estoque;

import java.util.Collection;
import java.util.Map;

import javax.inject.Provider;

import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.devolveregistro.DevolveRegistro;
import al.jdi.core.estoque.EstoqueModule.Livres;
import al.jdi.core.filter.TelefoneFilter;
import al.jdi.core.modelo.Discavel;
import al.jdi.core.modelo.ModeloModule.DiscavelTsa;
import al.jdi.core.modelo.Providencia;
import al.jdi.core.modelo.Providencia.Codigo;
import al.jdi.core.tratadorespecificocliente.TratadorEspecificoCliente;
import al.jdi.dao.beans.DaoFactory;

class EstoqueImplLivres extends EstoqueImpl {

  EstoqueImplLivres(Logger logger, Configuracoes configuracoes,
      Provider<DaoFactory> daoFactoryProvider, DevolveRegistro devolveRegistro,
      TratadorEspecificoCliente tratadorEspecificoCliente,
      @DiscavelTsa Discavel.Factory discavelFactory, Engine.Factory engineFactory,
      Collection<Registro> estoque, @Livres ExtraidorClientes extraidorClientes,
      @Livres Period intervaloMonitoracao, Map<Codigo, Providencia> providencias,
      TelefoneFilter telefoneFilter) {
    super(logger, configuracoes, daoFactoryProvider, devolveRegistro, tratadorEspecificoCliente,
        discavelFactory, engineFactory, estoque, extraidorClientes, intervaloMonitoracao,
        providencias, telefoneFilter);
  }

}
