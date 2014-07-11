package net.danieljurado.dialer.modelo;

import net.danieljurado.dialer.modelo.ModeloModule.ProvidenciaInvalidaAtualEProximoTelefone;
import net.danieljurado.dialer.modelo.ModeloModule.ProvidenciaMantemAtual;
import net.danieljurado.dialer.modelo.ModeloModule.ProvidenciaProximoTelefone;

import org.hibernate.cfg.annotations.MapBinder;

class ProvidenciaModule {

  protected void configure() {
    bind(Providencia.class).annotatedWith(ProvidenciaMantemAtual.class).to(MantemAtual.class);
    bind(Providencia.class).annotatedWith(ProvidenciaProximoTelefone.class).to(
        ProximoTelefone.class);
    bind(Providencia.class).annotatedWith(ProvidenciaInvalidaAtualEProximoTelefone.class).to(
        InvalidaAtualEProximoTelefone.class);

    MapBinder<Providencia.Codigo, Providencia> providencias =
        MapBinder.newMapBinder(binder(), Providencia.Codigo.class, Providencia.class);
    providencias.addBinding(Providencia.Codigo.MANTEM_ATUAL).to(MantemAtual.class);
    providencias.addBinding(Providencia.Codigo.PROXIMO_TELEFONE).to(ProximoTelefone.class);
    providencias.addBinding(Providencia.Codigo.INVALIDA_ATUAL_E_PROXIMO_TELEFONE).to(
        InvalidaAtualEProximoTelefone.class);
  }

}
