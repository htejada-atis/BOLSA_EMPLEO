<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorPlantillas"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlantillas"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaPlantillas bean = (VistaPlantillas) uvdatos.getVistas().get(VistaPlantillas.class.getName());
%>

<div class='bolsa-empleo mensajes'>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Plantillas</h2>
		<button class="link-btn" id="nueva_plantilla" style="margin-left: 4px">Nueva plantilla</button>
	</div>
	
	<table class="bluetable bolsaempleo" id="tablePlantillas">
		<tr>
			<th scope="col" style="width:9%">Id</th>
			<th scope="col" style="width:80%">Nombre</th>
			<th scope="col" style="width:11%"></th>
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
		Atis.sendForm("<%= request.getRequestURI() %>", {'<%= ControladorPlantillas.PARAM_ACCION %>': '<%= ControladorPlantillas.ACCION_NUEVA_PLANTILLA %>'});
	});
	
	var clickRow = function(row) {
		var params = {
			'<%= ControladorPlantillas.PARAM_ACCION %>': '<%= ControladorPlantillas.ACCION_EDITAR_PLANTILLA %>',
			'<%= ControladorPlantillas.PARAM_PLANTILLA %>': row.codNum
		};
		Atis.sendForm("<%=request.getRequestURI()%>", params);
	};
	
	var table = new Atis.DataTable('#tablePlantillas', {
		"ajax": { url: '<%= ControladorPlantillas.URL_PATTERN_AJAX %>' },
		"pageSize": 10,
		"stateSave": true,
		"action": "<%= ControladorPlantillas.ACCION_DATATABLE_PLANTILLAS %>",
		"clickable": {'onClick': clickRow},
		"columns": [
			{'data': 'codNum'},
			{'data': 'nombre'},
			{'data': 'codNum', 'buttons': [
				{'label': "Borrar", 'title':  "Borrar plantilla", 
					'onClick': function(row) {
						var mensaje = "¿Desea borrar la plantilla seleccionada?";
						var titulo = "Borrar plantilla";
						
						Atis.confirmDialog(titulo, mensaje, {
							Si: function() {
								var params = {'a': '<%=ControladorPlantillas.ACCION_ELIMINAR_PLANTILLA %>', 
										'<%= ControladorPlantillas.PARAM_PLANTILLA %>': row.codNum};
								Atis.sendForm("<%= request.getRequestURI() %>", params);
								$(this).dialog("close");
							},
							No: function() {
								$(this).dialog("close");
							}
						});
					}
				}
			]}
		]
	});	
}); 
</script>
