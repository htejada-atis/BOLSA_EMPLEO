<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidar" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidar bean = (VistaValidar)uvdatos.getVistas().get(VistaValidar.class.getName());
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria() != null) {
			descripcion = bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Validar meritos sujetos afinidad</h2>
	<h3><%= descripcion %></h3>
		
	<table class="bluetable bolsaempleo" id="tableAreasEvaluarAfines">
		<tr>
			<th scope="col" style="width:76px" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:50%" class="area">Area</th>
			<th scope="col" style="width:76px" class="center">No validados</th>
			<th scope="col" style="width:76px" class="center">Validados</th>
			<th scope="col" style="width:76px" class="center">Excluidos</th>
			<th scope="col" style="width:76px" class="center">Total</th>
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
	var tableAreasEvaluarAfines = new Atis.DataTable('#tableAreasEvaluarAfines', {
		"ajax": { url: "<%= ControladorValidar.URL_PATTERN_AJAX %>" },
		"pageSize": 10,
		"filterable": true,
		"action": "<%= ControladorValidar.ACCION_DATATABLE_BOLSAS %>",
		"defaultOrderBy": 2,
		"defaultOrderDirection": 'desc',
		"stateSave": true,
		"clickable": {'onClick': function(row) {
			var params = {
					'<%= ControladorValidar.PARAM_ACCION %>': '<%= ControladorValidar.ACCION_BOLSA_SELECCIONADA %>', 
					'<%= ControladorValidar.PARAM_BOLSA %>': row.codNum};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}},
		"columns": [
			{'data': 'area.idAreaExterno', 'filter': true},
			{'data': 'area.descripcion', 'filter': true},
			{'data': 'totalMeritosNoValidados', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosNoValidados) ? 0 : row.totalMeritosNoValidados; } },
			{'data': 'totalMeritosValidados', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosValidados) ? 0 : row.totalMeritosValidados; } },
			{'data': 'totalMeritosExcluidos', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosExcluidos) ? 0 : row.totalMeritosExcluidos; } },
			{'data': 'totalMeritos', 'class': 'center'} 
		]
	});
}); 
</script>