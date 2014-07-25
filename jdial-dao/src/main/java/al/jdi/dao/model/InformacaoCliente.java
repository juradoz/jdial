package al.jdi.dao.model;

import static org.slf4j.LoggerFactory.getLogger;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

@Entity
@Table(indexes = {@Index(name = "IX_informacaoCliente_chave", columnList = "chave")})
public class InformacaoCliente implements DaoObject {

  private static final Logger logger = getLogger(InformacaoCliente.class);

  @Id
  @GeneratedValue
  @Column(name = "idInformacaoCliente")
  private Long id;

  @Embedded
  private CriacaoModificacao criacaoModificacao = new CriacaoModificacao();

  @OneToOne
  @JoinColumn(name = "idCliente", nullable = true)
  private Cliente cliente;

  @Column(nullable = false)
  private int chave;

  private String nomeBase;

  private String informacoesAdicionais;

  private int providenciaTelefone;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    InformacaoCliente other = (InformacaoCliente) obj;
    return new EqualsBuilder().append(id, other.id).isEquals();
  }

  public int getChave() {
    return chave;
  }

  public Cliente getCliente() {
    return cliente;
  }

  @Override
  public CriacaoModificacao getCriacaoModificacao() {
    return criacaoModificacao;
  }

  public Long getId() {
    return id;
  }

  public String getInformacoesAdicionais() {
    return informacoesAdicionais;
  }

  public int getProvidenciaTelefone() {
    return providenciaTelefone;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  public void setChave(int chave) {
    this.chave = chave;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInformacoesAdicionais(String informacoesAdicionais) {
    this.informacoesAdicionais = informacoesAdicionais;
  }

  public void setProvidenciaTelefone(int providenciaTelefone) {
    this.providenciaTelefone = providenciaTelefone;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("chave", chave)
        .toString();
  }

  public String getNomeBase() {
    return nomeBase;
  }

  public void setNomeBase(String nomeBase) {
    this.nomeBase = nomeBase;
  }

  public int getSplitCodCliente() {
    String[] info = cliente.getInformacaoCliente().getInformacoesAdicionais().split("#");
    for (int i = 0; i < info.length; i++)
      switch (i) {
        case 0:
          try {
            return Integer.parseInt(info[i]);
          } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
          }
      }
    return 0;
  }

  public int getSplitCodCampanha() {
    String[] info = cliente.getInformacaoCliente().getInformacoesAdicionais().split("#");
    for (int i = 0; i < info.length; i++)
      switch (i) {
        case 1:
          try {
            return Integer.parseInt(info[i]);
          } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
          }
      }
    return 0;
  }
}
