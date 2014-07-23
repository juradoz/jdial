<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Filtro</h1>
<table>
	<thead>
		<td>Nome</td>
		<td>Código</td>
		<td>Descrição</td>
		<td>Nome Campanha</td>
		<td>Descr Campanha</td>
		<td></td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="filtro" items="${filtroList }" varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${filtro.nome }</td>
			<td>${filtro.codigo }</td>
			<td>${filtro.descricao }</td>
			<td>${filtro.campanha.nome }</td>
			<td>${filtro.campanha.descricao }</td>
			<td><form
					action="<c:url value="/filtro/${filtro.id}/mailings"/>">
					<input type="submit" value="Mailings" />
				</form></td>
			<td><form action="<c:url value="/filtro/${filtro.id}"/>">
					<input type="submit" value="Alterar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/filtro/${filtro.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<input type="submit" value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/filtro"/>">
	<input type="submit" value="Novo..." method="PUT" />
</form>