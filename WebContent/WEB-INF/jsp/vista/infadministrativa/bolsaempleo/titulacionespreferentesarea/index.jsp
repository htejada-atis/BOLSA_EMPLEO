<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorCandidatoTitulacionesPreferentesArea"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaCandidatoTitulacionesArea" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa" %>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaCandidatoTitulacionesArea bean = (VistaCandidatoTitulacionesArea)uvdatos.getVistas().get(VistaCandidatoTitulacionesArea.class.getName());
Bolsa bolsa = bean.getArea();
%>

<div class='bolsas'>
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
	<h2>Titulaciones por Área</h2>	
	
	<table class="bluetable bolsaempleo" id="tableAreas">
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
	
	<table class="bluetable bolsaempleo" id="tableTitulaciones">
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
	
	var tableAreas = new Atis.DataTable('#tableAreas', {
	    "ajax": { url: "<%= ControladorCandidatoTitulacionesPreferentesArea.URL_PATTERN_AJAX %>", async: false },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorCandidatoTitulacionesPreferentesArea.ACCION_DATATABLE_BOLSAS %>",
	    "defaultOrderBy": 1,
	    "title": 'Areas de conocimiento',
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'a': '<%= ControladorCandidatoTitulacionesPreferentesArea.ACCION_BOLSA_SELECCIONADA %>',
    				'<%= ControladorCandidatoTitulacionesPreferentesArea.PARAM_BOLSA %>': row.codNum};
    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	    }},
	    <% if (bolsa != null) { %> "selected": <%= bolsa.getCodNum() %>,<% } %>
	    "columns": [
	    	{'data': 'area.idAreaExterno', 'filter': true},
	        {'data': 'area.descripcion', 'filter': true},  
	    ],
	});
	
	<% if (bolsa != null) { %>
		
		var table_titulaciones_area = new Atis.DataTable('#tableTitulaciones', {
			"ajax": { url: "<%=ControladorCandidatoTitulacionesPreferentesArea.URL_PATTERN_AJAX%>", async: false },
		    "params": {"<%=ControladorCandidatoTitulacionesPreferentesArea.PARAM_AREA%>": "<%= bolsa.getArea().getCodNum() %>"},
		    "filterable": true,
		    "pageSize": 5,
		    "defaultOrderBy": 0,
		    "title": 'Titulaciones Preferentes: <%=bolsa.getArea().getDescripcion()%>',
		    "action": "<%=ControladorCandidatoTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES%>",
		    "columns": [
		        {'data': 'nombre', 'filter': {'type': 'text'}},
		    ],
		});
	
	<% } else { %>
		document.getElementById("tableTitulaciones").style.visibility = "hidden";
	<% } %>
	
}); 
</script>