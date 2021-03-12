
$(function() {
	
	if(window.location.search == "") {
		$("#menu_inicio").addClass("active");
	} else {
		$(".nav-bolsa-empleo ul").children("li").each(function() {
			if($(this).find("a").attr("id").split("_")[1] == window.location.search.split("=")[1]) {
				$(this).find("a").addClass("active");
			}
		});
	}
	
	

});