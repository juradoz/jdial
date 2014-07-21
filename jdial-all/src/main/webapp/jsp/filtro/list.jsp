<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
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
					<jurado:botaoSubmit value="Mailings" />
				</form></td>
			<td><form action="<c:url value="/filtro/${filtro.id}"/>">
					<jurado:botaoSubmit value="Alterar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/filtro/${filtro.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<jurado:botaoSubmit value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/filtro"/>">
	<jurado:botaoSubmit value="Novo..." method="PUT" />
</form>