package net.danieljurado.dialer.estoque;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import net.danieljurado.dialer.configuracoes.Configuracoes;
import net.danieljurado.dialer.devolveregistro.DevolveRegistro;
import net.danieljurado.dialer.estoque.EstoqueModule.Agendados;
import net.danieljurado.dialer.filter.TelefoneFilter;
import net.danieljurado.dialer.modelo.Discavel;
import net.danieljurado.dialer.modelo.ModeloModule.DiscavelTsa;
import net.danieljurado.dialer.modelo.Providencia;
import net.danieljurado.dialer.modelo.Providencia.Codigo;
import net.danieljurado.dialer.tratadorespecificocliente.TratadorEspecificoCliente;

import org.jdial.common.Engine;
import org.joda.time.Period;

import al.jdi.dao.beans.DaoFactory;

@Agendados
class EstoqueImplAgendados extends EstoqueImpl {

  @Inject
  EstoqueImplAgendados(Configuracoes configuracoes, Provider<DaoFactory> daoFactoryProvider,
      DevolveRegistro devolveRegistro, TratadorEspecificoCliente tratadorEspecificoCliente,
      @DiscavelTsa Discavel.Factory discavelFactory, Engine.Factory engineFactory,
      Collection<Registro> estoque, @Agendados ExtraidorClientes extraidorClientes,
      @Agendados Estoque estoqueAgendados, @Agendados Period intervaloMonitoracao,
      Map<Codigo, Providencia> providencias, TelefoneFilter telefoneFilter) {
    super(configuracoes, daoFactoryProvider, devolveRegistro, tratadorEspecificoCliente,
        discavelFactory, engineFactory, estoque, extraidorClientes, estoqueAgendados,
        intervaloMonitoracao, providencias, telefoneFilter);
  }

}
