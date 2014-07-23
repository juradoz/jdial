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
					<button type="submit">Mailings</button>
				</form></td>
			<td><form action="<c:url value="/filtro/${filtro.id}"/>">
					<button type="submit">Alterar</button>
				</form></td>
			<td><form method="post"
					action="<c:url value="/filtro/${filtro.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<button type="submit" name="_method" value="DELETE">Remover</button>
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/filtro"/>">
	<button type="submit" name="_method" value="PUT">Novo...</button>
</form>