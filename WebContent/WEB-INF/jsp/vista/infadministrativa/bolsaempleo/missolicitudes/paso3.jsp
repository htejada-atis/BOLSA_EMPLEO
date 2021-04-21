<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
%>

<div class="bolsa-empleo">	
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError()   %>
		</div>
	<% } %>
	
	<div class="titulo-bolsa-empleo">
		<h2>Paso 3: Resumen de solicitud y confirmación</h2>
		<h3><%= bean.getSolicitud().getConvocatoria().getDescripcion() %></h3>
		
		<p>Revise su solicitud para la convocatoria <%= bean.getSolicitud().getConvocatoria().getDescripcion() %>. 
		Una vez confirmada no podrá ser editada</p>
	</div>	
</div>

<script>

	$(document).ready(function() {
		
	});
	
</script>
