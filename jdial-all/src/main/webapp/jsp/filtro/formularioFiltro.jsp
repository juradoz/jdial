<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>
	Filtro:
	<c:choose>
		<c:when test="${filtro!=null}">${filtro.nome}</c:when>
		<c:otherwise>Novo</c:otherwise>
	</c:choose>
</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="filtro.id" value="${filtro.id}" />

	<table>
		<tr>
			<td>Nome:</td>
			<td align="right"><input name="filtro.nome"
				value="${filtro.nome}" /></td>
		</tr>
		<tr>
			<td>Codigo:</td>
			<td align="right"><input name="filtro.codigo"
				value="${filtro.codigo}" /></td>
		</tr>
		<tr>
			<td>Descrição:</td>
			<td align="right"><input name="filtro.descricao"
				value="${filtro.descricao}" /></td>
		</tr>
		<tr>
			<td>Campanha:</td>
			<td align="right"><select class="selectmenu" name="filtro.campanha.id">
					<c:forEach var="campanha" items="${campanhas}">
						<option value="${campanha.id}"
							<c:if test="${filtro.campanha.id == campanha.id}">selected="true"</c:if>>
							${campanha.nome} - ${campanha.descricao}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><button type="submit">Enviar</button></td>
		</tr>
	</table>
</form>