<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Grupo</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="grupo.id" value="${grupo.id}" />

	<table>
		<tr>
			<td>Código:</td>
			<td align="right"><input name="grupo.codigo"
				value="${grupo.codigo}" /></td>
		</tr>
		<tr>
			<td>Descrição:</td>
			<td align="right"><input name="grupo.descricao"
				value="${grupo.descricao}" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><button type="submit">Enviar</button></td>
		</tr>
	</table>
</form>