package al.jdi.core.modelo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.enterprise.inject.Instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.core.configuracoes.Configuracoes;
import al.jdi.dao.beans.ClienteDao;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.beans.TelefoneDao;
import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.Telefone;

public class InvalidaAtualEProximoTelefoneTest {

  private InvalidaAtualEProximoTelefone invalidaAtualEProximoTelefone;

  @Mock
  private DaoFactory daoFactory;
  @Mock
  private Cliente cliente;
  @Mock
  private Telefone telefone1;
  @Mock
  private Telefone telefone2;
  @Mock
  private Instance<ProximoTelefone> iProximoTelefone;
  @Mock
  private ProximoTelefone proximoTelefone;
  @Mock
  private ClienteDao clienteDao;
  @Mock
  private TelefoneDao telefoneDao;
  @Mock
  private Configuracoes configuracoes;


  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(cliente.getTelefone()).thenReturn(telefone1);
    when(iProximoTelefone.get()).thenReturn(proximoTelefone);
    when(proximoTelefone.getTelefone(configuracoes, daoFactory, cliente)).thenReturn(telefone2);
    when(daoFactory.getClienteDao()).thenReturn(clienteDao);
    when(daoFactory.getTelefoneDao()).thenReturn(telefoneDao);
    invalidaAtualEProximoTelefone = new InvalidaAtualEProximoTelefone(iProximoTelefone);
  }

  @Test
  public void deveriaRetornarProximo() throws Exception {
    assertThat(invalidaAtualEProximoTelefone.getTelefone(configuracoes, daoFactory, cliente),
        is(sameInstance(telefone2)));
  }

  @Test
  public void deveriaRetornarProximoSeNaoTiverTelefoneAtual() throws Exception {
    when(cliente.getTelefone()).thenReturn(null);
    assertThat(invalidaAtualEProximoTelefone.getTelefone(configuracoes, daoFactory, cliente),
        is(sameInstance(telefone2)));
  }

  @Test
  public void deveriaInvalidar() throws Exception {
    assertThat(invalidaAtualEProximoTelefone.getTelefone(configuracoes, daoFactory, cliente),
        is(sameInstance(telefone2)));
    verify(telefone1).setUtil(false);
  }

  @Test
  public void deveriaAtualizarCliente() throws Exception {
    assertThat(invalidaAtualEProximoTelefone.getTelefone(configuracoes, daoFactory, cliente),
        is(sameInstance(telefone2)));
    verify(clienteDao).atualiza(cliente);
  }

  @Test
  public void deveriaAtualizarTelefone() throws Exception {
    assertThat(invalidaAtualEProximoTelefone.getTelefone(configuracoes, daoFactory, cliente),
        is(sameInstance(telefone2)));
    verify(telefoneDao).atualiza(telefone1);
  }

}
