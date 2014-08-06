<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Resultado de Ligacao</h1>
<form action="${formAction}" method="post">
	<input type="hidden" name="resultadoLigacao.id"
		value="${resultadoLigacao.id}" /> <input type="hidden"
		name="resultadoLigacao.campanha.id"
		value="${resultadoLigacao.campanha.id}" /> <input type="hidden"
		name="resultadoLigacao.codigo" value="${resultadoLigacao.codigo}" />
	<input type="hidden" name="resultadoLigacao.descricao"
		value="${resultadoLigacao.descricao}" /> <input type="hidden"
		name="resultadoLigacao.nome" value="${resultadoLigacao.nome}" />

	<table>
		<tr style="font-weight: bold;">
			<td>Campanha:</td>
			<td align="right">${resultadoLigacao.campanha.descricao}</td>
		</tr>
		<tr style="font-weight: bold;">
			<td>idCampanha:</td>
			<td align="right">${resultadoLigacao.campanha.id}</td>
		</tr>
		<tr>
			<td>codigo:</td>
			<td align="right">${resultadoLigacao.codigo}</td>
		</tr>
		<tr>
			<td>descricao:</td>
			<td align="right">${resultadoLigacao.descricao}</td>
		</tr>
		<tr>
			<td>nome:</td>
			<td align="right">${resultadoLigacao.nome}</td>
		</tr>
		<tr>
			<td>motivo:</td>
			<td align="right"><input name="resultadoLigacao.motivo"
				value="${resultadoLigacao.motivo}" /></td>
		</tr>
		<tr>
			<td>motivoFinalizacaoPorQuantidadeResultado:</td>
			<td align="right"><input
				name="resultadoLigacao.motivoFinalizacaoPorQuantidadeResultado"
				value="${resultadoLigacao.motivoFinalizacaoPorQuantidadeResultado}" /></td>
		</tr>
		<tr>
			<td>visivelRelatorio:</td>
			<td align="right"><input type="checkbox"
				name="resultadoLigacao.visivelRelatorio"
				<c:if test="${resultadoLigacao.visivelRelatorio}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td>notificaFimTentativa:</td>
			<td align="right"><input type="checkbox"
				name="resultadoLigacao.notificaFimTentativa"
				<c:if test="${resultadoLigacao.notificaFimTentativa}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td>vaiParaOFimDaFila:</td>
			<td align="right"><input type="checkbox"
				name="resultadoLigacao.vaiParaOFimDaFila"
				<c:if test="${resultadoLigacao.vaiParaOFimDaFila}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td>quantidadeDesteResultadoInutilizaTelefone:</td>
			<td align="right"><input
				name="resultadoLigacao.quantidadeDesteResultadoInutilizaTelefone"
				value="${resultadoLigacao.quantidadeDesteResultadoInutilizaTelefone}" /></td>
		</tr>
		<tr>
			<td>inutilizaTelefone:</td>
			<td align="right"><input type="checkbox"
				name="resultadoLigacao.inutilizaTelefone"
				<c:if test="${resultadoLigacao.inutilizaTelefone}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td>intervaloReagendamento:</td>
			<td align="right"><input
				name="resultadoLigacao.intervaloReagendamento"
				value="${resultadoLigacao.intervaloReagendamento}" /></td>
		</tr>
		<tr>
			<td>intervaloIndisponivel:</td>
			<td align="right"><input
				name="resultadoLigacao.intervaloIndisponivel"
				value="${resultadoLigacao.intervaloIndisponivel}" /></td>
		</tr>
		<tr>
			<td>intervaloDesteResultadoReagenda:</td>
			<td align="right"><input
				name="resultadoLigacao.intervaloDesteResultadoReagenda"
				value="${resultadoLigacao.intervaloDesteResultadoReagenda}" /></td>
		</tr>
		<tr>
			<td>insereHistorico:</td>
			<td align="right"><input type="checkbox"
				name="resultadoLigacao.insereHistorico"
				<c:if test="${resultadoLigacao.insereHistorico}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td>incrementaTentativa:</td>
			<td align="right"><input type="checkbox"
				name="resultadoLigacao.incrementaTentativa"
				<c:if test="${resultadoLigacao.incrementaTentativa}">checked="true"</c:if>></td>
		</tr>
		<td>ciclaTelefone:</td>
		<td align="right"><input type="checkbox"
			name="resultadoLigacao.ciclaTelefone"
			<c:if test="${resultadoLigacao.ciclaTelefone}">checked="true"</c:if>></td>
		</tr>
		</tr>
		<td>limpaAgendamentos:</td>
		<td align="right"><input type="checkbox"
			name="resultadoLigacao.limpaAgendamentos"
			<c:if test="${resultadoLigacao.limpaAgendamentos}">checked="true"</c:if>></td>
		</tr>
		</tr>
		<td>incrementaQtdReag:</td>
		<td align="right"><input type="checkbox"
			name="resultadoLigacao.incrementaQtdReag"
			<c:if test="${resultadoLigacao.incrementaQtdReag}">checked="true"</c:if>></td>
		</tr>
		</tr>
		<td>finalizaCliente:</td>
		<td align="right"><input type="checkbox"
			name="resultadoLigacao.finalizaCliente"
			<c:if test="${resultadoLigacao.finalizaCliente}">checked="true"</c:if>></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><button type="submit">Enviar</button></td>
		</tr>
	</table>
</form>