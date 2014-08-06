<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<center>&copy; 2014 DJMV/Parla!</center>
<c:if test="${!empty usuarioAutenticadoSession.usuario }">
	Logado como ${usuarioAutenticadoSession.usuario.nome }(${usuarioAutenticadoSession.usuario.tipoPerfil })
</c:if>