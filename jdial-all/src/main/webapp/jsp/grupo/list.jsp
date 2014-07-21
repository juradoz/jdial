<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<h1>Grupos</h1>
<table>
	<thead>
		<td>Codigo</td>
		<td>Descricao</td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="grupo" items="${grupoList }" varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${grupo.codigo }</td>
			<td>${grupo.descricao }</td>
			<td><form action="<c:url value="/grupo/${grupo.id}"/>">
					<jurado:botaoSubmit value="Editar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/grupo/${grupo.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<jurado:botaoSubmit value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/grupo"/>">
	<jurado:botaoSubmit value="Novo..." method="PUT" />
</form>