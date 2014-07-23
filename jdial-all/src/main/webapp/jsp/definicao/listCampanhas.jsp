<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h2>Escolha uma campanha:</h2>
<table>
	<thead>
		<td>Nome</td>
		<td>Descrição</td>
		<td></td>
	</thead>
	<c:forEach var="campanha" items="${campanhaList }"
		varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${campanha.nome }</td>
			<td>${campanha.descricao }</td>
			<td><form
					action="<c:url value="/definicao/campanhas/${campanha.id }"/>">
					<button type="submit">Definição...</button>
				</form></td>
		</tr>
	</c:forEach>
</table>