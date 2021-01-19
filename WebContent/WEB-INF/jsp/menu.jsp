<%@ page import="es.ujaen.uvirtual.beans.*" %>
<%
Usuario usuario = (Usuario)request.getAttribute("Usuario");
%>
	
	<div id="menu">
		Menu personalizado para <%= usuario.getUid() %> 
	</div>
	