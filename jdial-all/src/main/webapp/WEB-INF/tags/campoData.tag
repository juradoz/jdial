<%@ attribute name="id" required="true"%>
<%@ attribute name="name" required="true"%>
<%@ attribute name="dateFormat" required="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url value="/imagens" var="imagens" />
<script type="text/javascript">
	$(function() {
		$('#${id}').datepicker(
				{
					dateFormat : '${dateFormat}',
					showOn : 'button',
					buttonImage : '${imagens}/calendar.gif',
					buttonImageOnly : true,
					showAnim : 'slideDown',
					dayNamesMin : [ 'Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex',
							'Sab' ],
					monthNames : [ 'Janeiro', 'Fevereiro', 'Março', 'Abril',
							'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro',
							'Outubro', 'Novembro', 'Dezembro' ]
				});
	});
</script>
<input type="text" id="${id }" name="${name }" />