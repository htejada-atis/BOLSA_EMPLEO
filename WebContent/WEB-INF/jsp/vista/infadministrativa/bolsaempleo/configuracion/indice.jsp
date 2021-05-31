<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorAreasABaremar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConfiguracion"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConfiguracion bean = (VistaConfiguracion) uvdatos.getVistas().get(VistaConfiguracion.class.getName());
%>

<div class='bolsa-empleo'>
	<h2>Configuración de la bolsa de trabajo</h2>
	
	<p>Seleccione una opción del menú lateral. O pulse sobre los siguientes accesos directos.</p>
	
	<div class="form-group-container col3">
   	    <div class="form-group">
   			<a class="link-btn" href="/srv/es/informacionadministrativa/bolsaempleo/configuracion/candidatos">Candidatos</a>
   		</div>
   		<div class="form-group">
			<a class="link-btn" href="/srv/es/informacionadministrativa/bolsaempleo/configuracion/ficheros">Ficheros</a>
   		</div>
   		<div class="form-group">
			<a class="link-btn" href="/srv/es/informacionadministrativa/bolsaempleo/configuracion/mensajeria">Mensajería</a>
   		</div>
   	</div>
	
	<p class="ta-right">Versión: <%= bean.getVersion() %></p>
</div>