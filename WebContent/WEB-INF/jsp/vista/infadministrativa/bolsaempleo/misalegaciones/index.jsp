<%@ page trimDirectiveWhitespaces="true"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisAlegaciones"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisAlegaciones bean = (VistaMisAlegaciones) uvdatos.getVistas().get(VistaMisAlegaciones.class.getName());

%>

<div class='bolsa-empleo mis-alegaciones'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Mis Alegaciones</h2>

	<div id="aviso" class="info">
    	Para crear una alegación, acceda desde la sección de 'Mis Resultados' y pulse el botón 'Crear Alegación'.
	</div>

	<table class="bluetable bolsaempleo tablebolsas" id="tableMisAlegaciones">
		<tr>
			<th scope="col" style="width: 15%;">Convocatoria</th>
			<th scope="col">Área</th>
			<th scope="col" style="width: 15%;">Estado</th>
			<th scope="col" style="width: 15%;">Fecha creación</th>
			<th scope="col" style="width: 15%;">Fecha presentación</th>
			<th scope="col" style="width: 10%;">Acción</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>
$(document).ready(function() {

	var table = new Atis.DataTable('#tableMisAlegaciones', {
		"ajax": { url: "<%= ControladorMisAlegaciones.URL_PATTERN_AJAX %>" },
		"pageSize": 10,
		"pageSizeOptions": [10, 50, 100],
		"filterable": true,
		"action": "<%= ControladorMisAlegaciones.ACCION_DATATABLE_MIS_ALEGACIONES %>",
		"defaultOrderBy": 2,
		"defaultOrderDirection": 'desc',
		"stateSave": true,
		"columns": [
			{ 'data': 'convocatoria.descripcion', 'filter': true },
			{ 'data': 'area.descripcion', 'filter': true },
			{
				'data': 'estado',
				'render': function(row) {
					switch(row.estado) {
						case '<%= ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION %>':
							return 'PENDIENTE';
						case '<%= ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION %>':
							return 'PRESENTADA';
						case '<%= ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION %>':
							return 'EN RESOLUCIÓN';
						case '<%= ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION %>':
							return 'ENVIADA AL DEPARTAMENTO';
						case '<%= ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION %>':
							return 'INFORMADA';
						case '<%= ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION %>':
							return 'RESUELTA';
						default:
							return (row.estado || '').toString().toUpperCase();
					}
				},
				'filter': {
					'type': 'select',
					'options': {
						'<%= ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION %>': 'PENDIENTE',
						'<%= ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION %>': 'PRESENTADA',
						'<%= ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION %>': 'EN RESOLUCIÓN',
						'<%= ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION %>': 'ENVIADA AL DEPARTAMENTO',
						'<%= ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION %>': 'INFORMADA',
						'<%= ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION %>': 'RESUELTA'
					}
				}
			},
			{ 'data': 'fechaCreacion', 'filter': false },
			{
				'data': 'fechaConfirmacion',
				'filter': false,
				'render': function(row) {
					if (row.fechaConfirmacion) {
						return row.fechaConfirmacion;
					}
					return '-';
				}
			},
			{
				"data": "codNum",
				"filter": false,
				"buttons": [
					{
						"label": "Ver Detalle",
						"title": "Ver los detalles de la alegación",
						"onClick": function(row) {
							var params = {
								'<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_VER_DETALLE_ALEGACION %>',
								'<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': row.codNum
							};
							Atis.sendForm("<%= request.getRequestURI() %>", params);
						}
					}
				]
			}
		]
	});
});
</script>