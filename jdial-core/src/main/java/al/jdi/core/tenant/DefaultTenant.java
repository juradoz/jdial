package al.jdi.core.tenant;

import org.joda.time.Period;

import al.jdi.core.JDial;
import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.core.estoque.Estoque;
import al.jdi.core.estoque.ExtraidorClientes;
import al.jdi.core.gerenciadoragentes.GerenciadorAgentes;
import al.jdi.core.gerenciadorfatork.GerenciadorFatorK;
import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoes;
import al.jdi.dao.model.Campanha;

class DefaultTenant implements Tenant {
  private final Campanha campanha;
  private final Configuracoes configuracoes;
  private final Estoque estoqueLivres;
  private final Estoque estoqueAgendados;
  private final GerenciadorAgentes gerenciadorAgentes;
  private final GerenciadorLigacoes gerenciadorLigacoes;
  private final GerenciadorFatorK gerenciadorFatorK;
  private final JDial jdial;

  static class DefaultTenantFactory implements Tenant.Factory {
    @Override
    public Tenant create(Campanha campanha) {
      // TODO Auto-generated method stub
      return null;
    }
  }

  DefaultTenant(Campanha campanha, Configuracoes.Factory configuracoesFactory,
      ExtraidorClientes extraidorClientesLivres, Period intervaloMonitoracaoLivres,
      ExtraidorClientes extraidorClientesAgendados, Period intervaloMonitoracaoAgendados,
      Estoque.Factory estoqueFactory) {
    this.campanha = campanha;
    this.configuracoes = configuracoesFactory.create(campanha.getNome());
    this.estoqueLivres =
        estoqueFactory.create(configuracoes, extraidorClientesLivres, intervaloMonitoracaoLivres);
    this.estoqueAgendados =
        estoqueFactory.create(configuracoes, extraidorClientesAgendados,
            intervaloMonitoracaoAgendados);
  }

  @Override
  public Campanha getCampanha() {
    return campanha;
  }

  @Override
  public Configuracoes getConfiguracoes() {
    return configuracoes;
  }

  @Override
  public Estoque getEstoqueLivres() {
    return estoqueLivres;
  }

  @Override
  public Estoque getEstoqueAgendados() {
    return estoqueAgendados;
  }

  @Override
  public GerenciadorAgentes getGerenciadorAgentes() {
    return gerenciadorAgentes;
  }

  @Override
  public GerenciadorLigacoes getGerenciadorLigacoes() {
    return gerenciadorLigacoes;
  }

  @Override
  public GerenciadorFatorK getGerenciadorFatorK() {
    return gerenciadorFatorK;
  }

  @Override
  public JDial getJdial() {
    return jdial;
  }

  @Override
  public void start() {
    // TODO Auto-generated method stub

  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }
}
