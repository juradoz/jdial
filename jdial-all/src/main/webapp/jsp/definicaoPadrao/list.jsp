<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<h1>Definicoes Padrao</h1>
<table>
	<thead>
		<td>Propriedade</td>
		<td>Valor</td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="definicaoPadrao" items="${definicaoPadraoList }" varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${definicaoPadrao.propriedade }</td>
			<td>${definicaoPadrao.valor }</td>
			<td><form action="<c:url value="/definicaoPadrao/${definicaoPadrao.id}"/>"><jurado:botaoSubmit
				value="Editar" /></form></td>
			<td><form method="post"
				action="<c:url value="/definicaoPadrao/${definicaoPadrao.id}"/>"><jurado:botaoSubmit
				value="Remover" method="DELETE" /></form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/definicaoPadrao"/>"><jurado:botaoSubmit
	value="Novo..." method="PUT" /></form>