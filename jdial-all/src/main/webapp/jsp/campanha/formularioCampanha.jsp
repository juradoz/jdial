<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>
	Campanha:
	<c:choose>
		<c:when test="${campanha!=null}">${campanha.nome}</c:when>
		<c:otherwise>Nova</c:otherwise>
	</c:choose>
</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="campanha.id" value="${campanha.id}" />

	<table>
		<tr>
			<td>Nome:</td>
			<td align="right"><input name="campanha.nome"
				value="${campanha.nome}" /></td>
		</tr>
		<tr>
			<td>Descrição:</td>
			<td align="right"><input name="campanha.descricao"
				value="${campanha.descricao}" /></td>
		</tr>
		<tr>
			<td>Grupo:</td>
			<td align="right"><select class="selectmenu" name="campanha.grupo.id">
					<c:forEach var="grupo" items="${grupos}">
						<option value="${grupo.id}"
							<c:if test="${campanha.grupo.id == grupo.id}">selected="true"</c:if>>
							${grupo.codigo} - ${grupo.descricao}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td>Rota:</td>
			<td align="right"><select class="selectmenu" name="campanha.rota.id">
					<c:forEach var="rota" items="${rotas}">
						<option value="${rota.id}"
							<c:if test="${campanha.rota.id == rota.id}">selected="true"</c:if>>
							${rota.codigo} - ${rota.descricao}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td>Serviço:</td>
			<td align="right"><select class="selectmenu" name="campanha.servico.id">
					<c:forEach var="servico" items="${servicos}">
						<option value="${servico.id}"
							<c:if test="${campanha.servico.id == servico.id}">selected="true"</c:if>>
							${servico.nome} - ${servico.descricao}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td>Filtro ativo:</td>
			<td align="right"><input type="checkbox"
				name="campanha.filtroAtivo"
				<c:if test="${campanha.filtroAtivo}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td>Código Filtro:</td>
			<td align="right"><input name="campanha.codigoFiltro"
				value="${campanha.codigoFiltro}" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><button type="submit">Enviar</button></td>
		</tr>
	</table>
</form>