<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<h1>Filtro: ${filtro.nome}</h1>
<form action="addMailing" method="post">
	<table>
		<tr>
			<td>Mailings ativos:</td>
			<td align="right"><select name="mailing.id">
					<c:forEach var="mailing" items="${mailingList}">
						<option value="${mailing.id}">${mailing.id} -
							${mailing.nome}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><jurado:botaoSubmit /></td>
		</tr>
	</table>
</form>