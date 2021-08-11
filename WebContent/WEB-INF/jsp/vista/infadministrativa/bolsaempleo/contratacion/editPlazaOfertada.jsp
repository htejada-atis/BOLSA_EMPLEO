<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="java.util.Map.Entry" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaContratacion bean = (VistaContratacion) uvdatos.getVistas().get(VistaContratacion.class.getName());
PlazaOfertada plaza = bean.getPlazaOfertada();
boolean personal = bean.getUsuarioLogeado().isServicioPersonal();

String area = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_AREA, plaza.getArea().getCodNum().toString());
String dedicacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DEDICACION, plaza.getDedicacion().getCodNum().toString());
String justificacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_JUSTIFICACION, EscapaHTML.escapa(plaza.getJustificacion()));
String duracionPrevista = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DURACION_PREVISTA, EscapaHTML.escapa(plaza.getDuracionPrevista()));
String cuatrimestre = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CUATRIMESTRE, EscapaHTML.escapa(plaza.getCuatrimestre()));
String centroDestino = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CUATRIMESTRE, EscapaHTML.escapa(plaza.getCuatrimestre()));
String fechaFinOferta = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_FECHA_FIN_OFERTA, plaza.getFechaFinOferta() != null ? Formateador.formatoFecha(plaza.getFechaFinOferta(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "");
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Editar plaza ofertada</h2>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_plaza" class="be-form" method="post" action="<%=request.getRequestURI()%>" enctype="multipart/form-data">
		<input type="hidden" name="<%=ControladorContratacion.PARAM_ACCION%>" id="accion_formulario"
				value="<%=ControladorContratacion.ACCION_EDITAR_PLAZA_OFERTADA%>" />
		<input type="hidden" name="<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>" id="plaza_id" 
				value="<%= plaza.getCodNum()%>" />
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_area" class="bold-label">Área:</label>
				<select id="plaza_area" name="<%=ControladorContratacion.PARAM_AREA%>" style="width:100%;" required>
					<option value="">----------------</option>
				<%	for(Area are: bean.getListaAreas()) { %>
						<option value="<%=are.getCodNum()%>" <%= Integer.parseInt(area) == are.getCodNum() ? "selected" : "" %>><%= are.getIdAreaExterno() + " " + are.getDescripcion() %></option>
				<% } %>
				</select>
			</div>
			<div class="form-group">
				<label for="plaza_centro_destino" class="bold-label">Centro destino:</label>
				<select id="plaza_centro_destino" name="<%=ControladorContratacion.PARAM_CENTRO_DESTINO%>" style="width:100%;" required>
				<%	for (Entry<String, String> cen: ModeloPlazaOfertada.CENTROS_DESTINO.entrySet()) { %>
						<option value="<%= cen.getKey() %>" <%= centroDestino != null && centroDestino.equals(cen.getKey()) ? "selected" : ""%>><%= cen.getValue() %></option>
				<%	} %>
				</select>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_dedicacion">Dedicación: </label>
				<select id="plaza_dedicacion" name="<%=ControladorContratacion.PARAM_DEDICACION%>" style="width:100%;" <%= personal ? "" : "readonly disabled" %>>
					<option value="">----------------</option>
				<%	for(Dedicacion ded: bean.getListaDedicaciones()) { %>
						<option value="<%=ded.getCodNum()%>" <%= Integer.parseInt(dedicacion) == ded.getCodNum() ? "selected" : "" %>><%= ded.getTexto() %></option>
				<%	} %>
				</select>
			</div>
			<div class="form-group">
				<label for="plaza_cuatrimestre">Cuatrimestre:</label>
				<select id="plaza_cuatrimestre" name="<%=ControladorContratacion.PARAM_CUATRIMESTRE%>" style="width:100%;" <%= personal ? "" : "readonly disabled" %>>
				<%	for (Entry<String, String> cua: ModeloPlazaOfertada.CUATRIMESTRES.entrySet()) { %>
						<option value="<%= cua.getKey() %>" <%= cuatrimestre != null && cuatrimestre.equals(cua.getKey()) ? "selected" : "" %>><%= cua.getValue() %></option>
				<%	} %>
				</select>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plaza_justificacion" class="bold-label">Justificación:</label>
				<textarea class="form-input-custom" id="plaza_justificacion" name="<%=ControladorContratacion.PARAM_JUSTIFICACION %>" rows="3" cols="60" required><%=justificacion%></textarea>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_duracion_prevista">Duración prevista:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_DURACION_PREVISTA%>" id="plaza_duracion_prevista" 
						value="<%=duracionPrevista%>" <%= personal ? "" : "readonly disabled" %>/>
			</div>
			<div class="form-group">
				<label for="plaza_fecha_fin_oferta">Fecha fin oferta:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_FECHA_FIN_OFERTA%>" id="plaza_fecha_fin_oferta" autocomplete="off" 
						value="<%=fechaFinOferta%>" <%= personal ? "" : "readonly disabled" %>/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-file">
				<label for="plaza_horario" style="margin-bottom: .5rem;">Horario:</label>
				<input id="plaza_horario" type="file" name="<%= ControladorContratacion.PARAM_HORARIO %>"/>
			<%	if (plaza.getHorario() != null) { %>
					<button id="plaza_descargar_horario" class="btn icon icon-download" title="Descargar horario de la plaza" type="button" 
						style="margin-top: .5rem;padding: 1px 6px;">Descargar horario</button>
			<%	} %>
			</div>
		<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
			<div class="form-file">
				<label for="plaza_nri" style="margin-bottom: .5rem;">NRI:</label>
				<input id="plaza_nri" type="file" name="<%= ControladorContratacion.PARAM_NRI %>"/>
			<%	if (plaza.getNri() != null) { %>
					<button id="plaza_descargar_nri" class="btn icon icon-download" title="Descargar nri de la plaza" type="button" 
							style="margin-top: .5rem;padding: 1px 6px;">Descargar NRI</button>
			<%	} %>
			</div>
		<%	} %>
		</div>
		<div class="form-group-container col2">
			<div class="form-group"></div>
			<div class="form-group">
				<input id="plaza_enviar" type="submit" name="<%=ControladorContratacion.PARAM_ENVIAR%>" value="Guardar cambios" style="float:right;"/>
			</div>
		</div>
	</form>
	
	<table class="bluetable bolsaempleo" id="tableOfertasCandidatos">
		<tr>
			<th scope="col" style="width:76px">D.N.I</th>
			<th scope="col" style="width:100%" class="nombre">Nombre</th>
			<th scope="col" style="width:76px" class="center">Puntuación</th>
			<th scope="col" style="width:76px" class="center"></th>
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
	$("#plaza_fecha_fin_oferta").datepicker({
		onSelect: function(dateText, inst) {
			$("#plaza_fecha_fin_oferta").val(dateText + " 23:59:59");
		}
	});
	
	var tableOfertasCandidatos = new Atis.DataTable('#tableOfertasCandidatos', {
		"ajax": { url: "<%= ControladorContratacion.URL_PATTERN_AJAX %>" },
		"pageSize": 100,
		"filterable": true,
		"defaultOrderBy": 2,
		"defaultOrderDirection": 'desc',
		"action": "<%= ControladorContratacion.ACCION_DATATABLE_CANDIDATOS %>",
		"params": {'<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>': '<%= plaza.getCodNum() %>'},
		"columns": [
			{'data': 'prsnif', 'filter': true},
			{'data': 'apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
				return row.nombre + " " + row.apellido1 + " " + row.apellido2;
			}},
			{'data': 'total', 'filter': {'type': 'number'}},
			{'data': 'codNum'}
		]
	});
	
	$('#actualizar_plaza').submit(function(event) { 
		$('#plaza_enviar').prop('disabled', true);
		$('#plaza_enviar').attr('value', 'Guardando plaza...');
		return true;
	});
	
	document.getElementById("plaza_descargar_horario").addEventListener("click", function() {
		window.open("<%=ControladorDescargaFicheros.URL_DESCARGA_FICHEROS%>"
				+ "<%="?a=" + (bean.getUsuarioLogeado().isDirectorDepartamento() ? ControladorDescargaFicheros.ACCION_DESCARGAR_HORARIO_DIRECTOR : ControladorDescargaFicheros.ACCION_DESCARGAR_HORARIO_PERSONAL) + "&" + ControladorDescargaFicheros.PARAM_PLAZA_OFERTADA + "=" + plaza.getCodNum()%>");
	});
	
<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
		document.getElementById("plaza_descargar_nri").addEventListener("click", function() {
			window.open("<%=ControladorDescargaFicheros.URL_DESCARGA_FICHEROS%>"
					+ "<%="?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_HORARIO_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_PLAZA_OFERTADA + "=" + plaza.getCodNum()%>");
		});
<%	} %>
	
});

</script>