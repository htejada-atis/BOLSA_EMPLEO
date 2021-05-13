<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorInicio"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaInicio" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaInicio bean = (VistaInicio)uvdatos.getVistas().get(VistaInicio.class.getName());
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

	<h2>Documentos de interés</h2>
	
	<div class="nav-bolsa-empleo">
        <%@ include file="includes/menu.jsp" %>
    </div>
    
    <div class="lista-bolsa-empleo">
    	<ul>
    		<% 
    			if(bean.getFicheros().size() > 0) {
    				for(Fichero fichero: bean.getFicheros()) { 
    	    			String link = (fichero.isPublico() ? ControladorInicio.URL_PATTERN_FILES_PUBLICA : ControladorInicio.URL_PATTERN_FILES_PRIVADA)
    	    					+ "?a=" + ControladorInicio.ACCION_DESCARGAR_FICHERO + "&" + ControladorInicio.PARAM_FICHERO
    	    					+ "=" + fichero.getCodNum();
    	    			String titulo = "";
    	    			
    	    			if(fichero.getTitulo() != null) {
    	    				titulo = fichero.getTitulo();
    	    			} else {
    	    				titulo = link;
    	    			}
        		%>
    				<li><a href="<%= link %>" target="_blank"><%= titulo %></a></li>
    			<% }
    			} else { %>
    				<li><p>No hay documentos disponibles.</p></li>
    		 <% }
    		%>
    			
    	</ul>
    </div>
	
</div>