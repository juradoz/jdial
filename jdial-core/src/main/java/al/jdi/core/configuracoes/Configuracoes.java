package al.jdi.core.configuracoes;

import org.joda.time.Period;

import al.jdi.common.Service;
import al.jdi.cti.TratamentoSecretariaEletronica;

public interface Configuracoes extends Service{
  
  public interface Factory{
    Configuracoes create(String nomeCampanha);
  }
  
  boolean bloqueiaCelular();

  boolean getCancelaChamadasDesnecessarias();

  boolean getConsideraAtendidasComoAtivas();

  boolean getDiscadorAtivo();

  int getFatorKMaximo();

  int getFatorKMinimo();

  boolean getFinalizaRegistroAtendido();

  Period getIntervaloEntreRodadas();

  Period getIntervaloMinimoNovaRodadaTelefone();

  int getLimiteTentativas();

  int getMaximoEstoque();

  int getMaxRings();

  int getMinimoEstoque();

  int getMinutosExpiracaoChamadasNaoAtendidas();

  int getOperador();

  boolean getSistemaAtivo();

  int getTempoMaximoRegistroEmMemoria();

  TratamentoSecretariaEletronica getTratamentoSecretariaEletronica();

  boolean isPriorizaCelular();

  String getNomeCampanha();

  String digitoSaidaPadraoFixoLocal();

  String digitoSaidaPadraoCelularLocal();

  String digitoSaidaPadraoFixoDDD();

  String digitoSaidaPadraoCelularDDD();

  String dddLocalidade();

  boolean isDigitoSaidaDoBanco();

  String digitoSaidaCustomPrefixoDDD();

  String getNomeBase();

  String getNomeBaseDados();

  int getMotivoCampanha();

  int getQtdAgentesReservados();

  boolean isDetectaCaixaPostalPeloTelefone();

  int getQtdMaximaCanaisSimultaneos();

  boolean isUraReversa();

  int getMotivoFinalizacao();

  boolean getLimiteTentativasPorTelefone();

  boolean isBloqueiaDddPorPeriodo();
}
