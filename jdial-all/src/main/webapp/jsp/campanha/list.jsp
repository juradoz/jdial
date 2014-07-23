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
					<input type="submit" value="Alterar" />
				</form></td>
			<c:if test="${usuario.tipoPerfil } == ADMINISTRADOR">
				<td><form method="post"
						action="<c:url value="/campanha/${campanha.id}"/>"
						onsubmit="return confirm('Tem certeza???');">
						<input type="submit" value="Remover" method="DELETE" />
					</form></td>
			</c:if>
		</tr>
	</c:forEach>
</table>
<c:if test="${usuario.tipoPerfil } == ADMINISTRADOR">
	<form method="post" action="<c:url value="/campanha"/>">
		<input type="submit" value="Novo..." method="PUT" />
	</form>
</c:if>