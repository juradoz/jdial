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
					<input type="submit" value="Editar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/definicaoPadrao/${definicaoPadrao.id}"/>">
					<input type="submit" value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/definicaoPadrao"/>">
	<input type="submit" value="Novo..." method="PUT" />
</form>