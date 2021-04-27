<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorValidarNoAfines"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaValidarNoAfines" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area" %>
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
	
	<h2>Validar meritos no sujetos a afinidad</h2>
	<h3><%= descripcion %></h3>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:5%">Id.</th>
			<th scope="col" style="width:5%" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:25%" class="area">Area</th>			
			<th scope="col" style="width:10%" class="center"></th>			
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
	var table = new Atis.DataTable('#table', {
	    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE %>",
	    "defaultOrderBy": 2,
	    "defaultOrderDirection": 'desc',
	    "columns": [
	    	{'data': 'codNum', 'filter': {'type': 'number'}},
	    	{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
	        {'data': 'area.descripcion', 'filter': true, 'class': 'overflow-auto'},
	        {'data': 'codnum', 'buttons': [{'label': 'Validar', 'onClick': function(row) {}}]}        
	    ]	    
	});		
}); 
</script>
