<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<ul class="topnav">
	<li><a href="#">Campanhas</a>
		<ul class="subnav">
			<li><a href="<c:url value="/campanhas"/>">Lista Campanhas</a></li>
			<c:if test="${usuarioAutenticadoSession.usuario.tipoPerfil == 'ADMINISTRADOR' }">
				<li><a href="<c:url value="/definicao/campanhas"/>">Definicoes</a></li>
				<li><a href="<c:url value="/definicaoPadrao"/>">Definicoes
						Padrao</a></li>
				<li><a href="<c:url value="/grupos"/>">Grupos</a></li>
				<li><a href="<c:url value="/rotas"/>">Rotas</a></li>
				<li><a href="<c:url value="/servicos"/>">Serviços</a></li>
			</c:if>
			<li><a href="<c:url value="/filtros"/>">Filtros</a></li>
		</ul></li>
	<c:if test="${usuarioAutenticadoSession.usuario.tipoPerfil == 'ADMINISTRADOR' }">
		<li><a href="#">Mailings</a>
			<ul class="subnav">
				<li><a href="<c:url value="/mailing/campanhas"/>">Mailings</a></li>
				<li><a href="<c:url value="/estadoDoMailing/campanhas"/>">Estado
						do mailing</a></li>
				<li><a href="<c:url value="/resultadoLigacao/campanhas"/>">Resultados
						de Ligação</a></li>
			</ul></li>
		<li><a href="<c:url value="/usuarios"/>">Usuários</a></li>
	</c:if>
	<li><a href="<c:url value="/admin/logout"/>">Logout</a></li>
</ul>