<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorConvocatorias" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConvocatorias" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConvocatorias bean = (VistaConvocatorias) uvdatos.getVistas().get(VistaConvocatorias.class.getName());
%>

<div class="bolsa-empleo convocatorias">
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Convocatorias</h2>
		
		<button class="link-btn" id="nueva_convocatoria">
			Nueva convocatoria
		</button>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableConvocatoriasCON">
		<tr>
			<th scope="col" style="width:30px" title="Id de la convocatoria">Id</th>
			<th scope="col" class="descripcion" style="width:100%">Descripción</th>
			<th scope="col" style="width:100px">Estado</th>
			<th scope="col" style="width:100px">Fecha cierre</th>
			<th scope="col" style="width:100px">Fecha finalización</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>

<script>
	
	$(document).ready(function() {
		var tableConvocatorias = new Atis.DataTable('#tableConvocatoriasCON', {
			"ajax": { url: "<%=  ControladorConvocatorias.URL_PATTERN_AJAX %>" },
			"pageSize": 10,
			"defaultOrderBy": 0,
			"defaultOrderDirection": 'desc',
			"stateSave": true,
			"action": "<%= ControladorConvocatorias.ACCION_DATATABLE %>",
			"clickable": {'onClick': function(row) {
				var params = {
						'a': '<%= ControladorConvocatorias.ACCION_SELECCIONAR_CONVOCATORIA %>',
						'<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>': row.codNum};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			}},
			"columns": [
				{'data': 'codNum'},
				{'data': 'descripcion'},
				{'data': 'estado'},
				{'data': 'fechaCierre'},
				{'data': 'fechaFinalizacion'}
			]
		});
	});
	
	document.getElementById("nueva_convocatoria").addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorConvocatorias.ACCION_AGREGAR_CONVOCATORIA %>'});
	});
	
</script>
