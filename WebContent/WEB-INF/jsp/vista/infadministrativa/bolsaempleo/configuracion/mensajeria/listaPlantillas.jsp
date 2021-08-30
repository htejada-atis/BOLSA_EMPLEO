<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorMensajes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMensajes"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMensajes bean = (VistaMensajes) uvdatos.getVistas().get(VistaMensajes.class.getName());
%>

<div class='bolsa-empleo mensajes'>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Plantillas</h2>
		<button class="link-btn" id="nueva_plantilla" style="margin-left: 4px">Nueva plantilla</button>
	</div>
	
	<table class="bluetable bolsaempleo" id="tablePlantillas">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:55%">Título</th>
			<th scope="col" class="center" style="width:15%">Estado</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="3" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>
	
<script>
$(document).ready(function() {
	document.getElementById('nueva_plantilla').addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'<%= ControladorMensajes.PARAM_ACCION %>': '<%= ControladorMensajes.ACCION_NUEVA_PLANTILLA %>'});
	});
	
	var clickRow = function(row) {
		var params = {
			'<%= ControladorMensajes.PARAM_ACCION %>': '<%= ControladorMensajes.ACCION_DETALLE_PLANTILLA %>',
			'<%= ControladorMensajes.PARAM_MENSAJE_ID %>': row.codNum
		};
		Atis.sendForm("<%=request.getRequestURI()%>", params);
	};
	
	var table = new Atis.DataTable('#tablePlantillas', {
		"ajax": { url: '<%= ControladorMensajes.URL_PATTERN_AJAX %>' },
		"pageSize": 10,
		"defaultOrderBy": 0,
		"action": "<%= ControladorMensajes.ACCION_DATATABLE_PLANTILLAS %>",
		"clickable": {'onClick': clickRow},
		"columns": [
			{'data': 'codNum'},
			{'data': 'titulo'},
		]
	});	
}); 
</script>
