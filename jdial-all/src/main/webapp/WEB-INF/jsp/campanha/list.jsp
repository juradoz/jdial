<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<ul>
	<c:forEach items="${campanhaList}" var="campanha">
		<li>${campanha.nome}-${campanha.descricao}</li>
	</c:forEach>
</ul>
