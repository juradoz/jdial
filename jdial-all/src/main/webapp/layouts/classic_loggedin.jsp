<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor"
	prefix="compress"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<compress:html enabled="true" removeComments="false"
	compressJavaScript="true" yuiJsDisableOptimizations="true">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><tiles:getAsString name="title" /></title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/css/css.css"/>" />
<link type="text/css" href="<c:url value="/css/jquery-ui.min.css"/>"
	rel="stylesheet" />
</head>
<body id="corpo">
	<div id="geral">

		<div id="topo">
			<tiles:insertAttribute name="topo" />
		</div>
		<div id="menu">
			<tiles:insertAttribute name="menu" />
		</div>

		<div id="conteudo">
			<div id="esquerda">
				<div id="info">
					<tiles:insertAttribute name="info" />
				</div>
			</div>
			<div id="sub-conteudo">
				<tiles:insertAttribute name="conteudo" />
				<tiles:insertAttribute name="messages" />
				<tiles:insertAttribute name="errors" />
			</div>
		</div>
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