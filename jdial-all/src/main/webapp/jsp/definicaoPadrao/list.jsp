<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Definicoes Padrao</h1>
<table>
	<thead>
		<td>Propriedade</td>
		<td>Valor</td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="definicaoPadrao" items="${definicaoPadraoList }"
		varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${definicaoPadrao.propriedade }</td>
			<td>${definicaoPadrao.valor }</td>
			<td><form
					action="<c:url value="/definicaoPadrao/${definicaoPadrao.id}"/>">
					<button type="submit">Editar</button>
				</form></td>
			<td><form method="post"
					action="<c:url value="/definicaoPadrao/${definicaoPadrao.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<button type="submit" name="_method" value="DELETE">Remover</button>
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/definicaoPadrao"/>">
	<button type="submit" name="_method" value="PUT">Nova...</button>
</form>