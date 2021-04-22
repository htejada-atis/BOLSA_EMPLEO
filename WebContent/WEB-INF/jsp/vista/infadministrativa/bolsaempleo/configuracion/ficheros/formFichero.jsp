<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFicheros"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFicheros bean = (VistaFicheros) uvdatos.getVistas().get(VistaFicheros.class.getName());
%>


<div class="bolsa-empleo">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	
	%>
	
	<h2>Nuevo documento</h2>
    
    <form id="subir_fichero" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    	<input type="hidden" name="<%= ControladorGestionFicheros.PARAM_ACCION %>" id="accion_formulario" value="" />
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="fichero_titulo">Título:</label>
	    		<input class="form-input-custom" id="fichero_titulo" name="<%= ControladorGestionFicheros.PARAM_TITULO %>"/>
	    	</div>
    	</div>
    	<div class="form-group-container col1">
	    	<div class="form-file">
				<label for="fichero_archivo">Fichero:</label>
				<input id="fichero_archivo" type="file" name="<%= ControladorGestionFicheros.PARAM_ARCHIVO %>"/>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-check">
	    		<label for="fichero_publico"><input type="checkbox" id="fichero_publico" name="<%= ControladorGestionFicheros.PARAM_PUBLICO %>"/>Público</label>
	    	</div>
    	</div>
    	<div class="form-btn">
    		<input id="fichero_enviar" type="submit" name="<%= ControladorGestionFicheros.PARAM_ENVIAR %>" value="Subir fichero"/>
    	</div>
    </form>
    
</div>

<script>

	function subirFichero(event, submit_input) {
		event.preventDefault();
		
		input_accion = document.getElementById("accion_formulario");
		input_accion.value = '<%= ControladorGestionFicheros.ACCION_SUBIR_FICHERO %>';
		submit_input.form.submit();
	}

	$(document).ready(function() {		
		document.getElementById("fichero_enviar").addEventListener("click", function(event) {
			subirFichero(event, this);
		});		
	});

</script>