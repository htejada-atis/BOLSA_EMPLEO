<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionTitulacionesArea"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaTitulacionesArea"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulacionesArea bean = (VistaTitulacionesArea) uvdatos.getVistas().get(VistaTitulacionesArea.class.getName());
%>


<div class="bolsa-empleo">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	
	%>
	
	<h2>Nueva titulación</h2>
    
    <form id="agregar_titulacion" class="be-form" method="post" action="<%= request.getRequestURI() %>"s>
    	<input type="hidden" name="<%= ControladorGestionTitulacionesArea.PARAM_ACCION %>" id="accion_formulario" value="" />
    	<div class="form-group">
    		<label for="titulacion_nombre">Nombre</label>
    		<textarea id="titulacion_nombre" name="<%= ControladorGestionTitulacionesArea.PARAM_NOMBRE %>" rows="1" cols="60"></textarea>
    	</div>
    	<div class="form-btn">
    		<input id="titulacion_enviar" type="submit" name="<%= ControladorGestionTitulacionesArea.PARAM_ENVIAR %>" value="Agregar titulación"/>
    	</div>
    </form>
    
</div>

<script>

	function subirFichero(event, submit_input) {
		event.preventDefault();
		
		input_accion = document.getElementById("accion_formulario");
		input_accion.value = '<%= ControladorGestionTitulacionesArea.ACCION_AGREGAR_TITULACION %>';
		submit_input.form.submit();
	}

	$(document).ready(function() {
		
		document.getElementById("titulacion_enviar").addEventListener("click", function(event) {
			subirFichero(event, this);
		});
		
	});

</script>