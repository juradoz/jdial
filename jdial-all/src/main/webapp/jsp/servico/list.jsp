<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Servicos</h1>
<table>
	<thead>
		<td>Nome</td>
		<td>Descrição</td>
		<td>Monitora Qrf</td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="servico" items="${servicoList }" varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${servico.nome }</td>
			<td>${servico.descricao }</td>
			<td><input type="checkbox" disabled="disabled"
				<c:if test="${servico.monitoravelQrf }">checked="true"</c:if>></td>
			<td><form action="<c:url value="/servico/${servico.id}"/>">
					<input type="submit" value="Editar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/servico/${servico.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<input type="submit" value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/servico"/>">
	<input type="submit" value="Novo..." method="PUT" />
</form>