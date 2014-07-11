package al.jdi.core.devolveregistro;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

import al.jdi.core.devolveregistro.DevolveRegistroModule;
import al.jdi.core.devolveregistro.ProcessoDevolucao;
import al.jdi.core.modelo.Ligacao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;

public class DevolveRegistroModuleTest {

  private class PX implements ProcessoDevolucao {

    private final int ordem;

    PX(int ordem) {
      this.ordem = ordem;
    }

    @Override
    public int compareTo(ProcessoDevolucao arg0) {
      return new CompareToBuilder().append(ordem, arg0.getOrdem()).toComparison();
    }

    @Override
    public int getOrdem() {
      return ordem;
    }

    @Override
    public boolean accept(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
        DaoFactory daoFactory) {
      return false;
    }

    @Override
    public boolean executa(Ligacao ligacao, Cliente cliente, ResultadoLigacao resultadoLigacao,
        DaoFactory daoFactory) {
      return false;
    }

    @Override
    public String toString() {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(ordem).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      PX other = (PX) obj;
      return new EqualsBuilder().append(ordem, other.ordem).isEquals();
    }

  }

  private ProcessoDevolucao p1;
  private ProcessoDevolucao p2;
  private ProcessoDevolucao p3;
  private LinkedHashSet<ProcessoDevolucao> set;

  private DevolveRegistroModule processosDevolucaoProvider;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    p1 = new PX(1);
    p2 = new PX(2);
    p3 = new PX(3);
    set = new LinkedHashSet<ProcessoDevolucao>(Arrays.asList(p3, p2, p1));
    processosDevolucaoProvider = new DevolveRegistroModule();
  }

  @Test
  public void getP1() throws Exception {
    assertThat(processosDevolucaoProvider.getProcessosDevolucao(set).get(0), is(sameInstance(p1)));
  }

  @Test
  public void getP2() throws Exception {
    assertThat(processosDevolucaoProvider.getProcessosDevolucao(set).get(1), is(sameInstance(p2)));
  }

  @Test
  public void getP3() throws Exception {
    assertThat(processosDevolucaoProvider.getProcessosDevolucao(set).get(2), is(sameInstance(p3)));
  }

}
