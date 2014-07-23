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
	href="<c:url value="/css/frame.css"/>" />
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
			</div>
		</div>

		<div id="rodape">
			<tiles:insertAttribute name="rodape" />
		</div>
	</div>
	<script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
	<script type="text/javascript"
		src="<c:url value="/js/jquery-ui.min.js"/>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$(":button").button();

			$("ul.subnav").parent().append("<span></span>"); //Only shows drop down trigger when js is enabled - Adds empty span tag after ul.subnav

			$("ul.topnav li span").click(function() { //When trigger is clicked...

				//Following events are applied to the subnav itself (moving subnav up and down)
				$(this).parent().find("ul.subnav").slideDown('fast').show(); //Drop down the subnav on click

				$(this).parent().hover(function() {
				}, function() {
					$(this).parent().find("ul.subnav").slideUp('slow'); //When the mouse hovers out of the subnav, move it back up
				});

				//Following events are applied to the trigger (Hover events for the trigger)
			}).hover(function() {
				$(this).addClass("subhover"); //On hover over, add class "subhover"
			}, function() { //On Hover Out
				$(this).removeClass("subhover"); //On hover out, remove class "subhover"
			});

		});
	</script>
</body>
</html>
</compress:html>