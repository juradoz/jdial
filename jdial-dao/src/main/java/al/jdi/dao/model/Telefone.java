package al.jdi.dao.model;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cascade;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"idCliente", "ddd", "telefone"})},
    indexes = {@Index(name = "IX_telefone_ddd_telefone", columnList = "ddd, telefone"),
        @Index(name = "IX_telefone_tentativa", columnList = "tentativa")})
public class Telefone implements DaoObject, Comparable<Telefone> {
  @Id
  @GeneratedValue
  @Column(name = "idTelefone")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne
  @JoinColumn(name = "idCliente")
  private Cliente cliente;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "telefone")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<HistoricoLigacao> historicoLigacao = new LinkedList<HistoricoLigacao>();

  @Column(nullable = false)
  private String ddd;

  @Column(nullable = false)
  private String telefone;

  @Column
  private Long chaveTelefone;

  @Column(nullable = false)
  private boolean util = true;

  @Column(nullable = false)
  private int tentativa = 0;

  @Column(nullable = false)
  private boolean detectaCaixaPostal = false;

  @Column(nullable = false)
  private int prioridade = 0;

  @Transient
  private boolean conurbada = false;

  @Override
  public int compareTo(Telefone outroTelefone) {
    return new CompareToBuilder().append(uniqueString(), outroTelefone.uniqueString())
        .toComparison();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Telefone other = (Telefone) obj;
    return new EqualsBuilder().append(uniqueString(), other.uniqueString()).isEquals();
  }

  public Long getChaveTelefone() {
    return chaveTelefone;
  }

  public Cliente getCliente() {
    return cliente;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public String getDdd() {
    return ddd;
  }

  public Collection<HistoricoLigacao> getHistoricoLigacao() {
    return historicoLigacao;
  }

  public Long getId() {
    return id;
  }

  public String getTelefone() {
    return telefone;
  }

  public int getTentativa() {
    return tentativa;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(uniqueString()).toHashCode();
  }

  public boolean isCelular() {
    if (isBlank(this.telefone))
      return false;
    return asList('9', '8', '7', '6').contains(this.telefone.charAt(0));
  }

  public boolean isUtil() {
    return util;
  }

  public void setChaveTelefone(Long chaveTelefone) {
    this.chaveTelefone = chaveTelefone;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public void setDdd(String ddd) {
    this.ddd = ddd;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public void setTentativa(int tentativa) {
    this.tentativa = tentativa;
  }

  public void setUtil(boolean util) {
    this.util = util;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("idTelefone", id)
        .append("chaveTelefone", chaveTelefone).append("ddd", ddd).append("telefone", telefone)
        .append("idCliente", getCliente().getId()).toString();
  }

  String uniqueString() {
    return ddd.concat(telefone);
  }

  public void incTentativa() {
    tentativa++;
  }

  public boolean isDetectaCaixaPostal() {
    return detectaCaixaPostal;
  }

  public void setDetectaCaixaPostal(boolean detectaCaixaPostal) {
    this.detectaCaixaPostal = detectaCaixaPostal;
  }

  public int getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(int prioridade) {
    this.prioridade = prioridade;
  }

  public boolean isConurbada() {
    return conurbada;
  }

  public void setConurbada(boolean conurbada) {
    this.conurbada = conurbada;
  }

}
