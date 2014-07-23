<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Mailings do Filtro ${filtro.codigo } - ${filtro.descricao }</h1>
<table>
	<thead>
		<td>idMailing</td>
		<td>Campanha</td>
		<td>Nome Campanha</td>
		<td>Arquivo / Descrição</td>
		<td></td>
	</thead>
	<c:forEach var="mailing" items="${mailingList }" varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${mailing.id }</td>
			<td>${mailing.campanha.nome }</td>
			<td>${mailing.descricao }</td>
			<td>${mailing.nome }</td>
			<td><form method="post"
					action="<c:url value="/filtro/${filtro.id}/mailing/${mailing.id }"/>"
					onsubmit="return confirm('Tem certeza???');">
					<button type="submit" name="_method" value="DELETE">Remover</button>
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post"
	action="<c:url value="/filtro/${filtro.id}/mailing"/>">
	<button type="submit" name="_method" value="PUT">Novo...</button>
</form>