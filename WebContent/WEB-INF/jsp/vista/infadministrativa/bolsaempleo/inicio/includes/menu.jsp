<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorInicio"%>


<ul>
    <li>
        <a id="menu_inicio" href="" rel="history" title="Inicio">
            Inicio
        </a>
    </li>
    <li>
        <a id="menu_ayuda" href="ayuda" rel="history" title="Ayuda">
            Ayuda
        </a>
    </li>
    <li>
        <a id="menu_faq" href="faq" rel="history" title="Preguntas Frecuentes">
            Preguntas Frecuentes
        </a>
    </li>
    <li>
        <a id="menu_documentos" href="documentos" rel="history" title="Documentos de interés">
            Documentos de interés
        </a>
    </li>
</ul>

<script>

	$(function() {
		if("<%=request.getParameter(ControladorInicio.PARAM_ACCION)%>" == "null" || "<%=request.getParameter(ControladorInicio.PARAM_ACCION)%>" == "") {
			$("#menu_inicio").addClass("active");
		} else {
			$(".nav-bolsa-empleo ul").children("li").each(function() {
				if($(this).find("a").attr("id").split("_")[1] == "<%=request.getParameter(ControladorInicio.PARAM_ACCION)%>") {
					$(this).find("a").addClass("active");
				}
			});
		}
		
		$(".nav-bolsa-empleo ul").children("li").each(function() {
			var a = $(this).find("a");
			a.on("click", function(event) {
				event.preventDefault();
				Atis.sendForm("<%= request.getRequestURI() %>", {'a': a.attr("href")});
			});
		
		});
	
	});

</script>
