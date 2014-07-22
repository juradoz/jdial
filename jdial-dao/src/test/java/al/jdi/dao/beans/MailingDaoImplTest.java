package al.jdi.dao.beans;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import al.jdi.dao.model.Campanha;
import al.jdi.dao.model.CriacaoModificacao;
import al.jdi.dao.model.Mailing;

public class MailingDaoImplTest {

  private static final Long CAMPANHA_ID = 1l;

  private DefaultMailingDao defaultMailingDao;

  @Mock
  private Session session;
  @Mock
  private Mailing mailing;
  @Mock
  private Campanha campanha;
  @Mock
  private SQLQuery query;
  @Mock
  private CriacaoModificacao criacaoModificacao;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    when(mailing.getCampanha()).thenReturn(campanha);
    when(campanha.getId()).thenReturn(CAMPANHA_ID);
    when(session.load(Campanha.class, CAMPANHA_ID)).thenReturn(campanha);
    when(session.createSQLQuery(Mockito.anyString())).thenReturn(query);
    when(query.setLong(Mockito.anyString(), Mockito.anyLong())).thenReturn(query);
    when(campanha.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    when(mailing.getCriacaoModificacao()).thenReturn(criacaoModificacao);
    defaultMailingDao = new DefaultMailingDao(session);
  }

  @Test
  public void removeDeveriaSetarTrueLimparMemoria() throws Exception {
    defaultMailingDao.remove(mailing);
    verify(campanha).setLimpaMemoria(true);
  }

  @Test
  public void removeDeveriaAtualizarCampanhaLimparMemoria() throws Exception {
    defaultMailingDao.remove(mailing);
    verify(session).update(campanha);
  }

  @Test
  public void atualizaDeveriaSetarTrueLimparMemoria() throws Exception {
    defaultMailingDao.atualiza(mailing);
    verify(campanha).setLimpaMemoria(true);
  }

  @Test
  public void atualizaDeveriaAtualizarCampanhaLimparMemoria() throws Exception {
    defaultMailingDao.atualiza(mailing);
    verify(session).update(campanha);
  }

}
