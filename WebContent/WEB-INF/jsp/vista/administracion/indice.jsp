<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Collections" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.controlador.administracion.ControladorIndice" %>

<%
@SuppressWarnings("unchecked")
HashMap<String, String> informacion = (HashMap<String, String>)request.getAttribute(ControladorIndice.INFORMACION);
Vector<String> claves = new Vector<String>(informacion.keySet());
Collections.sort(claves);
%>
<h2> Informaci&oacute;n del servidor </h2>
<div id="itemsmainContent">
	<table style="height: 300px;" class="bluetable">
		<caption>Información del servidor</caption>
		<thead>
			<tr>
				<th scope="col" style="width:20%">Campo</th>
				<th scope="col" style="width:80%">Valor</th>
			</tr>
		</thead>
		<tbody>
			<% for(String clave : claves) {	%>
				<tr><td><%= EscapaHTML.escapa(clave) %></td><td><%= EscapaHTML.escapa(informacion.get(clave)) %></td></tr>
			<% } %>
		</tbody>
	</table>
</div>