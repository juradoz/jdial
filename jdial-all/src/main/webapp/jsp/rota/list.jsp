<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<h1>Rotas</h1>
<table>
	<thead>
		<td>Código</td>
		<td>Descrição</td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="rota" items="${rotaList }" varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${rota.codigo }</td>
			<td>${rota.descricao }</td>
			<td><form action="<c:url value="/rota/${rota.id}"/>">
					<jurado:botaoSubmit value="Editar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/rota/${rota.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<jurado:botaoSubmit value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/rota"/>">
	<jurado:botaoSubmit value="Nova..." method="PUT" />
</form>