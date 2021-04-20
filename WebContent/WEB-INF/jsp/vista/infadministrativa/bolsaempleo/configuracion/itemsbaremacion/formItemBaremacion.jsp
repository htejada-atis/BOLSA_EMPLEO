<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());

String codigoApartado = bean.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo();
String codigoBloque = bean.getItemBaremacion().getBloqueBaremacion().getCodigo();
String codigoItem = bean.getItemBaremacion().getCodigo();
String codigoCompleto = codigoApartado + "." + codigoBloque + "." + bean.getItemBaremacion().getCodigo();
String nombre = "";

%>


<div class="bolsa-empleo">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	
		if(bean.getItemBaremacion().getCodNum() != null) {
			nombre = bean.getItemBaremacion().getNombre();
	    	out.print("<h2>Editar item</h2>");
		} else {
		    out.print("<h2>Nuevo item</h2>");
		}
	%>
    
    
    <form id="actualizar_item" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ACCION %>" id="accion_formulario" value="" />
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ITEM%>" id="item_id" value="" />
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE%>" id="bloque_id" value="" />
		<div class="form-group">
    		<label for="item_bloque_codigo">Código completo</label>
    		<input type="text" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO %>" id="item_bloque_codigo" value="<%= codigoCompleto %>" disabled/>
    	</div>
    	<div class="form-group">
    		<label for="item_codigo">Código</label>
    		<input type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_CODIGO %>" id="item_codigo" value="<%= codigoItem %>"/>
    	</div>
    	<div class="form-group">
    		<label for="item_nombre">Nombre</label>
    		<input type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_NOMBRE %>" id="item_nombre" value="<%= nombre %>"/>
    	</div>
    	<div class="form-btn">
    		<input id="item_enviar" type="submit" name="<%= ControladorItemsBaremacion.PARAM_ENVIAR %>" value="<%= bean.getItemBaremacion().getCodNum() != null ? "Guardar cambios" : "Insertar item" %>"/>
    	</div>
    </form>
    
</div>

<script>

	function enviarItemBaremacion(event, submit_input) {
		event.preventDefault();
		
		<% if (bean.getItemBaremacion().getCodNum() != null) { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorItemsBaremacion.ACCION_EDITAR_ITEM %>';
			input_id = document.getElementById("item_id");
			input_id.value = '<%= bean.getItemBaremacion().getCodNum() %>';
		<% } else { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorItemsBaremacion.ACCION_AGREGAR_ITEM %>';
			input_id = document.getElementById("bloque_id");
			input_id.value = '<%= bean.getItemBaremacion().getBloqueBaremacion().getCodNum() %>';
		<% } %>
		
		submit_input.form.submit();
	}

	$(document).ready(function() {
		
		document.getElementById("item_enviar").addEventListener("click", function(event) {
			enviarItemBaremacion(event, this);
		});
		
		document.getElementById("item_codigo").addEventListener("input", function(event) {
			document.getElementById("item_bloque_codigo").value = <%= codigoApartado %> + "." + <%= codigoBloque %> + "." + this.value;
		});
		
	});

</script>