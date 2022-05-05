<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaResultados"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaResultados bean = (VistaResultados)uvdatos.getVistas().get(VistaResultados.class.getName());
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Resultados</h2>
	
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
	
	<p>Resultados de la convocatoria seleccionada. Al pulsar sobre un area, listado de candidatos con su puntuanción y el detalle del cálculo.</p>
	
	<table class="bluetable bolsaempleo" id="tableAreasRES">
		<tr>
			<th scope="col" style="width:50px">Id.</th>
			<th scope="col" style="width:100px" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:100%; min-width:100px" class="area">Area</th>
			<th scope="col" style="width:120px" class="area">Fecha baremación</th>
			<th scope="col" style="width:90px" class="center"></th>
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
	
	document.getElementById("select_convocatoria").onchange = function () {
		if (this.value != 0) {
			var params = {
				'<%= ControladorResultados.PARAM_ACCION %>': '<%= ControladorResultados.ACCION_INDEX %>',
				'<%= ControladorResultados.PARAM_CONVOCATORIA %>': this.value
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		} else {
			document.getElementById("tablas_titulaciones").style.visibility = "hidden";
		}
	}
	
	var tableAreas = new Atis.DataTable('#tableAreasRES', {
		"ajax": { url: "<%= ControladorResultados.URL_PATTERN_AJAX %>" },
		"pageSize": 20,
		"filterable": true,
		"stateSave": true,
		"action": "<%= ControladorResultados.ACCION_DATATABLE_BOLSAS %>",
		"params": {'<%= ControladorResultados.PARAM_CONVOCATORIA %>': '<%= bean.getConvocatoria().getCodNum() %>'},
		"defaultOrderBy": 1,
		"columns": [
			{'data': 'codNum', 'filter': {'type': 'number'}},
			{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
			{'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'},
			{'data': 'fechaBaremacion', 'filter': {'type': 'date'}, 'render': function(row) {
					return row.resultadoActual ? row.fechaBaremacion : '';
				}
			},
			{'data': 'codnum', 'buttons': [{'label': 'Ver resultados',
					'onClick': function(row) {
						var params = {
							'a': '<%= ControladorResultados.ACCION_SELECCIONAR_BOLSA %>',
							'<%= ControladorResultados.PARAM_BOLSA %>': row.codNum,
							'<%= ControladorResultados.PARAM_CONVOCATORIA %>': '<%= bean.getConvocatoria().getCodNum() %>'
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
					},
					'visible': function(row) {
						return row.resultadoActual;
					}
				}]
			}
		],
	});
	
});
</script>
