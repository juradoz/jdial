<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<form action="${formAction}" method="post">
	<input type="hidden" name="definicao.id" value="${definicao.id}" /> <input
		type="hidden" name="campanha.id" value="${campanha.id}" /> <br>
	<h2>Campanha: ${campanha.nome}</h2>
	<input type="hidden" value="${campanha.id}">

	<table>
		<tr>
			<td>Propriedade:</td>
			<td align="right"><input name="definicao.propriedade"
				value="${definicao.propriedade}" /></td>
		</tr>
		<tr>
			<td>Valor:</td>
			<td align="right"><input name="definicao.valor"
				value="${definicao.valor}" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><jurado:botaoSubmit /></td>
		</tr>
	</table>
</form>