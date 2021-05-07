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
			descripcion = "Última convocatoria: " + bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>

	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } %>
	
	<h2>Validar meritos sujetos afinidad</h2>
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
	    "ajax": { url: "<%= ControladorValidar.URL_PATTERN_AJAX %>" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorValidar.ACCION_DATATABLE %>",
	    "defaultOrderBy": 1,
	    "columns": [
	    	{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
	        {'data': 'area.descripcion', 'filter': true},
	        {'data': 'totalMeritosNoValidados', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosNoValidados) ? 0 : row.totalMeritosNoValidados; } },
	        {'data': 'totalMeritosValidados', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosValidados) ? 0 : row.totalMeritosValidados; } },
	        {'data': 'totalMeritosExcluidos', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosExcluidos) ? 0 : row.totalMeritosExcluidos; } },
	        {'data': 'totalMeritos', 'order': false, 'class': 'center'} 
	    ]	    
	});		
}); 
</script>

