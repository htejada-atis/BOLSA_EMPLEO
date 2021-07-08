<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaResultados"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaResultados bean = (VistaResultados)uvdatos.getVistas().get(VistaResultados.class.getName());
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Resultados</h2>	
	<p>Resultados de la última evaluación. Al pulsar sobre un area, listado de candidatos con su puntuanción y el detalle del cálculo.</p>
	
	<table class="bluetable bolsaempleo" id="tableAreas">
		<tr>
			<th scope="col" style="width:50px">Id.</th>
			<th scope="col" style="width:100px" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:100%; min-width:100px" class="area">Area</th>
			<th scope="col" style="width:120px" class="area">Fecha baremación</th>
			<th scope="col" style="width:88px" class="center"></th>
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
	
	function isBolsaBaremada(fechaBaremacion) {
		if (fechaBaremacion) {
			var fechaConvocatoria = '<%= Formateador.formatoFecha(bean.getConvocatoria().getFechaCierre(), Formateador.FORMATO_FECHA_DDMMYYYY) %>';
    		var parts = fechaConvocatoria.split("/");
    		var date = new Date(Number(parts[2]), Number(parts[1]) - 1, Number(parts[0]));
    		
    		return Date.parse(fechaBaremacion) > Date.parse(date);
		} else {
			return false;
		}
	}
	
	var tableAreas = new Atis.DataTable('#tableAreas', {
	    "ajax": { url: "<%= ControladorResultados.URL_PATTERN_AJAX %>" },
	    "pageSize": 20,
	    "filterable": true,
	    "action": "<%= ControladorResultados.ACCION_DATATABLE_BOLSAS %>",
	    "defaultOrderBy": 1,
	    "columns": [
	    	{'data': 'codNum', 'filter': {'type': 'number'}},
	    	{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
	        {'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'},
	        {'data': 'codNum', 'filter': {'type': 'date'}, 'render': function(row) {
	        		return isBolsaBaremada(row.fechaBaremacion) ? row.fechaBaremacion : '';
	        	}
	        },
	        {'data': 'codnum', 'buttons': [{'label': 'Ver resultados', 
		        	'onClick': function(row) {
		        		var params = {
		    				'a': '<%= ControladorResultados.ACCION_SELECCIONAR_BOLSA %>',
		    				'<%= ControladorResultados.PARAM_BOLSA %>': row.codNum
			    		};
			    		Atis.sendForm("<%= request.getRequestURI() %>", params);
		        	},
		        	'visible': function(row) {
		        		return isBolsaBaremada(row.fechaBaremacion);
	        		}
	        	}]
	        }
	    ],
	});
});
</script>
