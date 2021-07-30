<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDedicaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaDedicacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaDedicacion bean = (VistaDedicacion) uvdatos.getVistas().get(VistaDedicacion.class.getName());
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Dedicación</h2>
		
		<button class="link-btn" id="nueva_dedicacion">
			Nueva dedicación
		</button>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableDedicaciones">
		<tr>
			<th scope="col" style="width:100px">Id</th>
			<th scope="col" style="width:100px">Dedicación</th>
			<th scope="col" style="width:100px">Sueldo</th>
			<th scope="col" style="width:100px">Fecha creación</th>
			<th scope="col" style="width:100px">Activa</th>
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
	
	var table = new Atis.DataTable('#tableDedicaciones', {
		"ajax": { url: "<%=ControladorDedicaciones.URL_PATTERN_AJAX%>" },
		"pageSize": 10,
		"filterable": true,
		"action": "<%=ControladorDedicaciones.ACCION_DATATABLE_DEDICACIONES%>",
		"columns": [
			{'data': 'codNum', 'filter': {'type': 'number'}},
			{'data': 'dedicacion', 'filter': true},
			{'data': 'sueldo', 'filter': {'type': 'number'}},
			{'data': 'fechaCreacion', 'filter': {'type': 'date'}},
			{'data': 'activa', 'filter': {'type': 'select', 'options': {'true': 'Activas', 'false': 'Inactivas'}}, 'order': {'active': false}, 'render': function(row) {
				if (row.activa) {
					return "<div title='Activa' class='circle-true'></div>";
				} else {
					return "<div title='Inactiva' class='circle-false'></div>";
				}
			}},
			{'data': 'codNum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
						var params = {'a': '<%=ControladorDedicaciones.ACCION_EDITAR_DEDICACION%>', 'id': row.codNum};
						Atis.sendForm("<%=request.getRequestURI()%>", params);
					}
				}, {'label': function(row) { return row.activa ? "Borrar" : "Restaurar"; }, 
					'title':  function(row) { return row.activa ? "Borrar noticia" : "Restaurar noticia"; }, 'onClick': function(row) {
					var mensaje = "¿Desea borrar la noticia seleccionada?";
					var titulo = "Borrar noticia";
					if(!row.activa) {
						titulo = "Restaurar noticia";
						mensaje = "¿Desea restaurar la noticia seleccionada?";
					}
					
					Atis.confirmDialog(titulo, mensaje, {
						Si: function() {
							var params = {'a': '<%=ControladorDedicaciones.ACCION_ELIMINAR_NOTICIA%>',
									'id': row.codNum,
									'<%=ControladorNoticias.PARAM_ACTIVA%>': !row.activa};
							Atis.sendForm("<%= request.getRequestURI() %>", params);
							$(this).dialog("close");
						},
						No: function() {
							$(this).dialog("close");
						}
					});
				}}]
			}
		]
	});
	
	document.getElementById("nueva_dedicacion").addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%=request.getRequestURI()%>", {'a': '<%=ControladorDedicaciones.ACCION_NUEVA_DEDICACION%>'});
	});
	
});
</script>
