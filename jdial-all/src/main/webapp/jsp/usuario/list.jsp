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
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${usuario.login }</td>
			<td>${usuario.nome }</td>
			<td>${usuario.tipoPerfil }</td>
			<td><form action="<c:url value="/usuario/${usuario.id}"/>">
					<button type="submit">Alterar</button>
				</form></td>
			<td><form method="post"
					action="<c:url value="/usuario/${usuario.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<button type="submit" name="_method" value="DELETE">Remover</button>
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/usuario"/>">
	<button type="submit" name="_method" value="PUT">Novo...</button>
</form>