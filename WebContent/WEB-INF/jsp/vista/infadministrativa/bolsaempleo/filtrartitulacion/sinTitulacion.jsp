<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.stream.Collectors" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorFiltrarTitulacion"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFiltrar" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFiltrar bean = (VistaFiltrar)uvdatos.getVistas().get(VistaFiltrar.class.getName());
UsuarioBolsaEmpleo candidato = bean.getCandidato();
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Listado de candidatos sin titulación</h2>
		
		<button class="link-btn" id="exportar">
			 Exportar a csv
		</button>
		
		<button class="link-btn" id="volver">
			 Volver
		</button>
	</div>

	<table class="bluetable bolsaempleo" id="tableCandidatosFST">
		<tr>
			<th scope="col" style="width:12%">Documento</th>
			<th scope="col" style="width:43%">Candidato</th>
			<th scope="col" style="width:30%">Email</th>
			<th scope="col" style="width:15%"></th>
		</tr>
		<tbody></tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>
	
<script>
	$(document).ready(function() {
		var tableCandidatos = new Atis.DataTable('#tableCandidatosFST', {
			"ajax": { url: "<%=ControladorFiltrarTitulacion.URL_PATTERN_AJAX%>", async: false },
			"action": "<%=ControladorFiltrarTitulacion.ACCION_DATATABLE_SIN_TITULACIONES%>",
			"pageSize": 100,
			"filterable": true,
			"columns": [
				{'data': 'prsnif', 'filter': true},
				{'data': 'apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
					return row.nombre + " " + row.apellido1 + " " + row.apellido2; 
				}},
				{'data': 'email', 'filter': true, 'overflow': 'auto'},
				{'data': 'codNum', 'buttons': [{'label': 'Ir a candidato', 'onClick': function(row) { 
					window.open("<%= ControladorUsuarioCandidato.getUrlCandidato("_NUM_") %>".replace("_NUM_", row.codNum));
				}}]}
			],
		});
		
		$('#volver').on('click', function() {
			var params = {'<%=ControladorFiltrarTitulacion.PARAM_ACCION %>': '<%=ControladorFiltrarTitulacion.ACCION_INDEX%>'};
			Atis.sendForm("<%=request.getRequestURI()%>", params);
		});
		
		$('#exportar').on('click', function() {
			window.open("<%= request.getRequestURI() %>?<%= ControladorFiltrarTitulacion.PARAM_ACCION %>=<%= ControladorFiltrarTitulacion.ACCION_EXPORTAR_SIN_TITULACION %>");
		});
	}); 
</script>
	
