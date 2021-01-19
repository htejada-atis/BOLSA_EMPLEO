
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.UVException" %>

<%
	UVException ex = (UVException) request.getAttribute(UVException.class.getName());
%>

<div id="itemsmainContent">
	<div id="divPrincipal" style="font-size: 1.4em; margin: 4px;">
		<%= EscapaHTML.escapa(ex.getMensajeUsuario(false)) %>
	</div>
</div>
