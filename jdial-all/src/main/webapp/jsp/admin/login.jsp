<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<form action="admin/logar" method="post">
	<table align="center">
		<tr>
			<td><label for="login">Usuário</label></td>
			<td align="right"><input name="usuario.login" id="login" /></td>
		</tr>
		<tr>
			<td><label for="senha">Senha</label></td>
			<td align="right"><input type="password" name="usuario.senha"
				id="senha" /></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><jurado:botaoSubmit /></td>
		</tr>
	</table>
</form>