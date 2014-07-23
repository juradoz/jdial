<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Usuário</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="usuario.id" value="${usuario.id}" />
	<table>
		<tr>
			<td>Nome:</td>
			<td align="right"><input name="usuario.nome"
				value="${usuario.nome }" /></td>
		</tr>
		<tr>
			<td>Login:</td>
			<td align="right"><input name="usuario.login"
				value="${usuario.login }" /></td>
		</tr>
		<tr>
			<td>Senha:</td>
			<td align="right"><input name="usuario.senha" type="password"
				value="" /></td>
		</tr>
		<tr>
			<td>Grupo:</td>
			<td align="right"><select name="usuario.tipoPerfil">
					<c:forEach var="tipoPerfil" items="${tiposPerfil }">
						<option value="${tipoPerfil}"
							<c:if test="${usuario.tipoPerfil == tipoPerfil }">selected="true"</c:if>>
							${tipoPerfil }</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><input type="submit" /></td>
		</tr>
	</table>
</form>