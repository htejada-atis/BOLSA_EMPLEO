<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEstadoBolsas"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaEstadoBolsas bean = (VistaEstadoBolsas) uvdatos.getVistas().get(VistaEstadoBolsas.class.getName());
%>

<div class='bolsas'>
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } else { %>
	<h2>Estado de las bolsas</h2>
	
	<table class="bluetable">
		<tr>
			<th scope="col" style="width:5%" title="Id de la convocatoria">Id</th>
			<th scope="col" style="width:25%">Area</th>
			<th scope="col" style="width:10%">Estado</th>
			<th scope="col" style="width:15%">Actualizada</th>
			<th scope="col" style="width:15%">Bloqueo</th>
			<th scope="col" style="width:30%">Desbloqueo</th>
			<th scope="col" style="width:15%">Baremable</th>
		</tr>
		
		<% for(Bolsa bolsa:bean.getBolsasEmpleo()){ %>
			<tr id="row_<%=bolsa.getIdBolsa()%>">
				<td><%=bolsa.getIdBolsa()%></td>
				<td><%=EscapaHTML.escapaHTML(bolsa.getArea().getNombre())%></td>
				<td><%=bolsa.getEstado()%></td>
				<td><%=bolsa.getFechaActualizacion() != null ? Formateador.formatoFecha(bolsa.getFechaActualizacion(), Formateador.FORMATO_FECHA_DDMMYYYY) : ""%></td>
				<td><%=bolsa.getFechaBloqueo() != null ? Formateador.formatoFecha(bolsa.getFechaBloqueo(), Formateador.FORMATO_FECHA_DDMMYYYY) : ""%></td>
				<td><%=bolsa.getFechaDesBloqueo() != null ? Formateador.formatoFecha(bolsa.getFechaDesBloqueo(), Formateador.FORMATO_FECHA_DDMMYYYY) : ""%></td>				
				<td><%=bolsa.getBaremable()%></td>
			</tr>
		<% } %>
	</table>
	<% } %>
</div>
	