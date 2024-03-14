<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorMiembrosComision"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorNoticias"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMiembrosComision"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMiembrosComision bean = (VistaMiembrosComision) uvdatos.getVistas().get(VistaMiembrosComision.class.getName());
Area areaSeleccionada = bean.getArea();
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Miembros de la comisión</h2>
	
	<div class="form-select form-group">
		<label>Area</label>
		<select id="select_area">
			<option value="0">Elija el área</option>
			<% for(Area area: bean.getAreas()) { %>
				<option value="<%=area.getCodNum()%>" <%= areaSeleccionada != null && areaSeleccionada.getCodNum().equals(area.getCodNum()) ? "selected=\"selected\"" : "" %>><%=area.getDescripcion()%></option>
			<% } %>
		</select>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableEvaluadores" style="<%= areaSeleccionada == null ? "visibility: hidden" : "" %>">
		<tr>
			<th scope="col">Nombre</th>
			<th scope="col">Apellido (primer)</th>
			<th scope="col">Apellido (segundo)</th>
		</tr>
		<tbody></tbody>
		<tfoot>
			<tr>
				<th colspan="3" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>
	$(document).ready(function() {
		document.getElementById("select_area").onchange = function () {
			if (this.value != 0) {
				var params = {
					'<%= ControladorMiembrosComision.PARAM_ACCION %>': '<%= ControladorMiembrosComision.ACCION_SELECCIONAR_AREA %>',
					'<%= ControladorMiembrosComision.PARAM_AREA %>': this.value
				};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			} else {
				document.getElementById("tableEvaluadores").style.visibility = "hidden";
			}
		}
		
		<% if (areaSeleccionada != null) { %>
			var tableEvaluadores = new Atis.DataTable('#tableEvaluadores', {
				"ajax": { url: "<%= ControladorMiembrosComision.URL_PATTERN_AJAX %>", async: false },
				"pageSize": 10,
				"action": "<%= ControladorMiembrosComision.ACCION_DATATABLE_EVALUADORES %>",
				"params": {"<%= ControladorMiembrosComision.PARAM_AREA %>": '<%= areaSeleccionada.getCodNum() %>'},
				"title": "EVALUADORES DEL AREA: <%= areaSeleccionada.getDescripcion() %>",				
				"columns": [
					{'data': 'nombre', 'filter': true},
					{'data': 'apellido1', 'filter': true},
					{'data': 'apellido2', 'filter': true}
				]
			});
		<% } %>
	});
</script>