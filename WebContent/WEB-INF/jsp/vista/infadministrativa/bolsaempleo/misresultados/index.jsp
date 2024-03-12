<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisResultados bean = (VistaMisResultados)uvdatos.getVistas().get(VistaMisResultados.class.getName());
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Mis Resultados</h2>	
	
	<div class="form-select form-group">
		<label>Convocatoria</label>
		<select id="select_convocatoria">
			<%	for (Convocatoria conv: bean.getListaConvocatorias()) { %>
					<option value="<%= conv.getCodNum() %>" <%= bean.getConvocatoria().getCodNum().equals(conv.getCodNum()) ? "selected" : "" %>>
						<%= conv.getDescripcion() %>
					</option>
			<%	} %>
		</select>
	</div>
	
	<p>Resultados de la convocatoria seleccionada. Al pulsar sobre un area, listado del candidato con su puntuación y el detalle del cálculo.</p>
	
	<table class="bluetable bolsaempleo" id="tableAreasMRE">
		<tr>
			<th scope="col" style="width:100px" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:100%" class="area">Área</th>
			<th scope="col" style="width:100px" class="area">Puntuación</th>
			<th scope="col" style="width:88px" class="center"></th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>
	
<script>
$(document).ready(function() {
	
	document.getElementById("select_convocatoria").onchange = function () {
		if (this.value != 0) {
			var params = {
				'<%= ControladorMisResultados.PARAM_ACCION %>': '<%= ControladorMisResultados.ACCION_INDEX %>',
				'<%= ControladorMisResultados.PARAM_CONVOCATORIA %>': this.value
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		} else {
			document.getElementById("tablas_titulaciones").style.visibility = "hidden";
		}
	}
	
	var table = new Atis.DataTable('#tableAreasMRE', {
		"ajax": { url: "<%= ControladorMisResultados.URL_PATTERN_AJAX %>" },
		"pageSize": 10,
		"filterable": true,
		"stateSave": true,
		"action": "<%= ControladorMisResultados.ACCION_DATATABLE_BOLSAS %>",
		"params": {'<%= ControladorMisResultados.PARAM_CONVOCATORIA %>': '<%= bean.getConvocatoria().getCodNum() %>'},
		"columns": [
			{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
			{'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'},
			{'data': 'total', 'filter': {'type': 'number'}},
			{'data': 'codnum', 'buttons': [{'label': 'Ver resultados',
				'onClick': function(row) {
					var params = {
						'a': '<%= ControladorMisResultados.ACCION_SELECCIONAR_BOLSA %>',
						'<%= ControladorMisResultados.PARAM_BOLSA %>': row.codNum,
						'<%= ControladorMisResultados.PARAM_CONVOCATORIA %>': '<%= bean.getConvocatoria().getCodNum() %>'
					};
					Atis.sendForm("<%= request.getRequestURI() %>", params);
				},
				'visible': function(row) {
					return row.fechaBaremacion != null;
				},
				'renderNotVisible': function(row) {
					return row.resultadoActual ? 'Esperando resultados' : '';
				}
			}]
		}]
	});
	
});
</script>
