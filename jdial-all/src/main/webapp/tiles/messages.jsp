<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty messages}">
	<div class="ui-state-highlight ui-corner-all" style="padding: 0 .7em">
		<c:forEach items="${messages}" var="message">
			<p>
				<span class="ui-icon ui-icon-info"
					style="float: left; margin-right: .3em; margin-bottom: 10px"></span>
				<strong>Mensagem:</strong> ${message }
			</p>
		</c:forEach>
	</div>
</c:if>