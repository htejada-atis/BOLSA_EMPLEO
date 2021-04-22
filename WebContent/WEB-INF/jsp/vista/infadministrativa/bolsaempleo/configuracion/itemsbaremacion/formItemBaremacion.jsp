<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());

ApartadoBaremacion apartado = bean.getApartadoBaremacion();
BloqueBaremacion bloque = bean.getBloqueBaremacion();
ItemBaremacion item = bean.getItemBaremacion();
String codigoCompleto = apartado.getCodigo() + "." + bloque.getCodigo() + "." + (item != null ? item.getCodigo() : bean.getUltimoCodigo());
%>


<div class="bolsa-empleo">
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } %>
	
	<h2><%= item != null ? "Editar item" : "Nuevo item" %></h2>
	
    <form id="actualizar_item" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ACCION %>" id="accion_formulario"
    		   value="<%= item != null ? ControladorItemsBaremacion.ACCION_EDITAR_ITEM_CONFIRM : ControladorItemsBaremacion.ACCION_AGREGAR_ITEM_CONFIRM %>" /> 
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_APARTADO %>" id="apartado_id" value="<%= apartado.getCodNum() %>" />			  
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE %>" id="bloque_id" value="<%= bloque.getCodNum() %>" />
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ITEM %>" id="item_id" value="<%= item != null ? item.getCodNum() : "" %>" />
		
		<div class="form-group-container col1">
			<div class="form-group">
	    		<label for="bloque_apartado_codigo">Código completo</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO %>" id="item_bloque_codigo" value="<%= codigoCompleto %>" disabled/>
	    	</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<label for="bloque_codigo">Código</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_CODIGO %>" id="bloque_codigo" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_CODIGO, item != null ? item.getCodigo() : bean.getUltimoCodigo()) %>"/>
	    	</div>
	    	<div class="form-group">
	    		<label for="bloque_nombre">Nombre</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_NOMBRE %>" id="bloque_nombre" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_NOMBRE, item != null ? item.getNombre() : "") %>"/>
	    	</div>
		</div>
		<div class="form-btn">
    		<input id="item_enviar" type="submit" name="<%= ControladorItemsBaremacion.PARAM_ENVIAR %>" 
    			   value="<%= item != null ? "Guardar cambios" : "Insertar item" %>"/>
    	</div>    	
    </form>
</div>

<script>
	$(document).ready(function() {
		document.getElementById("item_codigo").addEventListener("input", function(event) {
			document.getElementById("item_bloque_codigo").value = <%= apartado.getCodigo() + "." + bloque.getCodigo() %>  + "." + this.value;
		});
	});

</script>