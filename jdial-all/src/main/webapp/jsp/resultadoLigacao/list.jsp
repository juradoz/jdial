<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Resultados de Ligacao</h1>
<table>
	<thead>
		<th>Campanha</th>
		<th>Codigo</th>
		<th>Resultado</th>
		<th>Descrição</th>
		<th>Motivo</th>
		<th></th>
	</thead>
	<c:forEach var="resultadoLigacao" items="${resultadoLigacaoList }"
		varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${resultadoLigacao.campanha.descricao }</td>
			<td>${resultadoLigacao.codigo }</td>
			<td>${resultadoLigacao.nome }</td>
			<td>${resultadoLigacao.descricao }</td>
			<td>${resultadoLigacao.motivo }</td>
			<td><form
					action="<c:url value="/resultadoLigacao/${resultadoLigacao.id}"/>">
					<input type="submit" value="Editar" />
				</form></td>
		</tr>
	</c:forEach>
</table>