<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
%>

<div class="bolsa-empleo">

	<% if (session.getAttribute(ControladorMisSolicitudes.MENSAJE_ENVIADO) != null) { %>
		<div id="exito" class="success">
			<%= session.getAttribute(ControladorMisSolicitudes.MENSAJE_ENVIADO) %>
		</div>
	<% 
			session.removeAttribute(ControladorMisSolicitudes.MENSAJE_ENVIADO);
		} 
	%>

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
		<h2>Solicitudes</h2>
    
	    <p>A continuación se muestran las convocatorias disponibles para poder introducir méritos y poder seleccionar las áreas en las que desee participar.</p>
	</div>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col"	style="width:30%">Descripción</th>
			<th scope="col"	style="width:10%">Fecha cierre</th>
			<th scope="col"	style="width:10%">Estado convocatoria</th>			
			<th scope="col"	style="width:50%">Solicitud</th>			
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>

<script>

	$(document).ready(function() {
		var renderSolicitud = function(row) {
			var html = '';
			var solicitudNoCreada = row.codNum == 0;
			var solicitudAbierta = row.estado == '<%= ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA %>';
			var solicitudCerrada = row.estado == '<%= ModeloSolicitud.SOLICITUD_ESTADO_CERRADA %>';
			var convocatoriaAbierta = row.convocatoria.estado == '<%= ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA %>';
			var convocatoriaCerrada = row.convocatoria.estado == '<%= ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA %>';
			var btnCrear = '<button class="create-solicitud pointer" data-rowid="' + row.convocatoria.codNum + '" title="Crear una nueva solicitud para: ' + row.convocatoria.descripcion + '">Abrir una solicitud</button>';
			var btnConsultar = '<button class="consultar-solicitud pointer" data-rowid="' + row.codNum + '" title="Consultar solicitud para: ' + row.convocatoria.descripcion + '">Consultar solicitud</button>';
			var btnDescargarPDF = '<button class="descargar-pdf-solicitud pointer" data-rowid="' + row.codNum + '" title="Descargar pdf solicitud para: ' + row.convocatoria.descripcion + '">Descargar PDF solicitud</button>';
			
			if (convocatoriaAbierta && solicitudNoCreada) {
				// convocatoria abierta, solicitud no creada
				console.log("convocatoria abierta, solicitud no creada");
				html = 'Actualmente no tiene ninguna solicitud en esta convocatoria. ' + btnCrear;
			} else if (convocatoriaAbierta && solicitudAbierta) {
				// convocatoria abierta, solicitud abierta
				console.log("convocatoria abierta, solicitud abierta");
				html = 'Tiene un solicitud abierta. No olvide CERRAR antes de la fecha de cierre de la convocatoria. ' + btnConsultar;
			} else if (convocatoriaCerrada && solicitudNoCreada) {
				// convocatoria cerrada, solicitud no creada
				console.log("convocatoria cerrada, solicitud no creada");
				html = 'No tiene ninguna solicitud en esta convocatoria.';
			} else if (convocatoriaCerrada && solicitudAbierta) {
				// convocatoria cerrada, solicitud abierta
				console.log("convocatoria cerrada, solicitud abierta");
				html = 'No ha validado la solicitud antes de la fecha de cierre de la convocatoria.';
			} else if ((convocatoriaAbierta || convocatoriaCerrada) && solicitudCerrada) {
				// convocatoria abierta o cerrada y solicitud cerrada
				console.log("convocatoria abierta o cerrada y solicitud cerrada");
				html = 'Solicitud cerrada. ' + btnDescargarPDF;
			} 
			
			return html
		};
		
		var createSolicitud = function() {
			var id = $(this).data('rowid')
			var params = {
				'<%= ControladorMisSolicitudes.PARAM_ACCION %>': '<%= ControladorMisSolicitudes.ACCION_CREAR_SOLICITUD %>', 
				'<%= ControladorMisSolicitudes.PARAM_CONVOCATORIA_ID %>': id
			};
			Atis.sendForm('<%= request.getRequestURI() %>', params);			
		};
		
		var consultarSolicitud = function() {
			var id = $(this).data('rowid')
			var params = {
				'<%= ControladorMisSolicitudes.PARAM_ACCION %>': '<%= ControladorMisSolicitudes.ACCION_CONSULTAR_SOLICITUD %>', 
				'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': id
			};
			Atis.sendForm('<%= request.getRequestURI() %>', params);
		};
		
		var descargarPDFSolicitud = function() {
			var id = $(this).data('rowid')
			window.open("<%= request.getRequestURI() %>"
			        	+ "?a=<%= ControladorMisSolicitudes.ACCION_DESCARGAR_PDF %>&<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>=" + id);
		}
					
		var beforeRender = function(table) {
			$('button.create-solicitud', table.node).off('click', createSolicitud);
			$('button.consultar-solicitud', table.node).off('click', consultarSolicitud);
			$('button.descargar-pdf-solicitud', table.node).off('click', descargarPDFSolicitud);
		};
		
		var afterRender = function(table) {
			$('button.create-solicitud', table.node).on('click', createSolicitud)
			$('button.consultar-solicitud', table.node).on('click', consultarSolicitud);
			$('button.descargar-pdf-solicitud', table.node).on('click', descargarPDFSolicitud);
		};
		
		var table_titulaciones = new Atis.DataTable('#table', {
		    "ajax": { url: "<%=  ControladorMisSolicitudes.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "action": "<%=ControladorMisSolicitudes.ACCION_DATATABLE_SOLICITUDES%>",
		    "beforeRender": beforeRender,
		    "afterRender": afterRender,
		    "columns": [
		    	{'data': 'convocatoria.descripcion', order: {'active': false}},
		        {'data': 'convocatoria.fechaCierre', order: {'active': false}},
		        {'data': 'convocatoria.estado', order: {'active': false}},
		        {'data': 'codNum', order: {'active': false}, render: renderSolicitud},		        
		    ],
		});		
	});
	
</script>
