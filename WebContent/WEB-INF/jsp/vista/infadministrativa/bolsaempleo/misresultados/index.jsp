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

	<h2>Mis Resultados</h2>	
	<p>Resultados de la última evaluación. Al pulsar sobre un area, listado del candidato con su puntuanción y el detalle del cálculo.</p>
	
	<table class="bluetable bolsaempleo" id="tableAreas">
		<tr>
			<th scope="col" style="width:100PX" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:100%" class="area">Área</th>			
			<th scope="col" style="width:100px" class="area">Puntuación</th>
			<th scope="col" style="width:88px" class="center"></th>			
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
	
	var table = new Atis.DataTable('#tableAreas', {
	    "ajax": { url: "<%= ControladorMisResultados.URL_PATTERN_AJAX %>" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorMisResultados.ACCION_DATATABLE_BOLSAS %>",
	    "columns": [
	    	{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
	        {'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'},
	        {'data': 'total', 'filter': {'type': 'number'}},
	        {'data': 'codnum', 'buttons': [{'label': 'Ver resultados', 
	        	'onClick': function(row) {
	        		var params = {
	    				'a': '<%= ControladorMisResultados.ACCION_SELECCIONAR_BOLSA %>',
	    				'<%= ControladorMisResultados.PARAM_BOLSA %>': row.codNum
		    		};
		    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	        	},
	        	'visible': function(row) {
	        		return row.fechaBaremacion != null && row.resultadoActual;
        		},
        		'renderNotVisible': function(row) {
        			return row.resultadoActual ? 'Esperando resultados' : '';
        		}
        	}]
        }
	    ]
	});
	
}); 
</script>
