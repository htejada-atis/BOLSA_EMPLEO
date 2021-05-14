<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.Vista"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
Vista bean = null;

for(String clase : uvdatos.getVistas().keySet()) {
	if (clase.startsWith("es.ujaen.uvirtual.modulo.bolsaempleo.vistas")) {
		bean = uvdatos.getVistas().get(clase);
		break;
	}
}
%>

<div class='bolsa-empleo'>
	<h2>Error</h2>
	
	<% if (bean != null && bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } %>	
</div>
