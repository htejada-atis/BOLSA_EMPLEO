<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFicheros"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFicheros bean = (VistaFicheros) uvdatos.getVistas().get(VistaFicheros.class.getName());
%>
<div class="bolsa-empleo">
	<h2>Ficheros</h2>
	
	<form id="subir_fichero" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
		<input type="hidden" name="<%= ControladorGestionFicheros.PARAM_ACCION %>" id="accion_formulario" value="" />
		<div class="form-file">
			<input type="file" name="<%= ControladorGestionFicheros.PARAM_FICHERO %>"/>
		</div>
		<div class="form-btn">
    		<input id="fichero_enviar" type="submit" name="<%= ControladorGestionFicheros.PARAM_ENVIAR %>" value="Enviar"/>
    	</div>
	</form>
	
</div>

<script>

function enviarNoticia(event, submit_input) {
	event.preventDefault();
	
	input_accion = document.getElementById("accion_formulario");
	input_accion.value = '<%= ControladorGestionFicheros.ACCION_AGREGAR_FICHERO %>';
	
	submit_input.form.submit();
}

	$(document).ready(function() {
		document.getElementById("fichero_enviar").addEventListener("click", function(event) {
			enviarNoticia(event, this);
		});
	});

</script>