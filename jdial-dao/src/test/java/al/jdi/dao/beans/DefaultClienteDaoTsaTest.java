package al.jdi.dao.beans;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import al.jdi.dao.model.Cliente;
import al.jdi.dao.model.ResultadoLigacao;
import al.jdi.dao.model.Situacao;

public class DefaultClienteDaoTsaTest {

  private static final Situacao situacao = Situacao.FINALIZACAO;
  private static final int motivo = 0;
  private static final int motivoFinalizacao = 0;
  private static final int operador = 0;
  private static final int codDetCamp = 0;

  private DefaultClienteDaoTsa defaultClienteDaoTsa;

  @Mock
  private Session session;
  @Mock
  private SQLQuery query;
  @Mock
  private ResultadoLigacao resultadoLigacao;
  @Mock
  private Cliente cliente;

  @Before
  public void setUp() {
    initMocks(this);
    when(session.createSQLQuery(anyString())).thenReturn(query);
    defaultClienteDaoTsa = new DefaultClienteDaoTsa(session);
  }

  @Test
  public void deveriaCriarQuerySemIncrementarQtdReag() {
    when(resultadoLigacao.isIncrementaQtdReag()).thenReturn(false);
    defaultClienteDaoTsa.updateDetCampanha(resultadoLigacao, cliente, situacao, motivo,
        motivoFinalizacao, operador, codDetCamp);
    verify(session)
        .createSQLQuery(
            "update Operador.DetCampanha set situacao = :situacao, motivo = :motivo, dataHoraUC = Now(), operador = :operador  where codDetCamp = :codDetCamp");
  }

  @Test
  public void deveriaCriarQueryIncrementandoQtdReag() {
    when(resultadoLigacao.isIncrementaQtdReag()).thenReturn(true);
    defaultClienteDaoTsa.updateDetCampanha(resultadoLigacao, cliente, situacao, motivo,
        motivoFinalizacao, operador, codDetCamp);
    verify(session)
        .createSQLQuery(
            "update Operador.DetCampanha set situacao = :situacao, motivo = :motivo, dataHoraUC = Now(), operador = :operador , QtdReag = QtdReag + 1  where codDetCamp = :codDetCamp");
  }
}
