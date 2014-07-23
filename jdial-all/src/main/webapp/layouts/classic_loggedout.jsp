<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor"
	prefix="compress"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<compress:html enabled="true" removeComments="false"
	compressJavaScript="true" yuiJsDisableOptimizations="true">
<html>
<head>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/css.css"/>" />
<link type="text/css" href="<c:url value="/css/jquery-ui.min.css"/>"
	rel="stylesheet" />
<title><tiles:getAsString name="title" /></title>
</head>
<body id="corpo">
	<div id="geral">
		<div id="topo" class="login">
			<h2>
				<tiles:insertAttribute name="topo" />
			</h2>
		</div>
		<div id="login">
			<tiles:insertAttribute name="conteudo" />
		</div>
		<tiles:insertAttribute name="errors" />
		<div id="rodape">
			<tiles:insertAttribute name="rodape" />
		</div>
	</div>
	<script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
	<script type="text/javascript"
		src="<c:url value="/js/jquery-ui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/scripts.js"/>"></script>
</body>
</html>
</compress:html>