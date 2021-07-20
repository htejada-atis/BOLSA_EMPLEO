<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorAreasABaremar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConfiguracion"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConfiguracion bean = (VistaConfiguracion) uvdatos.getVistas().get(VistaConfiguracion.class.getName());
%>

<div class='bolsa-empleo'>
	<h2>Configuración de la bolsa de trabajo</h2>
	
	<p>Seleccione una opción del menú lateral. O pulse sobre los siguientes accesos directos.</p>
	
	<div class="form-group-container col3">
	<%	if (bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) { %>
	   	    <div class="form-group">
	   			<button id="btn_candidatos" class="link-btn">Candidatos</button>
	   		</div>
	   		<div class="form-group">
				<button id="btn_ficheros" class="link-btn">Ficheros</button>
	   		</div>
	   		<div class="form-group">
				<button id="btn_mensajeria" class="link-btn">Mensajería</button>
	   		</div>
   	<%	} else { %>
   			<div class="form-group">
				<button id="btn_evaluadores" class="link-btn">Evaluadores</button>
	   		</div>
   	<%	} %>
   	</div>
	
	<p class="ta-right">Versión: <%= bean.getVersion() %></p>
</div>

<script>

	$(document).ready(function() {
	
	<%	if (bean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) { %>
		document.getElementById("btn_candidatos").addEventListener("click", function() {
			window.location.replace("/srv/es/informacionadministrativa/bolsaempleo/configuracion/candidatos");
		});
		
		document.getElementById("btn_ficheros").addEventListener("click", function() {
			window.location.replace("/srv/es/informacionadministrativa/bolsaempleo/configuracion/ficheros");
		});
		
		document.getElementById("btn_mensajeria").addEventListener("click", function() {
			window.location.replace("/srv/es/informacionadministrativa/bolsaempleo/configuracion/mensajeria");
		});
	<%	} else { %>
		document.getElementById("btn_evaluadores").addEventListener("click", function() {
			window.location.replace("/srv/es/informacionadministrativa/bolsaempleo/configuracion/evaluadores");
		});
	<%	} %>
		
	});

</script>