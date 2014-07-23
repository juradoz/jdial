<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Expurgo de Mailing</h1>
<h3>Id: ${mailing.id }</h3>
<h3>Nome: ${mailing.nome }</h3>
<h3>Descricao: ${mailing.descricao }</h3>
<form action="<c:url value="/mailing/purge"/>" method="post"
	enctype="multipart/form-data"
	onsubmit="return confirm('Tem certeza??? Todos telefones encontrados serao marcados como inuteis!!!');">
	<input type="hidden" name="mailing.id" value="${mailing.id}" />
	<table>
		<tr>
			<td>Arquivo: <input name="file" type="file" /></td>
			<td><input type="submit" /></td>
		</tr>
	</table>

	<div id="errors">
		<ul>
			<c:forEach items="${errors}" var="error">
				<li>${error }</li>
			</c:forEach>
		</ul>
	</div>

</form>
<h4>ATENCAO: O formato do arquivo deve ser
	ChaveTelefone;Ddd;Telefone</h4>