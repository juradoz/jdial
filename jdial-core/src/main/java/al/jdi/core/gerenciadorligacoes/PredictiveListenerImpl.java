package al.jdi.core.gerenciadorligacoes;

import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import al.jdi.core.gerenciadorligacoes.GerenciadorLigacoesModule.PredictiveListenerFactory;
import al.jdi.cti.PredictiveListener;

class PredictiveListenerImpl implements PredictiveListener {

  static class PredictiveListenerImplFactory implements PredictiveListenerFactory {
    @Override
    public PredictiveListener create(DefaultGerenciadorLigacoes owner) {
      return new PredictiveListenerImpl(owner);
    }
  }

  private static final Logger logger = getLogger(PredictiveListenerImpl.class);

  private final DefaultGerenciadorLigacoes owner;

  PredictiveListenerImpl(DefaultGerenciadorLigacoes owner) {
    this.owner = owner;
    logger.debug("Iniciando {}", this);
  }

  @Override
  public void chamadaAtendida(int callId) {
    try {
      owner.chamadaAtendida(this, callId);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void chamadaEmFila(int callId) {
    try {
      owner.chamadaEmFila(this, callId);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void chamadaEncerrada(int callId, int causa) {
    try {
      owner.chamadaEncerrada(this, callId, causa);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void chamadaErro(Exception e) {
    try {
      owner.chamadaErro(this, e);
    } catch (Exception e1) {
      logger.error(e.getMessage(), e1);
    }
  }

  @Override
  public void chamadaIniciada(int callId) {
    try {
      owner.chamadaIniciada(this, callId);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void chamadaInvalida(int callId, int causa) {
    try {
      owner.chamadaInvalida(this, callId, causa);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void chamadaNoAgente(int callId, String agente) {
    try {
      owner.chamadaNoAgente(this, callId, agente);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

}
