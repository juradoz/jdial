<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<h2>Campanha: ${campanha.nome}</h2>
<table>
	<thead>
		<td>Propriedade</td>
		<td>Valor</td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="definicao" items="${definicaoList }" varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${definicao.propriedade }</td>
			<td>${definicao.valor }</td>
			<td><form action="<c:url value="/definicao/${definicao.id}"/>"><jurado:botaoSubmit
				value="Editar" /></form></td>
			<td><form method="post"
				action="<c:url value="/definicao/${definicao.id}"/>"><jurado:botaoSubmit
				value="Remover" method="DELETE" /></form></td>
		</tr>
	</c:forEach>
</table>
<form method="post"
	action="<c:url value="/definicao/${campanha.id}"/>"><jurado:botaoSubmit
	value="Nova..." method="PUT" /></form>