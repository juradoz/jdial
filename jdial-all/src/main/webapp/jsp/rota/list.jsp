<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
					<input type="submit" value="Editar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/rota/${rota.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<input type="submit" value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/rota"/>">
	<input type="submit" value="Nova..." method="PUT" />
</form>