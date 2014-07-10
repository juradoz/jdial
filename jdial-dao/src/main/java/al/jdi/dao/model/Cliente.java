package al.jdi.dao.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Cliente implements DaoObject {
  @Id
  @GeneratedValue
  @Column(name = "idCliente")
  private Long id;

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
  private final List<Telefone> telefones = new LinkedList<Telefone>();

  @ManyToOne
  @JoinColumn(name = "idEstadoCliente", nullable = false)
  private EstadoCliente estadoCliente;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "cliente")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private final Collection<Agendamento> agendamento = new LinkedList<Agendamento>();

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "cliente")
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private Collection<HistoricoCliente> historicoCliente = new LinkedList<HistoricoCliente>();

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
    return new EqualsBuilder().append(id, other.id).isEquals();
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

  public Collection<HistoricoCliente> getHistoricoCliente() {
    return historicoCliente;
  }

  public Long getId() {
    return id;
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

  public List<Telefone> getTelefones() {
    return telefones;
  }

  public DateTime getUltimaMudancaEstado() {
    return ultimaMudancaEstado;
  }

  public DateTime getUltimoInicioRodadaTelefones() {
    return ultimoInicioRodadaTelefones;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  public void setDisponivelAPartirDe(DateTime disponivelAPartirDe) {
    this.disponivelAPartirDe = disponivelAPartirDe;
  }

  public void setEstadoCliente(EstadoCliente estadoCliente) {
    if (this.estadoCliente != null && this.estadoCliente.equals(estadoCliente))
      return;
    // DaoFactory daoFactory = new DaoFactory();
    // try {
    // setUltimaMudancaEstado(daoFactory.getDataBanco());
    // } finally {
    // daoFactory.close();
    // }
    setUltimaMudancaEstado(new DateTime());
    this.estadoCliente = estadoCliente;
  }

  public void setFiltro(int filtro) {
    this.filtro = filtro;
  }

  public void setHistoricoCliente(Collection<HistoricoCliente> historicoCliente) {
    this.historicoCliente = historicoCliente;
  }

  public void setId(Long id) {
    this.id = id;
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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("idCliente", id)
        .toString();
  }

  public String toStringFull() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("idCliente", getId())
        .append("chave", getInformacaoCliente().getChave()).append("telefone", getTelefone())
        .append("ordemDaFila", getOrdemDaFila())
        .append("providencia", getInformacaoCliente().getProvidenciaTelefone()).toString();
  }

  public String getDigitoSaida() {
    return digitoSaida;
  }

  public void setDigitoSaida(String digitoSaida) {
    this.digitoSaida = digitoSaida;
  }

}
