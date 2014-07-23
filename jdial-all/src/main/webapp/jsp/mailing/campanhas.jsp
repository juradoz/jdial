<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Mailings</h1>
Selecione uma campanha para visualizar seus mailings
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
					action="<c:url value="/mailing/campanha/${campanha.id}"/>">
					<button type="submit">Ver Mailings...</button>
				</form></td>
		</tr>
	</c:forEach>
</table>