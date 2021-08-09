<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorPlazasOfertadas" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlazasOfertadas" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaPlazasOfertadas bean = (VistaPlazasOfertadas) uvdatos.getVistas().get(VistaPlazasOfertadas.class.getName());
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Plazas ofertadas</h2>
	</div>
	
	<table class="bluetable bolsaempleo" id="tablePlazasOfertadas">
		<tr>
			<th scope="col" style="width:100%">Área</th>
			<th scope="col" style="width:95px">Estado</th>
			<th scope="col" style="width:100px">Fecha fin oferta</th>
			<th scope="col" style="width:100px">Confirmación</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>
	
<script>
$(document).ready(function() {
	
	var table = new Atis.DataTable('#tablePlazasOfertadas', {
		"ajax": { url: "<%= ControladorPlazasOfertadas.URL_PATTERN_AJAX %>" },
		"pageSize": 10,
		"filterable": true,
		"title": 'Plazas ofertadas',
		"action": "<%= ControladorPlazasOfertadas.ACCION_DATATABLE_PLAZAS_OFERTADAS %>",
		"clickable": {'onClick': function(row) {
			var params = {
					'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_SELECCIONAR_PLAZA_OFERTADA %>', 
					'<%= ControladorPlazasOfertadas.PARAM_PLAZA_OFERTADA %>': row.plaza.codNum};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}},
		"columns": [
			{'data': 'plaza.area.idAreaExterno', 'filter': true, 'render': function(row) {
				return row.plaza.area.idAreaExterno + ' ' + row.plaza.area.descripcion;
			}},
			{'data': 'plaza.estado', 'filter': true},
			{'data': 'plaza.fechaFinOferta', 'filter': {'type': 'date'}},
			{'data': 'resultado', 'render': function(row) {
				if (row.resultado == true) {
					return "<div title='Aceptada' class='circle-true'></div>";
				} else if (row.resultado == false) {
					return "<div title='Rechazada' class='circle-false'></div>";
				}
			}}
		]
	});
	
});
</script>
