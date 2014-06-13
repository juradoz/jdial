package al.jdi.dao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(indexes = {
    @Index(name = "IX_historicoLigacao_idCliente_idResultadoLigacao",
        columnList = "idCliente, idResultadoLigacao"),
    @Index(name = "IX_idCliente_idResultadoLigacao_inicio",
        columnList = "idCliente, idResultadoLigacao, inicio"),
    @Index(name = "IX_idCliente_idResultadoLigacao_criacao",
        columnList = "idCliente, idResultadoLigacao, criacao")})
public class HistoricoLigacao implements DaoObject {
  @Id
  @GeneratedValue
  private Long idHistoricoLigacao;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne
  @JoinColumn(name = "idCliente", nullable = false)
  private Cliente cliente;

  @ManyToOne
  @JoinColumn(name = "idTelefone", nullable = false)
  private Telefone telefone;

  @OneToOne
  @JoinColumn(name = "idResultadoLigacao", nullable = false)
  private ResultadoLigacao resultadoLigacao;

  @Column(nullable = false)
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime inicio;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime atendimento;

  @Column(nullable = false)
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime termino;

  private String descricao;

  @Transient
  private String agente;

  public DateTime getAtendimento() {
    return atendimento;
  }

  public Cliente getCliente() {
    return cliente;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public Long getIdHistoricoLigacao() {
    return idHistoricoLigacao;
  }

  public DateTime getInicio() {
    return inicio;
  }

  public ResultadoLigacao getResultadoLigacao() {
    return resultadoLigacao;
  }

  public Telefone getTelefone() {
    return telefone;
  }

  public DateTime getTermino() {
    return termino;
  }

  public void setAtendimento(DateTime atendimento) {
    this.atendimento = atendimento;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setIdHistoricoLigacao(Long idHistoricoLigacao) {
    this.idHistoricoLigacao = idHistoricoLigacao;
  }

  public void setInicio(DateTime inicio) {
    this.inicio = inicio;
  }

  public void setResultadoLigacao(ResultadoLigacao resultadoLigacao) {
    this.resultadoLigacao = resultadoLigacao;
  }

  public void setTelefone(Telefone telefone) {
    this.telefone = telefone;
  }

  public void setTermino(DateTime termino) {
    this.termino = termino;
  }

  public String getAgente() {
    return agente;
  }

  public void setAgente(String agente) {
    this.agente = agente;
  }
}
