package al.jdi.dao.model;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static org.apache.commons.collections.ComparatorUtils.chainedComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ch.lambdaj.function.compare.ArgumentComparator;

@Entity
public class Cliente implements DaoObject {
  @Id
  @GeneratedValue
  private Long idCliente;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "idInformacaoCliente", nullable = false)
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private InformacaoCliente informacaoCliente;

  @ManyToOne
  @JoinColumn(name = "idMailing", nullable = false)
  private Mailing mailing;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "idTelefone", nullable = true)
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private Telefone telefone;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "cliente")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<Telefone> telefones = new LinkedList<Telefone>();

  @ManyToOne
  @JoinColumn(name = "idEstadoCliente", nullable = false)
  private EstadoCliente estadoCliente;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "cliente")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<Agendamento> agendamento = new LinkedList<Agendamento>();

  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
  private DateTime ultimoInicioRodadaTelefones;

  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
  private DateTime ultimaMudancaEstado = new DateTime();

  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
  private DateTime ordemDaFila = new DateTime();

  @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
  private DateTime disponivelAPartirDe = new DateTime();

  @Column(nullable = false)
  private int filtro = 0;

  @Transient
  private String digitoSaida;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Cliente other = (Cliente) obj;
    return new EqualsBuilder().append(idCliente, other.idCliente).isEquals();
  }

  public void fimDaFila() {
    ordemDaFila = new DateTime();
  }

  public Collection<Agendamento> getAgendamento() {
    return agendamento;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public DateTime getDisponivelAPartirDe() {
    return disponivelAPartirDe;
  }

  public EstadoCliente getEstadoCliente() {
    return estadoCliente;
  }

  public int getFiltro() {
    return filtro;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public InformacaoCliente getInformacaoCliente() {
    return informacaoCliente;
  }

  public Mailing getMailing() {
    return mailing;
  }

  public DateTime getOrdemDaFila() {
    return ordemDaFila;
  }

  public Telefone getTelefone() {
    return telefone;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Collection<Telefone> getTelefones() {
    ArgumentComparator byPrioridade = new ArgumentComparator(on(Telefone.class).getPrioridade());
    ArgumentComparator byId = new ArgumentComparator(on(Telefone.class).getIdTelefone());
    Comparator orderBy = chainedComparator(byPrioridade, byId);
    return sort(telefones, on(Telefone.class), orderBy);
  }

  public DateTime getUltimaMudancaEstado() {
    return ultimaMudancaEstado;
  }

  public DateTime getUltimoInicioRodadaTelefones() {
    return ultimoInicioRodadaTelefones;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(idCliente).toHashCode();
  }

  public void setDisponivelAPartirDe(DateTime disponivelAPartirDe) {
    this.disponivelAPartirDe = disponivelAPartirDe;
  }

  public void setEstadoCliente(EstadoCliente estadoCliente) {
    if (this.estadoCliente != null && this.estadoCliente.equals(estadoCliente))
      return;
    setUltimaMudancaEstado(new DateTime());
    this.estadoCliente = estadoCliente;
  }

  public void setFiltro(int filtro) {
    this.filtro = filtro;
  }

  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public void setInformacaoCliente(InformacaoCliente informacaoCliente) {
    this.informacaoCliente = informacaoCliente;
  }

  public void setMailing(Mailing mailing) {
    this.mailing = mailing;
  }

  public void setOrdemDaFila(DateTime ordemDaFila) {
    this.ordemDaFila = ordemDaFila;
  }

  public void setTelefone(Telefone telefone) {
    this.telefone = telefone;
  }

  void setUltimaMudancaEstado(DateTime ultimaMudancaEstado) {
    this.ultimaMudancaEstado = ultimaMudancaEstado;
  }

  public void setUltimoInicioRodadaTelefones(DateTime ultimoInicioRodadaTelefones) {
    this.ultimoInicioRodadaTelefones = ultimoInicioRodadaTelefones;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("idCliente",
        idCliente).toString();
  }

  public String getDigitoSaida() {
    return digitoSaida;
  }

  public void setDigitoSaida(String digitoSaida) {
    this.digitoSaida = digitoSaida;
  }

}
