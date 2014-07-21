<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
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
					<jurado:botaoSubmit value="Editar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/servico/${servico.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<jurado:botaoSubmit value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/servico"/>">
	<jurado:botaoSubmit value="Novo..." method="PUT" />
</form>