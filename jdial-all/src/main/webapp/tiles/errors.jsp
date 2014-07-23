<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty errors}">
	<div class="ui-state-error ui-corner-all" style="padding: 0 .7em">
		<c:forEach items="${errors}" var="error">
			<p>
				<span class="ui-icon ui-icon-alert"
					style="float: left; margin-right: .3em; margin-bottom: 10px"></span>
				<strong>Erro:</strong> ${error }
			</p>
		</c:forEach>
	</div>
</c:if>