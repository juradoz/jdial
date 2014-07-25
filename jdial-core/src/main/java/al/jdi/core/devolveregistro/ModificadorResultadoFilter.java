package al.jdi.core.devolveregistro;

import al.jdi.core.modelo.Ligacao;
import al.jdi.core.tenant.Tenant;
import al.jdi.dao.beans.DaoFactory;
import al.jdi.dao.model.ResultadoLigacao;

interface ModificadorResultadoFilter {

  boolean accept(Tenant tenant, DaoFactory daoFactory, ResultadoLigacao resultadoLigacao,
      Ligacao ligacao);

  ResultadoLigacao modifica(Tenant tenant, DaoFactory daoFactory,
      ResultadoLigacao resultadoLigacao, Ligacao ligacao);

}
