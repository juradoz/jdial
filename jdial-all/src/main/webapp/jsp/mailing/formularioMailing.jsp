<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<h1>Mailing</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="mailing.id" value="${mailing.id}" />

	<table>
		<tr>
			<td>Campanha:</td>
			<td align="right"><select class="selectmenu"
				name="mailing.campanha.id">
					<c:forEach var="campanha" items="${campanhaList}">
						<option value="${campanha.id}"
							<c:if test="${mailing.campanha.id == campanha.id}">selected="true"</c:if>>
							${campanha.nome} - ${campanha.descricao}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td>Nome:</td>
			<td align="right"><input name="mailing.nome"
				value="${mailing.nome}" /></td>
		</tr>
		<tr>
			<td>Descri��o:</td>
			<td align="right"><input name="mailing.descricao"
				value="${mailing.descricao}" /></td>
		</tr>
		<tr class="dataToolTip">
			<td><a href="#" title="Formato: DD/MM/YYYY">Data Inicial:</a></td>
			<td align="right"><input id="dataInicial" class="campoData"
				name="mailing.dataInicial"
				value="<joda:format value="${mailing.dataInicial }" pattern="dd/MM/yyyy" />" />
				<button type="button" onclick="resetDatePicker(dataInicial)">Limpa</button>
			</td>
		</tr>
		<tr class="dataToolTip">
			<td><a href="#" title="Formato: DD/MM/YYYY">Data Final:</a></td>
			<td align="right"><input id="dataFinal" class="campoData"
				name="mailing.dataFinal"
				value="<joda:format value="${mailing.dataFinal }" pattern="dd/MM/yyyy" />" />
				<button type="button" onclick="resetDatePicker(dataFinal)">Limpa</button>
			</td>
		</tr>
		<tr>
			<td>Ativo:</td>
			<td align="right"><input type="checkbox" name="mailing.ativo"
				<c:if test="${mailing.ativo}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><button type="submit">Enviar</button></td>
		</tr>
	</table>
</form>
</script>