package net.danieljurado.dialer.estoque;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.devolveregistro.DevolveRegistro;
import net.danieljurado.dialer.estoque.EstoqueModule.Livres;
import net.danieljurado.dialer.filter.TelefoneFilter;
import net.danieljurado.dialer.modelo.Discavel;
import net.danieljurado.dialer.modelo.ModeloModule.DiscavelTsa;
import net.danieljurado.dialer.modelo.Providencia;
import net.danieljurado.dialer.modelo.Providencia.Codigo;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.jdial.common.Engine;
import org.joda.time.Period;

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
