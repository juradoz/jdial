package al.jdi.dao.model;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.joda.time.DateTimeConstants.FRIDAY;
import static org.joda.time.DateTimeConstants.MONDAY;
import static org.joda.time.DateTimeConstants.SATURDAY;
import static org.joda.time.DateTimeConstants.SUNDAY;
import static org.joda.time.DateTimeConstants.THURSDAY;
import static org.joda.time.DateTimeConstants.TUESDAY;
import static org.joda.time.DateTimeConstants.WEDNESDAY;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalTime;

import al.jdi.dao.beans.Dao.CampoBusca;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"ddd"})}, indexes = {@Index(
    name = "IX_restricaoHorario_ddd_ativo", columnList = "ddd, ativo")})
public class RestricaoHorario implements DaoObject {

  @Retention(RUNTIME)
  @Target(METHOD)
  public @interface DiaSemana {
    int dayOfWeek();
  }

  @Retention(RUNTIME)
  @Target(METHOD)
  public @interface HoraInicio {
  }

  @Retention(RUNTIME)
  @Target(METHOD)
  public @interface HoraFinal {
  }

  @Id
  @GeneratedValue
  @Column(name = "idRestricaoHorario")
  private long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @CampoBusca
  @Column(nullable = false)
  private String ddd;

  @Column(nullable = false)
  private boolean ativo = true;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaInicioSegunda;
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaFinalSegunda;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaInicioTerca;
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaFinalTerca;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaInicioQuarta;
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaFinalQuarta;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaInicioQuinta;
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaFinalQuinta;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaInicioSexta;
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaFinalSexta;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaInicioSabado;
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaFinalSabado;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaInicioDomingo;
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
  private LocalTime horaFinalDomingo;

  public RestricaoHorario() {}

  RestricaoHorario(String ddd, boolean ativo, LocalTime horaInicioSegunda,
      LocalTime horaFinalSegunda, LocalTime horaInicioTerca, LocalTime horaFinalTerca,
      LocalTime horaInicioQuarta, LocalTime horaFinalQuarta, LocalTime horaInicioQuinta,
      LocalTime horaFinalQuinta, LocalTime horaInicioSexta, LocalTime horaFinalSexta,
      LocalTime horaInicioSabado, LocalTime horaFinalSabado, LocalTime horaInicioDomingo,
      LocalTime horaFinalDomingo) {
    this.ddd = ddd;
    this.ativo = ativo;
    this.horaInicioSegunda = horaInicioSegunda;
    this.horaFinalSegunda = horaFinalSegunda;
    this.horaInicioTerca = horaInicioTerca;
    this.horaFinalTerca = horaFinalTerca;
    this.horaInicioQuarta = horaInicioQuarta;
    this.horaFinalQuarta = horaFinalQuarta;
    this.horaInicioQuinta = horaInicioQuinta;
    this.horaFinalQuinta = horaFinalQuinta;
    this.horaInicioSexta = horaInicioSexta;
    this.horaFinalSexta = horaFinalSexta;
    this.horaInicioSabado = horaInicioSabado;
    this.horaFinalSabado = horaFinalSabado;
    this.horaInicioDomingo = horaInicioDomingo;
    this.horaFinalDomingo = horaFinalDomingo;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDdd() {
    return ddd;
  }

  public void setDdd(String ddd) {
    this.ddd = ddd;
  }

  public boolean isAtivo() {
    return ativo;
  }

  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
  }

  @HoraInicio
  @DiaSemana(dayOfWeek = MONDAY)
  public LocalTime getHoraInicioSegunda() {
    return horaInicioSegunda;
  }

  public void setHoraInicioSegunda(LocalTime horaInicioSegunda) {
    this.horaInicioSegunda = horaInicioSegunda;
  }

  @HoraFinal
  @DiaSemana(dayOfWeek = MONDAY)
  public LocalTime getHoraFinalSegunda() {
    return horaFinalSegunda;
  }

  public void setHoraFinalSegunda(LocalTime horaFinalSegunda) {
    this.horaFinalSegunda = horaFinalSegunda;
  }

  @HoraInicio
  @DiaSemana(dayOfWeek = TUESDAY)
  public LocalTime getHoraInicioTerca() {
    return horaInicioTerca;
  }

  public void setHoraInicioTerca(LocalTime horaInicioTerca) {
    this.horaInicioTerca = horaInicioTerca;
  }

  @HoraFinal
  @DiaSemana(dayOfWeek = TUESDAY)
  public LocalTime getHoraFinalTerca() {
    return horaFinalTerca;
  }

  public void setHoraFinalTerca(LocalTime horaFinalTerca) {
    this.horaFinalTerca = horaFinalTerca;
  }

  @HoraInicio
  @DiaSemana(dayOfWeek = WEDNESDAY)
  public LocalTime getHoraInicioQuarta() {
    return horaInicioQuarta;
  }

  public void setHoraInicioQuarta(LocalTime horaInicioQuarta) {
    this.horaInicioQuarta = horaInicioQuarta;
  }

  @HoraFinal
  @DiaSemana(dayOfWeek = WEDNESDAY)
  public LocalTime getHoraFinalQuarta() {
    return horaFinalQuarta;
  }

  public void setHoraFinalQuarta(LocalTime horaFinalQuarta) {
    this.horaFinalQuarta = horaFinalQuarta;
  }

  @HoraInicio
  @DiaSemana(dayOfWeek = THURSDAY)
  public LocalTime getHoraInicioQuinta() {
    return horaInicioQuinta;
  }

  public void setHoraInicioQuinta(LocalTime horaInicioQuinta) {
    this.horaInicioQuinta = horaInicioQuinta;
  }

  @HoraFinal
  @DiaSemana(dayOfWeek = THURSDAY)
  public LocalTime getHoraFinalQuinta() {
    return horaFinalQuinta;
  }

  public void setHoraFinalQuinta(LocalTime horaFinalQuinta) {
    this.horaFinalQuinta = horaFinalQuinta;
  }

  @HoraInicio
  @DiaSemana(dayOfWeek = FRIDAY)
  public LocalTime getHoraInicioSexta() {
    return horaInicioSexta;
  }

  public void setHoraInicioSexta(LocalTime horaInicioSexta) {
    this.horaInicioSexta = horaInicioSexta;
  }

  @HoraFinal
  @DiaSemana(dayOfWeek = FRIDAY)
  public LocalTime getHoraFinalSexta() {
    return horaFinalSexta;
  }

  public void setHoraFinalSexta(LocalTime horaFinalSexta) {
    this.horaFinalSexta = horaFinalSexta;
  }

  @HoraInicio
  @DiaSemana(dayOfWeek = SATURDAY)
  public LocalTime getHoraInicioSabado() {
    return horaInicioSabado;
  }

  public void setHoraInicioSabado(LocalTime horaInicioSabado) {
    this.horaInicioSabado = horaInicioSabado;
  }

  @HoraFinal
  @DiaSemana(dayOfWeek = SATURDAY)
  public LocalTime getHoraFinalSabado() {
    return horaFinalSabado;
  }

  public void setHoraFinalSabado(LocalTime horaFinalSabado) {
    this.horaFinalSabado = horaFinalSabado;
  }

  @HoraInicio
  @DiaSemana(dayOfWeek = SUNDAY)
  public LocalTime getHoraInicioDomingo() {
    return horaInicioDomingo;
  }

  public void setHoraInicioDomingo(LocalTime horaInicioDomingo) {
    this.horaInicioDomingo = horaInicioDomingo;
  }

  @HoraFinal
  @DiaSemana(dayOfWeek = SUNDAY)
  public LocalTime getHoraFinalDomingo() {
    return horaFinalDomingo;
  }

  public void setHoraFinalDomingo(LocalTime horaFinalDomingo) {
    this.horaFinalDomingo = horaFinalDomingo;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(ddd).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RestricaoHorario other = (RestricaoHorario) obj;
    return new EqualsBuilder().append(this.ddd, other.ddd).isEquals();
  }

}
