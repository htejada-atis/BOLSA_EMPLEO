<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorGestionTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulaciones bean = (VistaTitulaciones) uvdatos.getVistas().get(VistaTitulaciones.class.getName());
%>


<div class="bolsa-empleo">

	<%
		if(bean.getMensajesDeError().size() > 0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
		
		String nombre = "";
		
		if(bean.getTitulacion() != null) {
			nombre = bean.getTitulacion().getNombre();
	    	out.print("<h2>Editar titulación</h2>");
		} else {
		    out.print("<h2>Nueva titulación</h2>");
		}
		
	%>
	
    <form id="agregar_titulacion" class="be-form" method="post" action="<%=request.getRequestURI()%>">
    	<input type="hidden" name="<%= ControladorGestionTitulaciones.PARAM_ACCION %>" id="accion_formulario" value="" />
    	<input type="hidden" name="<%= ControladorGestionTitulaciones.PARAM_ID%>" id="titulacion_id" value="" />
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="titulacion_nombre">Nombre</label>
	    		<input class="form-input-custom" id="titulacion_nombre" name="<%=ControladorGestionTitulaciones.PARAM_NOMBRE%>" value="<%= nombre %>"/>
	    	</div>
    	</div>
    	<div class="form-btn">
    		<input id="titulacion_enviar" type="submit" name="<%=ControladorGestionTitulaciones.PARAM_ENVIAR%>"
    		 value="<%= bean.getTitulacion() != null ? "Guardar cambios" : "Insertar titulación" %>"/>
    	</div>
    </form>
    
</div>

<script>

	function enviarTitulacion(event, submit_input) {
		event.preventDefault();
		
		<% if(bean.getTitulacion() != null) { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorGestionTitulaciones.ACCION_EDITAR_TITULACION %>';
			input_id = document.getElementById("titulacion_id");
			input_id.value = '<%= bean.getTitulacion().getCodNum() %>';
		<% } else { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorGestionTitulaciones.ACCION_AGREGAR_TITULACION %>';
		<% } %>
		
		submit_input.form.submit();
	}

	$(document).ready(function() {
		
		document.getElementById("titulacion_enviar").addEventListener("click", function(event) {
			enviarTitulacion(event, this);
		});
		
	});

</script>