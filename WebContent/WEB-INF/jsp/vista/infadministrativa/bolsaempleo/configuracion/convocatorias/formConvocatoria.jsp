<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorConvocatorias" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConvocatorias" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConvocatorias bean = (VistaConvocatorias) uvdatos.getVistas().get(VistaConvocatorias.class.getName());
Convocatoria convocatoria = bean.getConvocatoria();
%>

<div class="bolsa-empleo convocatoria-form">
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
	
	<h2><%= convocatoria != null ? "Editar convocatoria" : "Nueva convocatoria" %></h2>
	
	<p>Se creará un nueva convocatoria con el estado cerrada, para así revisar antes de abrirla: áreas a baremar, items de baremación, titulaciones y otras configuraciones.</p>
	
	<form id="convocatoria_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorConvocatorias.PARAM_ACCION %>" id="accion_formulario" value="<%= convocatoria != null ? ControladorConvocatorias.ACCION_MODIFICAR_CONVOCATORIA : ControladorConvocatorias.ACCION_AGREGAR_CONVOCATORIA %>" />
		<input type="hidden" name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>" id="convocatoria_id" value="<%= convocatoria != null ? convocatoria.getCodNum() : "" %>" />		
		<div class="form-group-container col1">
    		<div class="form-group">
    			<label for="descripcion">Descripción convocatoria: </label>
    			<input id="descripcion"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_DESCRIPCION %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorConvocatorias.PARAM_CONVOCATORIA_DESCRIPCION, convocatoria != null ? convocatoria.getDescripcion() : "") %>"
    				   required/>
    		</div>
    	</div>
    	<div class="form-group-container col2">
	   		<div class="form-group">
    			<label for="fechaCierre">Fecha cierre: </label>
    			<input id="fechaCierre"
    				   class="form-input-custom"
    				   type="text"
    				   autocomplete="off" 
    				   name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_FECHACIERRE %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorConvocatorias.PARAM_CONVOCATORIA_FECHACIERRE, convocatoria != null ? Formateador.formatoFecha(convocatoria.getFechaCierre(), Formateador.FORMATO_FECHA_DDMMYYYY) : "") %>"
    				   required/>
    		</div>
    	</div>
    	
    	<div class="form-group-container col2">
	   		<div class="form-group">
    			<label for="numMaximoBolsas">Nº bolsa máximo de participación de candidato: </label>
    			<input id="numMaximoBolsas" 
    				   class="form-input-custom" 
    				   type="text"    				   
    				   name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorConvocatorias.PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO, convocatoria != null ? convocatoria.getNumBolsasMaximo().toString() : "5") %>"
    				   required/>
    		</div>
    		<div class="form-group">
    			<label for="numMeritosPorBloque">Nº méritos por bloque: </label>
    			<input id="numMeritosPorBloque" 
    				   class="form-input-custom" 
    				   type="text"    				   
    				   name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE %>" 
    				   value="<%= convocatoria != null ? convocatoria.getNumMeritosPorBloque() : "10" %>"
    				   required/>
    		</div>
    	</div>
    	
    	<div class="form-btn">
    		<input id="convocatoria_enviar" 
    			   type="submit" 
    			   name="<%= ControladorConvocatorias.PARAM_ACCION_ENVIAR %>" 
    			   value="<%= convocatoria != null ? "Guardar" : "Nueva convocatoria" %>"/>
    	</div>
    </form>
</div>

<script>

	$(document).ready(function() {		
		$("#fechaCierre").datepicker();		
	});


</script>