$(document).ready(function() {
	$(":button").button({
		width : 200
	});
	$(".selectmenu").selectmenu({
		width : 200
	});

	$(".campoData").datepicker({
		dateFormat : "dd/mm/yy",
		changeMonth : true,
		changeYear : true
	});
	$(".dataToolTip").tooltip();

	$("ul.subnav").parent().append("<span></span>");

	$("ul.topnav li span").click(function() {

		$(this).parent().find("ul.subnav").slideDown('fast').show();

		$(this).parent().hover(function() {
		}, function() {
			$(this).parent().find("ul.subnav").slideUp('slow');
		});

	}).hover(function() {
		$(this).addClass("subhover");
	}, function() {
		$(this).removeClass("subhover");
	});

});

function ajaxAtivarDesativarMailing(ref, id, url) {
	$.post(url, {
		'mailing.id' : id
	}, function(dados, status) {
		if (status == "success") {
			alert("Sucesso!");
			$(ref).button("option", "label", dados);
		} else {
			alert("Erro!");
		}
	});
}

function resetDatePicker(ref) {
	$(ref).datepicker("setDate", null);
}
