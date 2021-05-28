<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidarNoAfines"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidarNoAfines bean = (VistaValidarNoAfines)uvdatos.getVistas().get(VistaValidarNoAfines.class.getName());
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria() != null) {
			descripcion = "Última convocatoria: " + bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Validar meritos no sujetos afinidad</h2>
	<h3><%= descripcion %></h3>
		
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:10%" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:50%" class="area">Area</th>
			<th scope="col" style="width:10%" class="center">No validados</th>	
			<th scope="col" style="width:10%" class="center">Validados</th>
			<th scope="col" style="width:10%" class="center">Excluidos</th>	
			<th scope="col" style="width:10%" class="center">Total</th>	
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
	var table = new Atis.DataTable('#table', {
	    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS %>",
	    "defaultOrderBy": 5,
	    "defaultOrderDirection": 'desc',
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'a': '<%= ControladorValidarNoAfines.ACCION_BOLSA_SELECCIONADA %>', 
    				'<%= ControladorValidarNoAfines.PARAM_BOLSA %>': row.codNum};
    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	    }},
	    "columns": [
	    	{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
	        {'data': 'area.descripcion', 'filter': true},
	        {'data': 'totalMeritosNoValidados', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosNoValidados) ? 0 : row.totalMeritosNoValidados; } },
	        {'data': 'totalMeritosValidados', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosValidados) ? 0 : row.totalMeritosValidados; } },
	        {'data': 'totalMeritosExcluidos', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosExcluidos) ? 0 : row.totalMeritosExcluidos; } },
	        {'data': 'totalMeritos', 'class': 'center'} 
	    ]	    
	});		
}); 
</script>