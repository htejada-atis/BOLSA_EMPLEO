<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());

String codigoApartado = bean.getBloqueBaremacion().getApartadoBaremacion().getCodigo();
String codigoBloque = bean.getBloqueBaremacion().getCodigo();
String codigoCompleto = codigoApartado + "." + codigoBloque;
String nombre = "";
%>


<div class="bolsa-empleo">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	
		
	
		if(bean.getBloqueBaremacion().getCodNum() != null) {
			nombre = bean.getBloqueBaremacion().getNombre();
	    	out.print("<h2>Editar bloque</h2>");
		} else {
		    out.print("<h2>Nuevo bloque</h2>");
		}
	%>
    
    
    <form id="actualizar_bloque" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ACCION %>" id="accion_formulario" value="" />
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE%>" id="bloque_id" value="" />
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_APARTADO%>" id="apartado_id" value="" />
		<div class="form-group">
    		<label for="bloque_apartado_codigo">Código completo</label>
    		<input type="text" name="<%= ControladorItemsBaremacion.PARAM_APARTADO_CODIGO %>" id="bloque_apartado_codigo" value="<%= codigoCompleto %>" readonly/>
    	</div>
    	<div class="form-group">
    		<label for="bloque_codigo">Código</label>
    		<input type="text" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO %>" id="bloque_codigo" value="<%= codigoBloque %>"/>
    	</div>
    	<div class="form-group">
    		<label for="bloque_nombre">Nombre</label>
    		<input type="text" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE_NOMBRE %>" id="bloque_nombre" value="<%= nombre %>"/>
    	</div>
    	<div class="form-btn">
    		<input id="bloque_enviar" type="submit" name="<%= ControladorItemsBaremacion.PARAM_ENVIAR %>" value="<%= bean.getBloqueBaremacion().getCodNum() != null ? "Guardar cambios" : "Insertar bloque" %>"/>
    	</div>
    </form>
    
</div>

<script>

	function enviarBloqueBaremacion(event, submit_input) {
		event.preventDefault();
		
		<% if(bean.getBloqueBaremacion().getCodNum() != null) { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorItemsBaremacion.ACCION_EDITAR_BLOQUE %>';
			input_id = document.getElementById("bloque_id");
			input_id.value = '<%= bean.getBloqueBaremacion().getCodNum() %>';
		<% } else { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorItemsBaremacion.ACCION_AGREGAR_BLOQUE %>';
			input_id = document.getElementById("apartado_id");
			input_id.value = '<%= bean.getBloqueBaremacion().getApartadoBaremacion().getCodNum() %>';
		<% } %>
		
		submit_input.form.submit();
	}

	$(document).ready(function() {
		
		document.getElementById("bloque_enviar").addEventListener("click", function(event) {
			enviarBloqueBaremacion(event, this);
		});
		
		document.getElementById("bloque_codigo").addEventListener("input", function(event) {
			document.getElementById("bloque_apartado_codigo").value = <%= codigoApartado %> + "." + this.value;
		});
		
	});

</script>