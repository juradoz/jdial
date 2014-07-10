package al.jdi.dao.beans;

import java.util.List;

import org.hibernate.Session;

import al.jdi.dao.model.MotivoFinalizacao;

class DefaultMotivoFinalizacaoDao implements Dao<MotivoFinalizacao> {

  private final DefaultDao<MotivoFinalizacao> dao;

  public DefaultMotivoFinalizacaoDao(Session session) {
    this.dao = new DefaultDao<>(session, MotivoFinalizacao.class);
  }

  @Override
  public MotivoFinalizacao procura(String s) {
    return dao.procura(s);
  }

  @Override
  public void adiciona(MotivoFinalizacao t) {
    dao.adiciona(t);
  }

  @Override
  public void atualiza(MotivoFinalizacao t) {
    dao.atualiza(t);
  }

  @Override
  public List<MotivoFinalizacao> listaTudo() {
    return dao.listaTudo();
  }

  @Override
  public MotivoFinalizacao procura(Long id) {
    return dao.procura(id);
  }

  @Override
  public void remove(MotivoFinalizacao u) {
    dao.remove(u);
  }

}
