<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorCandidatoTitulacionesPreferentesArea"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatoTitulacionesArea" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaCandidatoTitulacionesArea bean = (VistaCandidatoTitulacionesArea)uvdatos.getVistas().get(VistaCandidatoTitulacionesArea.class.getName());
Bolsa bolsa = bean.getArea();
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Titulaciones por área</h2>	

	<p>Página de consulta sobre que titulaciones son preferentes por área</p>
	
	<table class="bluetable bolsaempleo" id="tableAreasTPA2">
		<tr>
			<th scope="col" style="width:15%" title="Código área">Cod. Área.</th>
			<th scope="col" style="width:85%" class="area">Nombre del Área</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" id="tableTitulacionesTPA2">
		<tr>
			<th scope="col"	style="width:100%">Nombre</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="1" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>
	
<script>
$(document).ready(function() {
	
	var tableAreas = new Atis.DataTable('#tableAreasTPA2', {
	    "ajax": { url: "<%= ControladorCandidatoTitulacionesPreferentesArea.URL_PATTERN_AJAX %>", async: false },
	    "pageSize": 10,
	    "filterable": true,
	    "stateSave": true,
	    "action": "<%= ControladorCandidatoTitulacionesPreferentesArea.ACCION_DATATABLE_BOLSAS %>",
	    "defaultOrderBy": 1,
	    "title": 'AREAS DE CONOCIMIENTO',
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'a': '<%= ControladorCandidatoTitulacionesPreferentesArea.ACCION_BOLSA_SELECCIONADA %>',
    				'<%= ControladorCandidatoTitulacionesPreferentesArea.PARAM_BOLSA %>': row.codNum};
    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	    }},
	    <% if (bolsa != null) { %> 
	    	"selected": <%= bolsa.getCodNum() %>,
	    <% } %>
	    "columns": [
	    	{'data': 'area.idAreaExterno', 'filter': true},
	        {'data': 'area.descripcion', 'filter': true},  
	    ],
	});
	
	<% if (bolsa != null) { %>
		
		var table_titulaciones_area = new Atis.DataTable('#tableTitulacionesTPA2', {
			"ajax": { url: "<%=ControladorCandidatoTitulacionesPreferentesArea.URL_PATTERN_AJAX%>", async: false },
		    "params": {"<%=ControladorCandidatoTitulacionesPreferentesArea.PARAM_AREA%>": "<%= bolsa.getArea().getCodNum() %>"},
		    "filterable": true,
		    "pageSize": 5,
		    "defaultOrderBy": 0,
		    "title": 'TITULACIONES PREFERENTES: <%=bolsa.getArea().getDescripcion()%>',
		    "action": "<%=ControladorCandidatoTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES%>",
		    "columns": [
		        {'data': 'nombre', 'filter': {'type': 'text'}},
		    ],
		});
	
	<% } else { %>
		document.getElementById("tableTitulacionesTPA2").style.visibility = "hidden";
	<% } %>
	
}); 
</script>
