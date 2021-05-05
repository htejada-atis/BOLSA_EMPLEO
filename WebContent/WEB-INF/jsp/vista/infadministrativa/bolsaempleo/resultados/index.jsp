<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorResultados"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaResultados" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaResultados bean = (VistaResultados)uvdatos.getVistas().get(VistaResultados.class.getName());
%>

<div class='bolsa-empleo'>
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
	<h2>Resultados</h2>	
	<p>Resultados de la última evaluación. Al pulsar sobre un area, listado de candidatos con su puntuanción y el detalle del cálculo.</p>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:5%">Id.</th>
			<th scope="col" style="width:5%" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:25%" class="area">Area</th>			
			<th scope="col" style="width:15%" class="area">Fecha baremación</th>
			<th scope="col" style="width:10%" class="center"></th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>	
</div>
	
<script>
$(document).ready(function() {
	var table = new Atis.DataTable('#table', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/resultados" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorResultados.ACCION_DATATABLE %>",
	    "defaultOrderBy": 1,
	    "defaultOrderDirection": 'desc',
	    "columns": [
	    	{'data': 'codNum', 'filter': {'type': 'number'}},
	    	{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},	    	
	        {'data': 'area.descripcion', 'filter': true, 'class': 'overflow-auto'},
	        {'data': 'codNum', 'filter': {'type': 'date'}, 'render': function(row) { return ""; } },
	        {'data': 'codnum', 'buttons': [{'label': 'Ver resultados', 'onClick': function(row) {}}]}        
	    ],
	    "actions": [
	    	{'label': 'Filtrar bolsa', 'onClick': function(selected) {  } },	    	
	    ]
	});		
}); 
</script>