<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorInicio"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaInicio" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Fichero" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaInicio bean = (VistaInicio)uvdatos.getVistas().get(VistaInicio.class.getName());
%>

<div class="bolsa-empleo">
	<h2>Documentos de interés</h2>
	
	<div class="nav-bolsa-empleo">
        <%@ include file="includes/menu.jsp" %>
    </div>
    
    <div class="lista-bolsa-empleo">
    	<ul>
    		<% for(Fichero fichero: bean.getFicheros()) { 
    			String link = ControladorGestionFicheros.URL_PATTERN_FILES + "?a=" + ControladorGestionFicheros.ACCION_DESCARGAR_FICHERO + "&" + ControladorGestionFicheros.PARAM_ID + "=" + fichero.getCodNum();
    			String titulo = "";
    			
    			if(fichero.getTitulo() != null) {
    				titulo = fichero.getTitulo();
    			} else {
    				titulo = link;
    			}
    		%>
				<li><a href="<%= link %>" target="_blank"><%= titulo %></a></li>
			<% } %>
    	</ul>
    </div>
	
</div>