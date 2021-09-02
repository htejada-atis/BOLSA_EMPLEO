<%@ page trimDirectiveWhitespaces="true" %>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Plantilla" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMensajes"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorPlantillas"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlantillas" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaPlantillas bean = (VistaPlantillas) uvdatos.getVistas().get(VistaPlantillas.class.getName());
Plantilla plantilla = bean.getPlantilla();
%>

<div class="bolsa-empleo afinidad-form">
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2><%= plantilla != null ? "Editar" : "Nueva" %> plantilla:</h2>
	
	<form id="plantilla_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
		<input type="hidden" name="<%= ControladorPlantillas.PARAM_ACCION %>" id="accion_formulario" value="<%= plantilla != null ? ControladorPlantillas.ACCION_EDITAR_PLANTILLA : ControladorPlantillas.ACCION_NUEVA_PLANTILLA %>" />
		
	<%	if (plantilla != null) { %>
			<input type="hidden" name="<%= ControladorPlantillas.PARAM_PLANTILLA %>" id="plantilla_id" value="<%= plantilla.getCodNum() %>" />
	<%	} %>
		
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plantilla_nombre" class="bold-label">Nombre: </label>
				<input id="plantilla_nombre"
						class="form-input-custom"
						type="text"
						name="<%= ControladorPlantillas.PARAM_NOMBRE %>"
						value="<%= EscapaHTML.escapa(BolsaEmpleoUtils.getParamForm(request, ControladorPlantillas.PARAM_NOMBRE, plantilla != null ? plantilla.getNombre() : "")) %>"
						required/>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plantilla_titulo" class="bold-label">Asunto: </label>
				<input id="plantilla_titulo"
						class="form-input-custom"
						type="text"
						name="<%= ControladorPlantillas.PARAM_TITULO %>"
						value="<%= EscapaHTML.escapa(BolsaEmpleoUtils.getParamForm(request, ControladorPlantillas.PARAM_TITULO, plantilla != null ? plantilla.getTitulo() : "")) %>"
						required/>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plantilla_cuerpo" class="bold-label">Cuerpo: </label>
				<textarea class="form-input-custom" id="plantilla_cuerpo"
						name="<%= ControladorPlantillas.PARAM_CUERPO %>" rows="5" cols="50"><%= EscapaHTML.escapa(BolsaEmpleoUtils.getParamForm(request, ControladorPlantillas.PARAM_CUERPO, plantilla != null ? plantilla.getCuerpo() : "")) %></textarea>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group"></div>
			<div class="form-group">
				<input id="plantilla_enviar" type="submit" name="<%= ControladorPlantillas.PARAM_ENVIAR %>" value="<%= plantilla != null ? "Guardar" : "Insertar" %>" style="float:right;" />
			</div>
		</div>
	</form>
	
</div>

<script>
$(document).ready(function() {
	
	tinyMCE.init({
		mode : "textareas",
		theme : "modern",
		language : "es",
		height: 250,
		menubar: "edit insert view format table",
		plugins: "advlist autolink autosave link image lists charmap print preview hr anchor pagebreak spellchecker searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking table contextmenu directionality emoticons template textcolor paste fullpage textcolor colorpicker textpattern",
		paste_retain_style_properties: "all",
		toolbar1: "undo redo | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
		toolbar2: "preview | forecolor backcolor | code | styleselect | fontselect | fontsizeselect",
		toolbar_items_size: 'small',
	});
	
});
</script>
