package al.jdi.core.modelo;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.InformacaoCliente;
import al.jdi.dao.model.Mailing;
import al.jdi.dao.model.Telefone;

public class DiscavelTsaImplTest {

  private static final String DIGITO_SAIDA_PADRAO_FIXO_LOCAL = "DIGITO_SAIDA_PADRAO_FIXO_LOCAL";
  private static final String DIGITO_SAIDA_PADRAO_CELULAR_LOCAL =
      "DIGITO_SAIDA_PADRAO_CELULAR_LOCAL";
  private static final String DIGITO_SAIDA_PADRAO_FIXO_DDD = "DIGITO_SAIDA_PADRAO_FIXO_DDD";
  private static final String DIGITO_SAIDA_PADRAO_CELULAR_DDD = "DIGITO_SAIDA_PADRAO_CELULAR_DDD";
  private static final String DIGITO_SAIDA_CUSTOM_PREFIXO_DDD = "DIGITO_SAIDA_CUSTOM_PREFIXO_DDD";
  private static final String DDD_LOCALIDADE = "DDD_LOCALIDADE";
  private static final String DIGITO_SAIDA = "DIGITO_SAIDA";
  private static final String TELEFONE = "TELEFONE";
  private static final String DDD = "DDD";
  private static final int CHAVE = 1234;
  private static final long CHAVE_TELEFONE = 1l;
  private static final int FILTRO = 2;

  private DiscavelTsaImpl discavelTsaImpl;

  @Mock
  private Configuracoes configuracoes;
  @Mock
  private Cliente cliente;
  @Mock
  private InformacaoCliente informacaoCliente;
  @Mock
  private Telefone telefone;
  @Mock
  private Mailing mailing;
  @Mock
  private Campanha campanha;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(cliente.getInformacaoCliente()).thenReturn(informacaoCliente);
    when(informacaoCliente.getChave()).thenReturn(CHAVE);
    when(cliente.getTelefone()).thenReturn(telefone);
    when(telefone.getChaveTelefone()).thenReturn(CHAVE_TELEFONE);
    when(cliente.getMailing()).thenReturn(mailing);
    when(cliente.getDigitoSaida()).thenReturn(DIGITO_SAIDA);
    when(mailing.getCampanha()).thenReturn(campanha);
    when(campanha.isFiltroAtivo()).thenReturn(true);
    when(cliente.getFiltro()).thenReturn(FILTRO);
    when(telefone.getDdd()).thenReturn(DDD);
    when(telefone.getTelefone()).thenReturn(TELEFONE);

    when(configuracoes.digitoSaidaPadraoFixoLocal()).thenReturn(DIGITO_SAIDA_PADRAO_FIXO_LOCAL);
    when(configuracoes.digitoSaidaPadraoCelularLocal()).thenReturn(
        DIGITO_SAIDA_PADRAO_CELULAR_LOCAL);
    when(configuracoes.digitoSaidaPadraoFixoDDD()).thenReturn(DIGITO_SAIDA_PADRAO_FIXO_DDD);
    when(configuracoes.digitoSaidaPadraoCelularDDD()).thenReturn(DIGITO_SAIDA_PADRAO_CELULAR_DDD);
    when(configuracoes.dddLocalidade()).thenReturn(DDD_LOCALIDADE);
    when(configuracoes.digitoSaidaCustomPrefixoDDD()).thenReturn(DIGITO_SAIDA_CUSTOM_PREFIXO_DDD);

    discavelTsaImpl = new DiscavelTsaImpl(configuracoes, cliente);
  }

  @Test
  public void getChaveDeveriaRetornarChaveEsperada() {
    String chave = discavelTsaImpl.getChave();
    assertThat(chave,
        is(equalTo(CHAVE + "#" + String.valueOf(CHAVE_TELEFONE) + "#" + String.valueOf(FILTRO))));
  }

  @Test
  public void getChaveDeveriaRetornarChaveEsperadaFiltroPadrao() {
    when(campanha.isFiltroAtivo()).thenReturn(false);
    String chave = discavelTsaImpl.getChave();
    assertThat(chave,
        is(equalTo(CHAVE + "#" + String.valueOf(CHAVE_TELEFONE) + "#" + String.valueOf(0))));
  }

  @Test
  public void getClienteDeveriaRetornar() {
    Cliente cliente = discavelTsaImpl.getCliente();
    assertThat(cliente, is(sameInstance(this.cliente)));
  }

  @Test
  public void getDddDeveriaRetornar() {
    String ddd = discavelTsaImpl.getCliente().getTelefone().getDdd();
    assertThat(ddd, is(equalTo(DDD)));
  }

  @Test
  public void getTelefoneDeveriaRetornar() {
    String telefone = discavelTsaImpl.getCliente().getTelefone().getTelefone();
    assertThat(telefone, is(equalTo(TELEFONE)));
  }

  @Test
  public void getDigitoSaidaDeveriaRetornar() {
    String digitoSaida = discavelTsaImpl.getCliente().getDigitoSaida();
    assertThat(digitoSaida, is(equalTo(DIGITO_SAIDA)));
  }

  @Test
  public void getDestinoDeveriaRetornarDestinoPadraoFixoLocal() throws Exception {
    when(cliente.getDigitoSaida()).thenReturn(EMPTY);
    when(telefone.getDdd()).thenReturn(DDD_LOCALIDADE);
    assertThat(discavelTsaImpl.getDestino(), is(DIGITO_SAIDA_PADRAO_FIXO_LOCAL.concat(TELEFONE)));
  }

  @Test
  public void getDestinoDeveriaRetornarDestinoPadraoCelularLocal() throws Exception {
    when(cliente.getDigitoSaida()).thenReturn(EMPTY);
    when(telefone.isCelular()).thenReturn(true);
    when(telefone.getDdd()).thenReturn(DDD_LOCALIDADE);
    assertThat(discavelTsaImpl.getDestino(), is(DIGITO_SAIDA_PADRAO_CELULAR_LOCAL.concat(TELEFONE)));
  }

  @Test
  public void getDestinoDeveriaRetornarDestinoPadraoFixoDdd() throws Exception {
    when(cliente.getDigitoSaida()).thenReturn(EMPTY);
    when(telefone.getDdd()).thenReturn(DDD);
    assertThat(discavelTsaImpl.getDestino(),
        is(DIGITO_SAIDA_PADRAO_FIXO_DDD.concat(DDD).concat(TELEFONE)));
  }

  @Test
  public void getDestinoDeveriaRetornarDestinoPadraoCelularDdd() throws Exception {
    when(cliente.getDigitoSaida()).thenReturn(EMPTY);
    when(telefone.isCelular()).thenReturn(true);
    when(telefone.getDdd()).thenReturn(DDD);
    assertThat(discavelTsaImpl.getDestino(),
        is(DIGITO_SAIDA_PADRAO_CELULAR_DDD.concat(DDD).concat(TELEFONE)));
  }

  @Test
  public void getDestinoDeveriaRetornarDestinoCustomLocal() throws Exception {
    when(telefone.getDdd()).thenReturn(DDD_LOCALIDADE);
    assertThat(discavelTsaImpl.getDestino(), is(DIGITO_SAIDA.concat(TELEFONE)));
  }

  @Test
  public void getDestinoDeveriaRetornarDestinoCustomDdd() throws Exception {
    when(telefone.getDdd()).thenReturn(DDD);
    assertThat(discavelTsaImpl.getDestino(), is(DIGITO_SAIDA
        .concat(DIGITO_SAIDA_CUSTOM_PREFIXO_DDD).concat(DDD).concat(TELEFONE)));
  }

}
