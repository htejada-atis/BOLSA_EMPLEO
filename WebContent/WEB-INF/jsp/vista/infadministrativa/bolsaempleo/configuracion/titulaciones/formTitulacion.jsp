<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulaciones bean = (VistaTitulaciones) uvdatos.getVistas().get(VistaTitulaciones.class.getName());
%>


<div class="bolsa-empleo">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<%
		
		String nombre = "";
		
		if(bean.getTitulacion() != null) {
			nombre = bean.getTitulacion().getNombre();
	    	out.print("<h2>Editar titulación</h2>");
		} else {
		    out.print("<h2>Nueva titulación</h2>");
		}
		
	%>
	
	<p>Las etiquetas en <b>negrita</b> corresponden a campos de relleno obligatorio</p>
	
    <form id="agregar_titulacion" class="be-form" method="post" action="<%=request.getRequestURI()%>">
    	<input type="hidden" name="<%= ControladorGestionTitulaciones.PARAM_ACCION %>" id="accion_formulario" 
    			value="<%= bean.getTitulacion() != null ? ControladorGestionTitulaciones.ACCION_EDITAR_TITULACION : ControladorGestionTitulaciones.ACCION_AGREGAR_TITULACION %>" />
    	<input type="hidden" name="<%= ControladorGestionTitulaciones.PARAM_ID%>" id="titulacion_id" 
    			value="<%= bean.getTitulacion() != null ? bean.getTitulacion().getCodNum() : "" %>" />
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="titulacion_nombre" class="bold-label">Nombre</label>
	    		<input class="form-input-custom" id="titulacion_nombre" name="<%=ControladorGestionTitulaciones.PARAM_NOMBRE%>" value="<%= nombre %>" required/>
	    	</div>
    	</div>
    	<div class="form-btn">
    		<input id="titulacion_enviar" type="submit" name="<%=ControladorGestionTitulaciones.PARAM_ENVIAR%>"
    		 value="<%= bean.getTitulacion() != null ? "Guardar cambios" : "Insertar titulación" %>"/>
    	</div>
    </form>
    
</div>