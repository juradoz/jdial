<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<h1>Servico</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="servico.id" value="${servico.id}" />
	<table>
		<tr>
			<td>Nome:</td>
			<td align="right"><input name="servico.nome"
				value="${servico.nome}" /></td>
		</tr>
		<tr>
			<td>Descrição:</td>
			<td align="right"><input name="servico.descricao"
				value="${servico.descricao}" /></td>
		</tr>
		<tr>
			<td>Monitorável Qrf:</td>
			<td align="right"><input type="checkbox"
				name="servico.monitoravelQrf"
				<c:if test="${servico.monitoravelQrf}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><jurado:botaoSubmit /></td>
		</tr>
	</table>
</form>