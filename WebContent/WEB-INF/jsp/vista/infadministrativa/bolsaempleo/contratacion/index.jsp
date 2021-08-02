<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaContratacion bean = (VistaContratacion) uvdatos.getVistas().get(VistaContratacion.class.getName());
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Plazas ofertadas</h2>
		
		<button class="link-btn" id="nueva_plaza">
			Nueva plaza
		</button>
	</div>
	
	<table class="bluetable bolsaempleo" id="tablePlazasOfertadas">
		<tr>
			<th scope="col" style="width:120px">Área</th>
			<th scope="col" style="width:100%">Dedicación</th>
			<th scope="col" style="width:95px">Estado</th>
			<th scope="col" style="width:96px">Fecha creación</th>
			<th scope="col" style="width:90px">Fecha abierta</th>
			<th scope="col" style="width:90px">Fecha cerrada</th>
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
	
	var table = new Atis.DataTable('#tablePlazasOfertadas', {
		"ajax": { url: "<%= ControladorContratacion.URL_PATTERN_AJAX %>" },
		"pageSize": 10,
		"filterable": true,
		"title": 'Plazas ofertadas',
		"action": "<%= ControladorContratacion.ACCION_DATATABLE_PLAZAS_OFERTADAS %>",
		"clickable": {'onClick': function(row) {
			var params = {
					'a': '<%= ControladorContratacion.ACCION_SELECCIONAR_PLAZA_OFERTADA %>', 
					'<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>': row.codNum};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}},
		"columns": [
			{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}, 'render': function(row) {
				return row.area.idAreaExterno + ' ' + row.area.descripcion;
			}},
			{'data': 'dedicacion', 'filter': true},
			{'data': 'estado', 'filter': true},
			{'data': 'fechaCreacion', 'filter': {'type': 'date'}},
			{'data': 'fechaAbierta', 'filter': {'type': 'date'}},
			{'data': 'fechaCerrada', 'filter': {'type': 'date'}}
		]
	});
	
	document.getElementById("nueva_plaza").addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%=request.getRequestURI()%>", {'a': '<%=ControladorContratacion.ACCION_NUEVA_PLAZA_OFERTADA%>'});
	});
	
});
</script>
