<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorAfinidades" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAfinidades" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

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
	
	<p>Las etiquetas en <b>negrita</b> corresponden a campos de relleno obligatorio</p>
		
	<form id="afinidad_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorAfinidades.PARAM_ACCION %>" id="accion_formulario" value="<%= afinidad != null ? ControladorAfinidades.ACCION_MODIFICAR_AFINIDAD : ControladorAfinidades.ACCION_AGREGAR_AFINIDAD %>" />
		<input type="hidden" name="<%= ControladorAfinidades.PARAM_ID %>" id="afinidad_id" value="<%= afinidad != null ? afinidad.getCodNum() : "" %>" />		
		<div class="form-group-container col1">
    		<div class="form-group">
    			<label for="descripcion" class="bold-label">Descripción: </label>
    			<input id="descripcion"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorAfinidades.PARAM_DESCRIPCION %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorAfinidades.PARAM_DESCRIPCION, afinidad != null ? afinidad.getDescripcion() : "") %>"
    				   required/>
    		</div>
    	</div>

    	<div class="form-group-container col2">
	   		<div class="form-group">
    			<label for="codigo" class="bold-label">Código: </label>
    			<input id="codigo" 
    				   class="form-input-custom" 
    				   type="text"    				   
    				   name="<%= ControladorAfinidades.PARAM_CODIGO %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorAfinidades.PARAM_CODIGO, afinidad != null ? afinidad.getCodigo() : "") %>"
    				   required/>
    		</div>
    		<div class="form-group">
    			<label for="modulacion" class="bold-label">Modulación: </label>
    			<input id="modulacion" 
    				   class="form-input-custom" 
    				   type="text"    				   
    				   name="<%= ControladorAfinidades.PARAM_MODULACION %>" 
    				   value="<%= afinidad != null ? afinidad.getModulacion() : "" %>"
    				   required>
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