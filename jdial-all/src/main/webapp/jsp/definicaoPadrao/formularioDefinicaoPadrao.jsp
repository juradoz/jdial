<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Definicao Padrao</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="definicaoPadrao.id"
		value="${definicaoPadrao.id}" />
	<table>
		<tr>
			<td>Propriedade:</td>
			<td align="right"><input name="definicaoPadrao.propriedade"
				value="${definicaoPadrao.propriedade}" /></td>
		</tr>
		<tr>
			<td>Valor:</td>
			<td align="right"><input name="definicaoPadrao.valor"
				value="${definicaoPadrao.valor}" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><input type="submit" /></td>
		</tr>
	</table>
</form>