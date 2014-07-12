package al.jdi.core.configuracoes;

import static java.lang.Math.max;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;

import al.jdi.common.Engine;
import al.jdi.common.Service;
import al.jdi.core.configuracoes.ConfiguracoesModule.ConfiguracoesService;
import al.jdi.core.configuracoes.ConfiguracoesModule.IntervaloAtualizacao;
import al.jdi.core.configuracoes.ConfiguracoesModule.NomeCampanha;
import al.jdi.cti.TratamentoSecretariaEletronica;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Definicao;

@Singleton
@Default
@ConfiguracoesService
class ConfiguracoesImpl implements Configuracoes, Service, Runnable {

  private static final String SISTEMA_DETECTA_CAIXA_POSTAL_PELO_TELEFONE =
      "sistema.detectaCaixaPostalPeloTelefone";
  private static final String SISTEMA_DIGITO_SAIDA_CUSTOM_PREFIXO_DDD =
      "sistema.digitoSaidaCustomPrefixoDDD";
  private static final String SISTEMA_DDD_LOCALIDADE = "sistema.dddLocalidade";
  private static final String SISTEMA_DIGITO_SAIDA_PADRAO_CELULAR_DDD =
      "sistema.digitoSaidaPadraoCelularDDD";
  private static final String SISTEMA_DIGITO_SAIDA_PADRAO_FIXO_DDD =
      "sistema.digitoSaidaPadraoFixoDDD";
  private static final String SISTEMA_DIGITO_SAIDA_PADRAO_CELULAR_LOCAL =
      "sistema.digitoSaidaPadraoCelularLocal";
  private static final String SISTEMA_DIGITO_SAIDA_PADRAO_FIXO_LOCAL =
      "sistema.digitoSaidaPadraoFixoLocal";
  private static final String SISTEMA_IS_DIGITO_SAIDA_DO_BANCO = "sistema.isDigitoSaidaDoBanco";
  private static final String SISTEMA_MAX_RINGS = "sistema.maxRings";
  private static final String SISTEMA_LOGA_OCUPACAO_AGENTE = "sistema.logaOcupacaoAgente";
  private static final String SISTEMA_PRIORIZA_CELULAR = "sistema.priorizaCelular";
  private static final String SISTEMA_UTILIZA_HORARIO_VERAO = "sistema.utilizaHorarioVerao";
  private static final String SISTEMA_UTILIZA_ESTOQUE_LIVRE_MHC = "sistema.utilizaEstoqueLivreMhc";
  private static final String SISTEMA_UTILIZA_ESTOQUE_AGP = "sistema.utilizaEstoqueAgp";
  private static final String SISTEMA_TEMPO_MAXIMO_REGISTRO_EM_MEMORIA =
      "sistema.tempoMaximoRegistroEmMemoria";
  private static final String DIALER_QTD_PARCIAIS_TEMPOS_AGENTES =
      "dialer.qtdParciaisTemposAgentes";
  private static final String SISTEMA_QTD_MAXIMA_SOLICITACOES_CHAMADAS_SIMULTANEAS =
      "sistema.qtdMaximaSolicitacoesChamadasSimultaneas";
  private static final String DIALER_QTD_MAXIMA_ERROS_ALOCACAO_INTERVALO =
      "dialer.qtdMaximaErrosAlocacaoIntervalo";
  private static final String DAC_PORT_TELECOM = "dac.portTelecom";
  private static final String SISTEMA_OPERADOR_DISCADOR = "sistema.operadorDiscador";
  private static final String SISTEMA_MINUTOS_EXPIRACAO_CHAMADAS_NAO_ATENDIDAS =
      "sistema.minutosExpiracaoChamadasNaoAtendidas";
  private static final String ESTOQUE_MINIMO_ESTOQUE_AGENDAS_PESSOAIS =
      "estoque.minimoEstoqueAgendasPessoais";
  private static final String ESTOQUE_MINIMO_ESTOQUE = "estoque.minimoEstoque";
  private static final String ESTOQUE_MAXIMO_ESTOQUE_AGENDAS_PESSOAIS =
      "estoque.maximoEstoqueAgendasPessoais";
  private static final String ESTOQUE_MAXIMO_ESTOQUE = "estoque.maximoEstoque";
  private static final String SISTEMA_LIMITE_TENTATIVAS = "sistema.limiteTentativas";
  private static final String SISTEMA_LIMIAR_TEMPO_MEDIO_LIVRE_RUIM =
      "sistema.limiarTempoMedioLivreRuim";
  private static final String SISTEMA_LIMIAR_TEMPO_MEDIO_LIVRE_BOM =
      "sistema.limiarTempoMedioLivreBom";
  private static final String SISTEMA_INTERVALO_MINIMO_NOVA_RODADA_TELEFONE_MINUTOS =
      "sistema.intervaloMinimoNovaRodadaTelefoneMinutos";
  private static final String DIALER_INTERVALO_GERENCIADOR_FATOR_K =
      "dialer.intervaloGerenciadorFatorK";
  private static final String DIALER_INTERVALO_ERRO_ALOCACAO = "dialer.intervaloErroAlocacao";
  private static final String DIALER_INTERVALO_ENTRE_RODADAS = "dialer.intervaloEntreRodadas";
  private static final String DAC_HOST_TELECOM = "dac.hostTelecom";
  private static final String SISTEMA_FINALIZA_REGISTRO_ATENDIDO =
      "sistema.finalizaRegistroAtendido";
  private static final String DIALER_FATOR_K_MINIMO = "dialer.fatorKMinimo";
  private static final String DIALER_FATOR_K_MAXIMO = "dialer.fatorKMaximo";
  private static final String SISTEMA_DISCADOR_ATIVO = "sistema.discadorAtivo";
  private static final String SISTEMA_CONSIDERA_ATENDIDAS_COMO_ATIVAS =
      "sistema.consideraAtendidasComoAtivas";
  private static final String SISTEMA_CANCELA_CHAMADAS_DESNECESSARIAS =
      "sistema.cancelaChamadasDesnecessarias";
  private static final String SISTEMA_BLOQUEIA_LIVRES_MHC = "sistema.bloqueiaLivresMhc";
  private static final String SISTEMA_BLOQUEIA_AGENDA_PESSOAL = "sistema.bloqueiaAgendaPessoal";
  private static final String SISTEMA_BLOQUEIA_AGENDA_GERAL = "sistema.bloqueiaAgendaGeral";
  private static final String SISTEMA_BLOQUEIA_CELULAR = "sistema.bloqueiaCelular";
  private static final String SISTEMA_NOME_BASE = "sistema.nomeBase";
  private static final String SISTEMA_NOME_BASE_DADOS = "sistema.nomeBaseDados";
  private static final String SISTEMA_MOTIVO_CAMPANHA = "sistema.motivoCampanha";
  private static final String SISTEMA_QTD_AGENTES_RESERVADOS = "sistema.qtdAgentesReservados";
  private static final String SISTEMA_QTD_MAXIMA_CANAIS_SIMULTANEOS =
      "sistema.qtdMaximaCanaisSimultaneos";
  private static final String SISTEMA_URA_REVERSA = "sistema.uraReversa";
  private static final String SISTEMA_MOTIVO_FINALIZACAO = "sistema.motivoFinalizacao";
  private static final String SISTEMA_LIMITE_TENTATIVAS_POR_TELEFONE =
      "sistema.limiteTentativasPorTelefone";
  private static final String SISTEMA_BLOQUEIA_DDD_POR_PERIODO = "sistema.bloqueiaDddPorPeriodo";

  private final Logger logger;

  private final String nomeCampanha;
  private final Engine.Factory engineFactory;
  private final Provider<DaoFactory> daoFactoryProvider;
  private final Period intervaloAtualizacao;
  private final Map<String, Definicao> definicoes;
  private final SistemaAtivo sistemaAtivo;

  private Engine engine;

  @Inject
  ConfiguracoesImpl(Logger logger, @NomeCampanha String nomeCampanha, Engine.Factory engineFactory,
      Provider<DaoFactory> daoFactoryProvider, @IntervaloAtualizacao Period intervaloAtualizacao,
      Map<String, Definicao> definicoes, SistemaAtivo.Factory sistemaAtivoFactory) {
    this.logger = logger;
    this.nomeCampanha = nomeCampanha;
    this.engineFactory = engineFactory;
    this.daoFactoryProvider = daoFactoryProvider;
    this.definicoes = definicoes;
    this.sistemaAtivo = sistemaAtivoFactory.create(this);
    this.intervaloAtualizacao = intervaloAtualizacao;
    run();
    logger.debug("Starting {}...", this);
  }

  @Override
  public boolean bloqueiaCelular() {
    return getBoolean(SISTEMA_BLOQUEIA_CELULAR, false);
  }

  boolean getBloqueiaAgendaGeral(DaoFactory daoFactory) {
    return getBoolean(SISTEMA_BLOQUEIA_AGENDA_GERAL, false);
  }

  boolean getBloqueiaAgendaPessoal(DaoFactory daoFactory) {
    return getBoolean(SISTEMA_BLOQUEIA_AGENDA_PESSOAL, false);
  }

  boolean getBloqueiaLivresMhc(DaoFactory daoFactory) {
    return getBoolean(SISTEMA_BLOQUEIA_LIVRES_MHC, false);
  }

  boolean getBoolean(String propriedade, boolean defaultValue) {
    Definicao definicao;
    synchronized (definicoes) {
      definicao = definicoes.get(propriedade);
    }
    if (definicao == null)
      return defaultValue;
    return Boolean.valueOf(definicao.getValor());
  }

  @Override
  public boolean getCancelaChamadasDesnecessarias() {
    return getBoolean(SISTEMA_CANCELA_CHAMADAS_DESNECESSARIAS, false);
  }

  @Override
  public boolean getConsideraAtendidasComoAtivas() {
    return getBoolean(SISTEMA_CONSIDERA_ATENDIDAS_COMO_ATIVAS, false);
  }

  @Override
  public boolean getDiscadorAtivo() {
    return getBoolean(SISTEMA_DISCADOR_ATIVO, true);
  }

  @Override
  public int getFatorKMaximo() {
    if (isUraReversa())
      return 1;
    return max(1, getInt(DIALER_FATOR_K_MAXIMO, 3));
  }

  @Override
  public int getFatorKMinimo() {
    if (isUraReversa())
      return 1;
    return max(1, getInt(DIALER_FATOR_K_MINIMO, 1));
  }

  @Override
  public boolean getFinalizaRegistroAtendido() {
    return getBoolean(SISTEMA_FINALIZA_REGISTRO_ATENDIDO, false);
  }

  String getHostTelecom(DaoFactory daoFactory) {
    return getString(DAC_HOST_TELECOM, "");
  }

  int getInt(String propriedade, int defaultValue) {
    try {
      Definicao definicao;
      synchronized (definicoes) {
        definicao = definicoes.get(propriedade);
      }
      if (definicao == null)
        return defaultValue;
      return Integer.valueOf(definicao.getValor());
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  @Override
  public Period getIntervaloEntreRodadas() {
    return Period.millis(getInt(DIALER_INTERVALO_ENTRE_RODADAS, 2000));
  }

  Period getIntervaloErroAlocacao(DaoFactory daoFactory) {
    return Period.millis(getInt(DIALER_INTERVALO_ERRO_ALOCACAO, 2000));
  }

  Period getIntervaloGerenciadorFatorK(DaoFactory daoFactory) {
    return Period.millis(max(5000, getInt(DIALER_INTERVALO_GERENCIADOR_FATOR_K, 25000)));
  }

  @Override
  public Period getIntervaloMinimoNovaRodadaTelefone() {
    return Period.minutes(Math.max(1,
        getInt(SISTEMA_INTERVALO_MINIMO_NOVA_RODADA_TELEFONE_MINUTOS, 1)));
  }

  int getLimiarTempoMedioLivreBom(DaoFactory daoFactory) {
    return getInt(SISTEMA_LIMIAR_TEMPO_MEDIO_LIVRE_BOM, 10);
  }

  int getLimiarTempoMedioLivreRuim(DaoFactory daoFactory) {
    return getInt(SISTEMA_LIMIAR_TEMPO_MEDIO_LIVRE_RUIM, 15);
  }

  @Override
  public int getLimiteTentativas() {
    return Math.max(1, getInt(SISTEMA_LIMITE_TENTATIVAS, 10));
  }

  @Override
  public int getMaximoEstoque() {
    return Math.max(1, getInt(ESTOQUE_MAXIMO_ESTOQUE, 10));
  }

  int getMaximoEstoqueAgendasPessoais(DaoFactory daoFactory) {
    return Math.max(1, getInt(ESTOQUE_MAXIMO_ESTOQUE_AGENDAS_PESSOAIS, 2));
  }

  @Override
  public int getMaxRings() {
    return Math.max(1, getInt(SISTEMA_MAX_RINGS, 5));
  }

  @Override
  public int getMinimoEstoque() {
    return Math.max(1, getInt(ESTOQUE_MINIMO_ESTOQUE, 1));
  }

  int getMinimoEstoqueAgendasPessoais(DaoFactory daoFactory) {
    return Math.max(1, getInt(ESTOQUE_MINIMO_ESTOQUE_AGENDAS_PESSOAIS, 1));
  }

  @Override
  public int getMinutosExpiracaoChamadasNaoAtendidas() {
    return Math.max(1, getInt(SISTEMA_MINUTOS_EXPIRACAO_CHAMADAS_NAO_ATENDIDAS, 1));
  }

  @Override
  public int getOperador() {
    return getInt(SISTEMA_OPERADOR_DISCADOR, 3);
  }

  int getPortTelecom(DaoFactory daoFactory) {
    return getInt(DAC_PORT_TELECOM, 22000);
  }

  int getQtdMaximaErrosAlocacaoIntervalo(DaoFactory daoFactory) {
    return Math.max(10, getInt(DIALER_QTD_MAXIMA_ERROS_ALOCACAO_INTERVALO, 10));
  }

  int getQtdMaximaSolicitacoesChamadasSimultaneas(DaoFactory daoFactory) {
    return Math.max(1, getInt(SISTEMA_QTD_MAXIMA_SOLICITACOES_CHAMADAS_SIMULTANEAS, 50));
  }

  int getQtdParciaisTemposAgentes(DaoFactory daoFactory) {
    return Math.max(5, getInt(DIALER_QTD_PARCIAIS_TEMPOS_AGENTES, 25));
  }

  @Override
  public boolean getSistemaAtivo() {
    DateTime agora = new DateTime();
    return sistemaAtivo.isAtivo(agora);
  }

  String getString(String propriedade, String defaultValue) {
    Definicao definicao;
    synchronized (definicoes) {
      definicao = definicoes.get(propriedade);
    }
    if (definicao == null)
      return defaultValue;
    return definicao.getValor();
  }

  @Override
  public int getTempoMaximoRegistroEmMemoria() {
    return Math.max(1, getInt(SISTEMA_TEMPO_MAXIMO_REGISTRO_EM_MEMORIA, 5));
  }

  @Override
  public TratamentoSecretariaEletronica getTratamentoSecretariaEletronica() {
    return TratamentoSecretariaEletronica.valueOf(getString(
        "sistema.tratamentoSecretariaEletronica",
        TratamentoSecretariaEletronica.DESLIGAR.toString()));
  }

  boolean getUtilizaEstoqueAgp(DaoFactory daoFactory) {
    return getBoolean(SISTEMA_UTILIZA_ESTOQUE_AGP, false);
  }

  boolean getUtilizaEstoqueLivreMhc(DaoFactory daoFactory) {
    return getBoolean(SISTEMA_UTILIZA_ESTOQUE_LIVRE_MHC, false);
  }

  boolean getUtilizaHorarioVerao(DaoFactory daoFactory) {
    return getBoolean(SISTEMA_UTILIZA_HORARIO_VERAO, true);
  }

  @Override
  public boolean isPriorizaCelular() {
    return getBoolean(SISTEMA_PRIORIZA_CELULAR, false);
  }

  boolean logaOcupacaoAgente(DaoFactory daoFactory) {
    return getBoolean(SISTEMA_LOGA_OCUPACAO_AGENTE, true);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

  @Override
  public void start() {
    if (engine != null)
      throw new IllegalStateException();
    engine = engineFactory.create(this, intervaloAtualizacao, true, true);
  }

  @Override
  public void stop() {
    logger.debug("Stopping {}...", this);
    if (engine == null)
      throw new IllegalStateException();
    engine.stop();
    engine = null;
  }

  @Override
  public void run() {
    DaoFactory daoFactory = daoFactoryProvider.get();
    try {
      Campanha campanha = daoFactory.getCampanhaDao().procura(nomeCampanha);
      if (campanha == null)
        throw new IllegalArgumentException("Campanha " + nomeCampanha + " nao existe!!!");
      List<Definicao> definicoes = daoFactory.getDefinicaoDao().listaTudo(campanha);
      synchronized (this.definicoes) {
        for (Definicao definicao : definicoes) {
          this.definicoes.put(definicao.getPropriedade(), definicao);
        }
      }
      logger.info("Configuracoes para {} recarregadas!", nomeCampanha);
    } finally {
      daoFactory.close();
    }
  }

  @Override
  public String getNomeCampanha() {
    return nomeCampanha;
  }

  @Override
  public boolean isDigitoSaidaDoBanco() {
    return getBoolean(SISTEMA_IS_DIGITO_SAIDA_DO_BANCO, false);
  }

  @Override
  public String digitoSaidaPadraoFixoLocal() {
    return getString(SISTEMA_DIGITO_SAIDA_PADRAO_FIXO_LOCAL, "0");
  }

  @Override
  public String digitoSaidaPadraoCelularLocal() {
    return getString(SISTEMA_DIGITO_SAIDA_PADRAO_CELULAR_LOCAL, "0");
  }

  @Override
  public String digitoSaidaPadraoFixoDDD() {
    return getString(SISTEMA_DIGITO_SAIDA_PADRAO_FIXO_DDD, "0015");
  }

  @Override
  public String digitoSaidaPadraoCelularDDD() {
    return getString(SISTEMA_DIGITO_SAIDA_PADRAO_CELULAR_DDD, "0015");
  }

  @Override
  public String dddLocalidade() {
    return getString(SISTEMA_DDD_LOCALIDADE, "11");
  }

  @Override
  public String digitoSaidaCustomPrefixoDDD() {
    return getString(SISTEMA_DIGITO_SAIDA_CUSTOM_PREFIXO_DDD, "0");
  }

  @Override
  public String getNomeBase() {
    return getString(SISTEMA_NOME_BASE, EMPTY);
  }

  @Override
  public String getNomeBaseDados() {
    return getString(SISTEMA_NOME_BASE_DADOS, "Operador");
  }

  @Override
  public int getMotivoCampanha() {
    return getInt(SISTEMA_MOTIVO_CAMPANHA, 0);
  }

  @Override
  public int getQtdAgentesReservados() {
    return getInt(SISTEMA_QTD_AGENTES_RESERVADOS, 0);
  }

  @Override
  public boolean isDetectaCaixaPostalPeloTelefone() {
    return getBoolean(SISTEMA_DETECTA_CAIXA_POSTAL_PELO_TELEFONE, true);
  }

  @Override
  public int getQtdMaximaCanaisSimultaneos() {
    return getInt(SISTEMA_QTD_MAXIMA_CANAIS_SIMULTANEOS, 1);
  }

  @Override
  public boolean isUraReversa() {
    return getBoolean(SISTEMA_URA_REVERSA, false);
  }

  @Override
  public int getMotivoFinalizacao() {
    return getInt(SISTEMA_MOTIVO_FINALIZACAO, 0);
  }

  @Override
  public boolean getLimiteTentativasPorTelefone() {
    return getBoolean(SISTEMA_LIMITE_TENTATIVAS_POR_TELEFONE, true);
  }

  @Override
  public boolean isBloqueiaDddPorPeriodo() {
    return getBoolean(SISTEMA_BLOQUEIA_DDD_POR_PERIODO, true);
  }

}
