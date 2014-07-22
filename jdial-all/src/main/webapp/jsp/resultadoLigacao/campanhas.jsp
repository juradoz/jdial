<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<h1>Resultados de Ligação</h1>
Selecione uma campanha para visualizar seus resultados
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
					action="<c:url value="/resultadoLigacao/campanha/${campanha.id}"/>">
					<jurado:botaoSubmit value="Ver Resultados..." />
				</form></td>
		</tr>
	</c:forEach>
</table>