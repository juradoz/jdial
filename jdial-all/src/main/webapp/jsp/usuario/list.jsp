<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Usuários</h1>
<table>
	<thead>
		<tr>
			<td>Login</td>
			<td>Nome</td>
			<td>Perfil</td>
			<td></td>
			<td></td>
		</tr>
	</thead>
	<c:forEach var="usuario" items="${usuarioList }" varStatus="loopStatus">
		<tr>
			<td>${usuario.login }</td>
			<td>${usuario.nome }</td>
			<td>${usuario.tipoPerfil }</td>
			<td><form action="<c:url value="/usuario/${usuario.id}"/>">
					<input type="submit" value="Alterar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/usuario/${usuario.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<input type="submit" value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/usuario"/>">
	<input type="submit" value="Novo..." method="PUT" />
</form>