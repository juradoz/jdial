<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h2>Campanha: ${campanha.nome}</h2>
<table>
	<thead>
		<td>Propriedade</td>
		<td>Valor</td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="definicao" items="${definicaoList }"
		varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${definicao.propriedade }</td>
			<td>${definicao.valor }</td>
			<td><form action="<c:url value="/definicao/${definicao.id}"/>">
					<button type="submit">Editar</button>
				</form></td>
			<td><form method="post"
					action="<c:url value="/definicao/${definicao.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<button type="submit" name="_method" value="DELETE">Remover</button>
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/definicao/${campanha.id}"/>">
	<button type="submit" name="_method" value="PUT">Nova...</button>
</form>