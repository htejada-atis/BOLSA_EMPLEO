<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());

String codigo = bean.getApartadoBaremacion().getCodigo();
String nombre = "";
%>


<div class="bolsa-empleo">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	
		if(bean.getApartadoBaremacion().getCodNum() != null) {
			nombre = bean.getApartadoBaremacion().getNombre();
	    	out.print("<h2>Editar apartado</h2>");
		} else {
		    out.print("<h2>Nuevo apartado</h2>");
		}
	%>
    
    
    <form id="actualizar_apartado" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ACCION %>" id="accion_formulario" value="" />
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_APARTADO%>" id="apartado_id" value="" />
		<div class="form-group-container col2">
	    	<div class="form-group">
	    		<label for="apartado_codigo">Código:</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_APARTADO_CODIGO %>" id="apartado_codigo" value="<%= codigo %>"/>
	    	</div>
	    	
    	</div>
    	<div class="form-group-container col2">
    		<div class="form-group">
	    		<label for="apartado_nombre">Nombre:</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE %>" id="apartado_nombre" value="<%= nombre %>"/>
	    	</div>
    	</div>
    	<div class="form-btn">
    		<input id="apartado_enviar" type="submit" name="<%= ControladorItemsBaremacion.PARAM_ENVIAR %>" value="<%= bean.getApartadoBaremacion().getCodNum() != null ? "Guardar cambios" : "Insertar apartado" %>"/>
    	</div>
    </form>
    
</div>

<script>

	function enviarApartadoBaremacion(event, submit_input) {
		event.preventDefault();
		
		<% if(bean.getApartadoBaremacion().getCodNum() != null) { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorItemsBaremacion.ACCION_EDITAR_APARTADO %>';
			input_id = document.getElementById("apartado_id");
			input_id.value = '<%= bean.getApartadoBaremacion().getCodNum() %>';
		<% } else { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO %>';
		<% } %>
		
		submit_input.form.submit();
	}

	$(document).ready(function() {
		
		document.getElementById("apartado_enviar").addEventListener("click", function(event) {
			enviarApartadoBaremacion(event, this);
		});
	});

</script>