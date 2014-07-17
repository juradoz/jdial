<%@ attribute name="value" required="false"%>
<%@ attribute name="clickEvent" required="false"%>
<%@ attribute name="method" required="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	$(function() {
		$("button, input:submit, a", ".demo").button();
	});
</script>
<div class="demo"><c:choose>
	<c:when test="${not empty method}">
		<button type="submit" name="_method" value="${method }"
			<c:if test="${not empty clickEvent}">onclick="${clickEvent }"</c:if>><c:if
			test="${empty value}">Enviar</c:if> <c:if test="${not empty value}">${value }</c:if></button>
	</c:when>
	<c:otherwise>
		<input type="submit" <c:if test="${empty value}">value="Enviar"</c:if>
			<c:if test="${not empty value}">value="${value }"</c:if>
			<c:if test="${not empty clickEvent}">onclick="${clickEvent }"</c:if>>
	</c:otherwise>
</c:choose></div>
