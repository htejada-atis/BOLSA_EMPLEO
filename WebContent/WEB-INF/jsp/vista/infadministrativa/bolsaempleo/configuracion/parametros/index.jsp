<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorParametrosConfiguracion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaParametrosConfiguracion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaParametrosConfiguracion bean = (VistaParametrosConfiguracion) uvdatos.getVistas().get(VistaParametrosConfiguracion.class.getName());
%>

<div class="bolsa-empleo parametros-form">

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
	
	<div class="titulo-bolsa-empleo" style="margin-top:1rem;">
		<h2>Parámetros de configuración</h2>
	</div>
	
	 <p>Las etiquetas en <b>negrita</b> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorParametrosConfiguracion.PARAM_ACCION %>" id="accion_formulario" value="<%= ControladorParametrosConfiguracion.ACCION_EDITAR_PARAMETROS %>" />
		
		<div class="form-group">
			<% for(ParametrosConfiguracion param : bean.getParametros()){%>
				<div class="titulo-bolsa-empleo" style="margin-top:1rem;">
					<h3><%=param.getNombre().split("[.]")[1] %></h3>
				</div>
			
				<div class="form-group-container col2">
					<div class="form-group">
	   					<div class="form-group">
    						<label for="nombre" class="bold-label">Nombre: </label>
    						<input class="form-input-custom" id="nombre" type="text" name="<%= param.getNombre() + ControladorParametrosConfiguracion.PARAM_NOMBRE %>" 
    								value="<%= param.getNombre() %>"
    								disabled
    								required/>
    					</div>
	   					<div class="form-group">
    						<label for="nombre" class="bold-label">Valor: </label>
    						<input class="form-input-custom" id="valor" type="text" name="<%= param.getNombre() + ControladorParametrosConfiguracion.PARAM_VALOR%>" 
    								value="<%= param.getValor() %>"
    								required/>
    					</div>
    				</div>
		
					<div class="form-group">
    					<label for="razon_exclusion">Descripción: </label>
    					<textarea class="params form-input-custom" id="descripcion" name="<%= param.getNombre() + ControladorParametrosConfiguracion.PARAM_DESCRIPCION %>" rows="6" cols="60"><%=param.getDescripcion() != null ? param.getDescripcion() : ""%></textarea>
   					</div>
   				</div>
			<% } %>
		</div>
		
		<div class="form-btn">
    		<input id="parametro_enviar" type="submit" name="<%= ControladorParametrosConfiguracion.PARAM_EDITAR_PARAMETROS %> " value="Guardar Parametros"/>
    	</div>
		
    </form>

</div>