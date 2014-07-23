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
					<button type="submit">Editar</button>
				</form></td>
			<td><form method="post"
					action="<c:url value="/grupo/${grupo.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<button type="submit" name="_method" value="DELETE">Remover</button>
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/grupo"/>">
	<button type="submit" name="_method" value="PUT">Novo...</button>
</form>