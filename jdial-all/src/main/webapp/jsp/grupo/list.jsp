<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
					<input type="submit" value="Editar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/grupo/${grupo.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<input type="submit" value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/grupo"/>">
	<input type="submit" value="Novo..." method="PUT" />
</form>