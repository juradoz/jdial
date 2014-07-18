<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
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
					<jurado:botaoSubmit value="Alterar" />
				</form></td>
			<td><form method="post"
					action="<c:url value="/usuario/${usuario.id}"/>"
					onsubmit="return confirm('Tem certeza???');">
					<jurado:botaoSubmit value="Remover" method="DELETE" />
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/usuario"/>">
	<jurado:botaoSubmit value="Novo..." method="PUT" />
</form>