<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.beans.Rol" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorAfinidades" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaAfinidades" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaAfinidades bean = (VistaAfinidades) uvdatos.getVistas().get(VistaAfinidades.class.getName());
Afinidad afinidad = bean.getAfinidad();
%>

<div class="bolsa-empleo afinidad-form">
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
	
	<h2><%= afinidad != null ? "Editar afinidad" : "Nueva afinidad" %></h2>
		
	<form id="afinidad_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorAfinidades.PARAM_ACCION %>" id="accion_formulario" value="<%= afinidad != null ? ControladorAfinidades.ACCION_MODIFICAR_AFINIDAD : ControladorAfinidades.ACCION_AGREGAR_AFINIDAD %>" />
		<input type="hidden" name="<%= ControladorAfinidades.PARAM_ID %>" id="afinidad_id" value="<%= afinidad != null ? afinidad.getCodNum() : "" %>" />		
		<div class="form-group-container col1">
    		<div class="form-group">
    			<label for="descripcion">Descripción: </label>
    			<input id="descripcion"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorAfinidades.PARAM_DESCRIPCION %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorAfinidades.PARAM_DESCRIPCION, afinidad != null ? afinidad.getDescripcion() : "") %>"/>
    		</div>
    	</div>

    	<div class="form-group-container col2">
	   		<div class="form-group">
    			<label for="codigo">Código: </label>
    			<input id="codigo" 
    				   class="form-input-custom" 
    				   type="text"    				   
    				   name="<%= ControladorAfinidades.PARAM_CODIGO %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorAfinidades.PARAM_CODIGO, afinidad != null ? afinidad.getCodigo() : "") %>"/>
    		</div>
    		<div class="form-group">
    			<label for="modulacion">Modulación </label>
    			<input id="modulacion" 
    				   class="form-input-custom" 
    				   type="text"    				   
    				   name="<%= ControladorAfinidades.PARAM_MODULACION %>" 
    				   value="<%= afinidad != null ? afinidad.getModulacion() : "" %>"/>
    		</div>
    	</div>
    	
    	<div class="form-btn">
    		<input id="convocatoria_enviar" 
    			   type="submit" 
    			   name="<%= ControladorAfinidades.PARAM_ACCION_ENVIAR %>" 
    			   value="<%= afinidad != null ? "Guardar" : "Nueva afinidad" %>" />
    	</div>
    </form>
</div>

<script>

	$(document).ready(function() {				
		document.getElementById("convocatoria_enviar").addEventListener("click", function(event) {
			enviarConvocatoria(event, this);
		});
	})
	
	function enviarConvocatoria(event, submit_input) {
		event.preventDefault();
	
		<% if(bean.getAfinidad() != null) { %>
				input_accion = document.getElementById("accion_formulario");
				input_accion.value = '<%= ControladorAfinidades.ACCION_MODIFICAR_AFINIDAD %>';
				input_id = document.getElementById("afinidad_id");
				input_id.value = '<%= bean.getAfinidad().getCodNum() %>';
		<%}%>
	
		submit_input.form.submit();
	}


</script>