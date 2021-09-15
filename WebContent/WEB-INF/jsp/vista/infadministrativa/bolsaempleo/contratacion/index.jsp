<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="java.util.Map.Entry" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaContratacion bean = (VistaContratacion) uvdatos.getVistas().get(VistaContratacion.class.getName());
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Plazas ofertadas</h2>
	
	<button class="link-btn" id="nueva_plaza" style="float: right">
		Nueva plaza
	</button>
	
	<% if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
		<button class="link-btn" id="exportar_plazas" title="Exportar plazas ofertadas a csv">Exportar a csv</button>
	<% } %>
	
	<table class="bluetable bolsaempleo" id="tablePlazasOfertadas">
		<tr>
			<th scope="col" style="width:20px">Id</th>
			<th scope="col" style="width:46px">Código</th>
			<th scope="col" style="width:100%">Área</th>
			<th scope="col" style="width:88px">Estado</th>
			<th scope="col" style="width:86px">Fecha creación</th>
			<th scope="col" style="width:80px">Fecha abierta</th>
			<th scope="col" style="width:88px">Fecha fin oferta</th>
			<th scope="col" style="width:80px">Fecha cerrada</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="8" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>
	
<script>
$(document).ready(function() {
	var estadosPlaza = {};
<%	for (Entry<String, String> est: ModeloPlazaOfertada.ESTADOS.entrySet()) { %>
		estadosPlaza["<%= est.getKey() %>"] = "<%= est.getValue() %>";
<%	} %>
	
	var table = new Atis.DataTable('#tablePlazasOfertadas', {
		"ajax": { url: "<%= ControladorContratacion.URL_PATTERN_AJAX %>" },
		"pageSize": 10,
		"defaultOrderBy": 4,
		"defaultOrderDirection": 'desc',
		"filterable": true,
		"action": "<%= ControladorContratacion.ACCION_DATATABLE_PLAZAS_OFERTADAS %>",
		"clickable": {'onClick': function(row) {
			var params = {
					'a': '<%= ControladorContratacion.ACCION_SELECCIONAR_PLAZA_OFERTADA %>', 
					'<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>': row.codNum};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}},
		"columns": [
			{'data': 'codNum', 'filter': true, 'overflow': 'auto'},
			{'data': 'idPlaza', 'filter': true, 'overflow': 'auto'},
			{'data': 'area.idAreaExterno', 'filter': true, 'render': function(row) {
				return row.area.idAreaExterno + ' ' + row.area.descripcion;
			}},
			{'data': 'estado', 'filter': {'type': 'select', 'options': estadosPlaza}},
			{'data': 'fechaCreacion', 'filter': {'type': 'date'}},
			{'data': 'fechaAbierta', 'filter': {'type': 'date'}},
			{'data': 'fechaFinOferta', 'filter': {'type': 'date'}},
			{'data': 'fechaCerrada', 'filter': {'type': 'date'}}
		]
	});
	
	document.getElementById("nueva_plaza").addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%=request.getRequestURI()%>", {'a': '<%=ControladorContratacion.ACCION_NUEVA_PLAZA_OFERTADA%>'});
	});
	
<% if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
		$('#exportar_plazas').on('click', function() {
			window.open("<%= request.getRequestURI() %>?<%= ControladorContratacion.PARAM_ACCION %>=<%= ControladorContratacion.ACCION_EXPORTAR_PLAZAS %>");
		});
<%	} %>
	
});
</script>
