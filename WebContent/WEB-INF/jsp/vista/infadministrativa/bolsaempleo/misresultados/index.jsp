<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisResultados bean = (VistaMisResultados)uvdatos.getVistas().get(VistaMisResultados.class.getName());
%>

<div class='bolsa-empleo'>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Resultados</h2>	
	<p>Resultados de la última evaluación. Al pulsar sobre un area, listado del candidato con su puntuanción y el detalle del cálculo.</p>
	
	<!-- <table class="bluetable bolsaempleo" id="tableAreas">
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
	</table>-->
</div>
	
<script>
$(document).ready(function() {
	var table = new Atis.DataTable('#tableAreas', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/misresultados" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorMisResultados.ACCION_DATATABLE_AREAS %>",
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