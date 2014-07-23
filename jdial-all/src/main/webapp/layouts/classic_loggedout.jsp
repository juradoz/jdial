<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor"
	prefix="compress"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jurado"%>
<compress:html enabled="true" removeComments="false"
	compressJavaScript="true" yuiJsDisableOptimizations="true">
<html>
<head>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/login.css"/>" />
<link type="text/css"
	href="<c:url value="/css/trontastic/jquery-ui-1.8.1.custom.css"/>"
	rel="stylesheet" />
<script type="text/javascript"
	src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/js/jquery-ui-1.8.1.custom.min.js"/>"></script>
<title><tiles:getAsString name="title" /></title>
</head>
<body id="corpo">
	<div id="geral">
		<div id="topo">
			<h2>
				<tiles:insertAttribute name="topo" />
			</h2>
		</div>
		<div id="login">
			<tiles:insertAttribute name="conteudo" />
		</div>
		<c:if test="${not empty errors}">
			<div id="errors">
				<ul>
					<c:forEach items="${errors}" var="error">
						<li>${error }</li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
		<div id="rodape">
			<tiles:insertAttribute name="rodape" />
		</div>
	</div>
</body>
</html>
</compress:html>