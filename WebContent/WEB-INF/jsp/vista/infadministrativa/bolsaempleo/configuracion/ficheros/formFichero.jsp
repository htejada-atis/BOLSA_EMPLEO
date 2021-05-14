<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFicheros"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFicheros bean = (VistaFicheros) uvdatos.getVistas().get(VistaFicheros.class.getName());

String tituloValue = BolsaEmpleoUtils.getParamForm(request, ControladorGestionFicheros.PARAM_TITULO, "");
%>


<div class="bolsa-empleo">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Nuevo documento</h2>
	
	<p>Las etiquetas en <b>negrita</b> corresponden a campos de relleno obligatorio</p>
    
    <form id="subir_fichero" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    	<input type="hidden" name="<%= ControladorGestionFicheros.PARAM_ACCION %>" id="accion_formulario" 
    		value="<%= ControladorGestionFicheros.ACCION_SUBIR_FICHERO %>" />
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="fichero_titulo">Título:</label>
	    		<input class="form-input-custom" id="fichero_titulo" name="<%= ControladorGestionFicheros.PARAM_TITULO %>" value="<%= tituloValue %>"/>
	    	</div>
    	</div>
    	<div class="form-group-container col1">
	    	<div class="form-file">
				<label for="fichero_archivo" class="bold-label">Fichero:</label>
				<input id="fichero_archivo" type="file" name="<%= ControladorGestionFicheros.PARAM_ARCHIVO %>" required/>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-check">
	    		<label for="fichero_publico" class="bold-label"><input type="checkbox" id="fichero_publico" name="<%= ControladorGestionFicheros.PARAM_PUBLICO %>"/>Público</label>
	    	</div>
    	</div>
    	<div class="form-btn">
    		<input id="fichero_enviar" type="submit" name="<%= ControladorGestionFicheros.PARAM_ENVIAR %>" value="Subir fichero"/>
    	</div>
    </form>
    
</div>