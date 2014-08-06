<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<h1>Mailings</h1>
<h2>Campanha: ${campanha.nome } - ${campanha.descricao }</h2>
<table>
	<thead>
		<td>idMailing</td>
		<td>Nome</td>
		<td>Descrição</td>
		<td>Data início</td>
		<td>Data término</td>
		<td>Manipulação</td>
		<td></td>
	</thead>
	<c:forEach var="mailing" items="${mailingList }" varStatus="loopStatus">
		<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
			<td>${mailing.id }</td>
			<td>${mailing.nome }</td>
			<td>${mailing.descricao }</td>
			<td><joda:format value="${mailing.dataInicial }"
					pattern="dd/MM/yyyy" /></td>
			<td><joda:format value="${mailing.dataFinal }"
					pattern="dd/MM/yyyy" /></td>
			<td><button type="submit"
					onclick="ajaxAtivarDesativarMailing(this,${mailing.id},'<c:url value="/mailing/ajaxAtivarDesativar"/>')">${mailing.ativo?'Desativar':'Ativar'}</button></td>
			<td><form action="<c:url value="/mailing/${mailing.id }"/>">
					<button type="submit">Editar</button>
				</form></td>
			<td><form
					action="<c:url value="/mailing/purge/${mailing.id }"/>">
					<button type="submit">Expurgar</button>
				</form></td>
			<td><form action="<c:url value="/mailing/limpaTelefoneAtual"/>"
					method="post" onsubmit="return confirm('Tem certeza???');">
					<input type="hidden" name="mailing.id" value="${mailing.id }" />
					<button type="submit">Limpa tel atual</button>
				</form></td>
			<td><form method="post"
					action="<c:url value="/mailing/${mailing.id }"/>" method="post"
					onsubmit="return confirm('Tem certeza???');">
					<button type="submit" name="_method" value="DELETE">Apagar</button>
				</form></td>
		</tr>
	</c:forEach>
</table>
<form method="post" action="<c:url value="/mailing"/>">
	<button type="submit" name="_method" value="PUT">Novo...</button>
</form>
