<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Campanhas</h1>
<table>
	<thead>
		<td>Nome</td>
		<td>Descrição</td>
		<td>Grupo</td>
		<td>Rota</td>
		<td>Serviço</td>
		<td>Filtro ativo</td>
		<td>Codigo Filtro</td>
		<td></td>
		<td></td>
	</thead>
	<c:forEach var="campanha" items="${campanhaList }"
		varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${campanha.nome }</td>
			<td>${campanha.descricao }</td>
			<td>${campanha.grupo.codigo }</td>
			<td>${campanha.rota.codigo }</td>
			<td>${campanha.servico.nome }</td>
			<td><input type="checkbox" disabled="disabled"
				<c:if test="${campanha.filtroAtivo}">checked="true"</c:if>></td>
			<td>${campanha.codigoFiltro }</td>
			<td><form action="<c:url value="/campanha/${campanha.id}"/>">
					<button type="submit">Alterar</button>
				</form></td>
			<c:if
				test="${usuarioAutenticadoSession.usuario.tipoPerfil == 'ADMINISTRADOR' }">
				<td><form method="post"
						action="<c:url value="/campanha/${campanha.id}"/>"
						onsubmit="return confirm('Tem certeza???');">
						<button type="submit" name="_method" value="DELETE">Remover</button>
					</form></td>
			</c:if>
		</tr>
	</c:forEach>
</table>
<c:if
	test="${usuarioAutenticadoSession.usuario.tipoPerfil == 'ADMINISTRADOR' }">
	<form method="post" action="<c:url value="/campanha"/>">
		<button type="submit" name="_method" value="PUT">Nova...</button>
	</form>
</c:if>