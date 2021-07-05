<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaResultados" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaResultados bean = (VistaResultados)uvdatos.getVistas().get(VistaResultados.class.getName());
Bolsa bolsa = bean.getBolsa();
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Resultados del área: <%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h2>
	
	<table class="bluetable bolsaempleo" id="tableCandidatos">
		<tr>
			<th scope="col" style="width:76px">D.N.I</th>
			<th scope="col" style="width:100%" class="nombre">Nombre</th>
			<th scope="col" style="width:76px" class="center">Puntuación</th>
			<th scope="col" style="width:76px" class="center"></th>
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
	var tableAreas = new Atis.DataTable('#tableCandidatos', {
	    "ajax": { url: "<%= ControladorResultados.URL_PATTERN_AJAX %>" },
	    "pageSize": 100,
	    "filterable": true,
	    "action": "<%= ControladorResultados.ACCION_DATATABLE_CANDIDATOS %>",
	    "params": {'<%=ControladorResultados.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>'},
	    "columns": [
	    	{'data': 'prsnif', 'filter': true},
	    	{'data': 'apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
        		return row.nombre + " " + row.apellido1 + " " + row.apellido2;
        	}},
	        {'data': 'total', 'filter': {'type': 'number'}},
	        {'data': 'codnum', 'buttons': [
	        	{'label': 'Ver detalles',
		        	'onClick': function(row) {
		        		var params = {
		    				'<%= ControladorResultados.PARAM_ACCION %>': '<%= ControladorResultados.ACCION_SELECCIONAR_CANDIDATO %>',
		    				'<%= ControladorResultados.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
		    				'<%= ControladorResultados.PARAM_CANDIDATO %>': row.codNum
			    		};
			    		Atis.sendForm("<%= request.getRequestURI() %>", params);
		        	}
	        	}]
	        }
	    ]
	});
});

</script>
