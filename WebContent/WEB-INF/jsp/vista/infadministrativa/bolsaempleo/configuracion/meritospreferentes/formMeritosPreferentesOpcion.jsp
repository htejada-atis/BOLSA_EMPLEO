<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritosPreferentes bean = (VistaMeritosPreferentes) uvdatos.getVistas().get(VistaMeritosPreferentes.class.getName());
MeritoPreferente merito = bean.getMeritoPreferente();
%>

<div class="bolsa-empleo meritos-preferentes-form">

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
	
	<h2>Nueva opción para: <%= merito.getDescripcion() %></h2>
		
	<form id="merito_preferente_opcion_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorMeritosPreferentes.PARAM_ACCION %>" id="accion_formulario" 
    		   value="<%= ControladorMeritosPreferentes.ACCION_NUEVA_OPCION_CONFIRM %>" />
		<input type="hidden" name="<%= ControladorMeritosPreferentes.PARAM_ID %>" id="merito_preferente_id"
		       value="<%= merito.getCodNum() %>" />
		       
		<div class="form-group-container col1">
			<div class="form-group">
    			<label for="codigo">Nombre: </label>
    			<input id="codigo"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorMeritosPreferentes.PARAM_MERITO_OPCION_NOMBRE %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_OPCION_NOMBRE, "") %>"/>
    		</div>
    	</div>
    	
    	<div class="form-group-container col2">
			<div class="form-group">
    			<label for="bloque_calculo_valor_merito_factor">Factor: </label>
    			<input id="bloque_calculo_valor_merito_factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_OPCION_FACTOR%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_OPCION_FACTOR, "")%>"/>
    		</div>		
    	</div>
    	
    	<div class="form-btn">
    		<input id="enviar" type="submit" value="Nueva opción" />
    	</div>
    </form>
</div>

<script>
	
</script>