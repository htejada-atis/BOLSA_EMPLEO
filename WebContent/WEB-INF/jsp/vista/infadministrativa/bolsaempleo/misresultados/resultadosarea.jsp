<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisResultados bean = (VistaMisResultados)uvdatos.getVistas().get(VistaMisResultados.class.getName());
Bolsa bolsa = bean.getBolsa();
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Resultados del área: <%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h2>
	
	<table class="bluetable bolsaempleo" id="tableCandidatosMRE">
		<tr>
			<th scope="col" style="width:76px">D.N.I</th>
			<th scope="col" style="width:100%" class="nombre">Nombre</th>
			<th scope="col" style="width:76px">Puntuación</th>
			<th scope="col" style="width:76px"></th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<div class="row">
		<button class="link-btn" id="misresultados_volver" style="float:left;">
			Volver
		</button>
	</div>
</div>
	
<script>

$(document).ready(function() {
	var tableCandidatos = new Atis.DataTable('#tableCandidatosMRE', {
		"ajax": { url: "<%= ControladorMisResultados.URL_PATTERN_AJAX %>" },
		"pageSize": 20,
		"defaultOrderBy": 2,
		"defaultOrderDirection": 'desc',
		"stateSave": true,
		"action": "<%= ControladorMisResultados.ACCION_DATATABLE_CANDIDATOS %>",
		"params": {'<%=ControladorMisResultados.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>'},
		"selected": <%= bean.getUsuarioLogeado().getCodNum() %> ,
		"columns": [
			{'data': 'prsnif', 'order': false},
			{'data': 'apellido1', 'order': false, 'overflow': 'auto', 'render': function(row) {
				return row.nombre + " " + row.apellido1 + " " + row.apellido2;
			}},
			{'data': 'total', 'order': false},
			{'data': 'codnum', 'buttons': [
				{'label': 'Ver detalles',
					'onClick': function(row) {
						var params = {
							'<%= ControladorMisResultados.PARAM_ACCION %>': '<%= ControladorMisResultados.ACCION_MIS_RESULTADOS %>',
							'<%= ControladorMisResultados.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
							'<%= ControladorMisResultados.PARAM_CONVOCATORIA %>': '<%= bean.getConvocatoria().getCodNum() %>'
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
					},
					'visible': function(row) {
						return row.codNum == <%= bean.getUsuarioLogeado().getCodNum() %>;
					}
				}]
			}
		]
	});
	
	document.getElementById("misresultados_volver").addEventListener("click", function() {
		Atis.sendForm("<%= request.getRequestURI() %>", {});
	});
});

</script>
