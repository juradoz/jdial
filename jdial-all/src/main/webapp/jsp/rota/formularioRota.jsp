<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<h1>Rota</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="rota.id" value="${rota.id}" />
	<table>
		<tr>
			<td>Código:</td>
			<td align="right"><input name="rota.codigo"
				value="${rota.codigo}" /></td>
		</tr>
		<tr>
			<td>Descrição:</td>
			<td align="right"><input name="rota.descricao"
				value="${rota.descricao}" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><jurado:botaoSubmit /></td>
		</tr>
	</table>
</form>