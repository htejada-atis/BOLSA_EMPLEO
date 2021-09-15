<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloContratacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEstadoCandidato" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="java.util.Map.Entry" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaContratacion bean = (VistaContratacion) uvdatos.getVistas().get(VistaContratacion.class.getName());

PlazaOfertada plaza = bean.getPlazaOfertada();

boolean personal = bean.getUsuarioLogeado().isServicioPersonal();
boolean estadoCreacion = plaza.getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_CREACION);
boolean estadoTramitacion = plaza.getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_TRAMITACION);
boolean estadoAbierta = plaza.getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA);
boolean estadoContratacion = plaza.getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_CONTRATACION);
boolean estadoCerrada = plaza.getEstado().equals(ModeloPlazaOfertada.PLAZA_ESTADO_CERRADA);

boolean mostrarCandidatos = (estadoAbierta || estadoContratacion) && personal;
boolean mostrarCandidatoContratado = !estadoCreacion && !estadoTramitacion && !estadoAbierta;
boolean editable = estadoCreacion || (estadoTramitacion && personal);
boolean extraRequeridos = !estadoCreacion;

String codigo = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CODIGO, EscapaHTML.escapa(plaza.getIdPlaza()));
String area = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_AREA, plaza.getArea().getCodNum().toString());
String cuatrimestre = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CUATRIMESTRE, plaza.getCuatrimestre());
String centroDestino = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CENTRO_DESTINO, EscapaHTML.escapa(plaza.getCentroDestino()));
String dedicacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DEDICACION, plaza.getDedicacion() != null ? plaza.getDedicacion().getCodNum().toString() : "");
String duracionPrevista = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DURACION_PREVISTA, EscapaHTML.escapa(plaza.getDuracionPrevista()));
String fechaFinOferta = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_FECHA_FIN_OFERTA, plaza.getFechaFinOferta() != null ? Formateador.formatoFecha(plaza.getFechaFinOferta(), Formateador.FORMATO_FECHA_DDMMYYYY) : "");
String horaFinOferta = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_HORA_FIN_OFERTA, plaza.getFechaFinOferta() != null ? Formateador.formatoFecha(plaza.getFechaFinOferta(), Formateador.FORMATO_FECHA_HORA_MINUTOS) : "");
String justificacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_JUSTIFICACION, EscapaHTML.escapa(plaza.getJustificacion()));
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Plaza ofertada - Id: <%= plaza.getCodNum() %></h2>
	
	<h4>Estado: <%= plaza.getEstado() %><br/><%= plaza.getFechaAbierta() != null ? "Fecha abierta: " + Formateador.formatoFecha(plaza.getFechaAbierta(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "" %></h4>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_plaza" class="be-form" method="post" action="<%=request.getRequestURI()%>" enctype="multipart/form-data">
		<input type="hidden" name="<%=ControladorContratacion.PARAM_ACCION%>" id="accion_formulario"
				value="<%=ControladorContratacion.ACCION_EDITAR_PLAZA_OFERTADA%>" />
		<input type="hidden" name="<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>" id="plaza_id" 
				value="<%= plaza.getCodNum()%>" />
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_codigo" <%= extraRequeridos ? "class='bold-label'" : "" %>>Código:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_CODIGO%>" id="plaza_codigo" value="<%=codigo%>"
						<%= editable ? "" : "readonly disabled " %>
						<%= extraRequeridos ? "required " : "" %>/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_area" class="bold-label">Área:</label>
				<select id="plaza_area" name="<%=ControladorContratacion.PARAM_AREA%>" style="width:100%;" required <%= editable ? "" : "readonly disabled " %>>
					<option value="">----------------</option>
				<%	for(Area are: bean.getListaAreas()) { %>
						<option value="<%=are.getCodNum()%>" <%= Integer.parseInt(area) == are.getCodNum() ? "selected" : "" %>><%= are.getIdAreaExterno() + " " + are.getDescripcion() %></option>
				<%	} %>
				</select>
			</div>
			<div class="form-group">
				<label for="plaza_centro_destino" class="bold-label">Centro destino:</label>
				<select id="plaza_centro_destino" name="<%=ControladorContratacion.PARAM_CENTRO_DESTINO%>" style="width:100%;" required <%= editable ? "" : "readonly disabled " %>>
				<%	for (Entry<String, String> cen: ModeloPlazaOfertada.CENTROS_DESTINO.entrySet()) { %>
						<option value="<%= cen.getKey() %>" <%= centroDestino != null && centroDestino.equals(cen.getKey()) ? "selected" : ""%>><%= cen.getValue() %></option>
				<%	} %>
				</select>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_dedicacion" <%= extraRequeridos ? "class='bold-label'" : "" %>>Dedicación: </label>
				<select id="plaza_dedicacion" name="<%=ControladorContratacion.PARAM_DEDICACION%>" style="width:100%;" 
						<%= editable ? "" : "readonly disabled " %>
						<%= extraRequeridos ? "required " : "" %>>
					<option value="">----------------</option>
				<%	if (plaza.getDedicacion() != null) { %>
						<option value="<%= plaza.getDedicacion().getCodNum() %>" selected><%= plaza.getDedicacion().getTexto() + " - Sueldo: " + plaza.getDedicacion().getSueldo() %></option>
				<%	} %>
				<%	for(Dedicacion ded: bean.getListaDedicaciones()) { %>
					<%	if (plaza.getDedicacion() != null && Integer.parseInt(dedicacion) != ded.getCodNum()) { %>
							<option value="<%=ded.getCodNum()%>"><%= ded.getTexto() + " - Sueldo: " + ded.getSueldo() %></option>
					<%	} else if (plaza.getDedicacion() == null) { %>
							<option value="<%=ded.getCodNum()%>"><%= ded.getTexto() + " - Sueldo: " + ded.getSueldo() %></option>
					<%	} %>
				<%	} %>
				</select>
			</div>
			<div class="form-group">
				<label for="plaza_cuatrimestre" <%= extraRequeridos ? "class='bold-label'" : "" %>>Cuatrimestre:</label>
				<select id="plaza_cuatrimestre" name="<%=ControladorContratacion.PARAM_CUATRIMESTRE%>" style="width:100%;" 
						<%= editable ? "" : "readonly disabled " %>
						<%= extraRequeridos ? "required " : "" %>>
					<option value="">----------------</option>
				<%	for (Entry<String, String> cua: ModeloPlazaOfertada.CUATRIMESTRES.entrySet()) { %>
						<option value="<%= cua.getKey() %>" <%= cuatrimestre != null && cuatrimestre.equals(cua.getKey()) ? "selected" : "" %>><%= cua.getValue() %></option>
				<%	} %>
				</select>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plaza_justificacion" class="bold-label">Justificación:</label>
				<textarea class="form-input-custom" id="plaza_justificacion" name="<%=ControladorContratacion.PARAM_JUSTIFICACION %>" rows="3" cols="60" required <%= editable ? "" : "readonly disabled " %>><%=justificacion%></textarea>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_duracion_prevista" <%= extraRequeridos ? "class='bold-label'" : "" %>>Duración prevista:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_DURACION_PREVISTA%>" id="plaza_duracion_prevista" 
						value="<%=duracionPrevista%>" <%= editable ? "" : "readonly disabled " %> <%= extraRequeridos ? "required " : "" %>/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_fecha_fin_oferta" <%= extraRequeridos ? "class='bold-label'" : "" %>>Fecha fin oferta:</label>
				<input class="form-input-custom" name="<%=ControladorContratacion.PARAM_FECHA_FIN_OFERTA%>" id="plaza_fecha_fin_oferta" type="text" autocomplete="off" value="<%= fechaFinOferta %>"
						<%= extraRequeridos ? "required " : "" %> <%= editable ? "" : "readonly disabled " %>/>
			</div>
			<div class="form-group">
				<label for="plaza_hora_fin_oferta" <%= extraRequeridos ? "class='bold-label'" : "" %>>Hora fin oferta:</label>
				<input class="form-input-custom" name="<%=ControladorContratacion.PARAM_HORA_FIN_OFERTA%>" id="plaza_hora_fin_oferta" type="text" autocomplete="off" value="<%= horaFinOferta %>"
						<%= extraRequeridos ? "required " : "" %> <%= editable ? "" : "readonly disabled " %> placeholder="HH:MM:SS"/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-file">
				<label for="plaza_horario" style="margin-bottom: .5rem; width: 100%!important; text-align: left;">Horario:</label>
			<%	if (editable) { %>
					<input id="plaza_horario" type="file" name="<%= ControladorContratacion.PARAM_HORARIO %>"/>
			<%	} %>
			<%	if (plaza.getHorario() != null) { %>
					<br/>
					<button id="plaza_descargar_horario" class="btn icon icon-download" title="Descargar horario de la plaza" type="button" 
						style="margin-top: .5rem;padding: 1px 6px;">Descargar horario</button>
			<%	} %>
			</div>
		<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
				<div class="form-file">
					<label for="plaza_nri" style="margin-bottom: .5rem; width: 100%!important; text-align: left;">NRI:</label>
			<%	if (editable) { %>
					<input id="plaza_nri" type="file" name="<%= ControladorContratacion.PARAM_NRI %>"/>
			<%	} %>
			
			<%	if (plaza.getNri() != null) { %>
					<button id="plaza_descargar_nri" class="btn icon icon-download" title="Descargar nri de la plaza" type="button" 
							style="margin-top: .5rem;padding: 1px 6px;">Descargar NRI</button>
			<%	} %>
				</div>
		<%	} %>
		</div>
		<br/>
		<div class="form-group-container col2">
			<div class="form-group">
		<%	if (estadoCreacion) { %>
				<button id="plaza_eliminar" style="float:left;" <%= personal ? "" : "disabled" %>>Eliminar</button>
		<%	} %>
			</div>
			<div class="form-group">
		<%	if (editable) { %>
				<input id="plaza_enviar" type="submit" name="<%=ControladorContratacion.PARAM_ENVIAR%>" value="Guardar cambios" style="float:right;"/>
		<%	} %>
		<%	if (estadoCreacion) { %>
				<button id="plaza_estado_tramitacion" style="float:right; margin-right: 12px;" <%= personal ? "" : "disabled" %>>Tramitación</button>
		<%	} else if (estadoTramitacion) { %>
				<button id="plaza_estado_abierta" style="float:right; margin-right: 12px;" <%= personal ? "" : "disabled" %>>Abrir plaza</button>
		<%	} %>
			</div>
		</div>
	</form>
	
<%	if ((mostrarCandidatos || estadoCerrada) && personal) { %>
		<button class="link-btn" id="exportar_candidatos" title="Exportar candidatos a csv">Exportar candidatos a csv</button>
<%	} %>
	
<%	if (mostrarCandidatos) { %>
		<table class="bluetable bolsaempleo" id="tableOfertasCandidatos">
			<tr>
				<th scope="col" style="width:76px">D.N.I</th>
				<th scope="col" style="width:100%" class="nombre">Nombre</th>
				<th scope="col" style="width:76px" class="center">Puntuación</th>
				<th scope="col" style="width:76px" class="center">Confirmación</th>
				<th scope="col" style="width:76px">Fecha confirmación</th>
				<th scope="col" style="width:76px" class="center"></th>
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="6" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
		<br/>
<%	} %>

<%	if ((estadoContratacion || estadoCerrada) && personal) { %>
		<table class="bluetable bolsaempleo" id="tableCandidatosContrato">
			<tr>
				<th scope="col" style="width:76px">D.N.I</th>
				<th scope="col" style="width:100%" class="nombre">Nombre</th>
				<th scope="col" style="width:100px">Fecha cita</th>
				<th scope="col" style="width:100px">Resultado cita</th>
				<th scope="col" style="width:100px">Fecha resultado</th>
				<th scope="col" style="width:76px" class="center"></th>
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="6" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
<%	} %>

<%	if (estadoContratacion && personal) { %>
		<button id="plaza_estado_cerrar" style="float:right;">Cerrar plaza</button>
<%	} %>
	
</div>

<script>

$(document).ready(function() {
	
	$("#plaza_fecha_fin_oferta").datepicker();
	
<%	if (mostrarCandidatos) { %>
		var tableOfertasCandidatos = new Atis.DataTable('#tableOfertasCandidatos', {
			"ajax": { url: "<%= ControladorContratacion.URL_PATTERN_AJAX %>", async: false },
			"pageSize": 100,
			"filterable": true,
			"defaultOrderBy": 2,
			"defaultOrderDirection": 'desc',
			"title": 'Candidatos disponibles para la plaza',
			"action": "<%= ControladorContratacion.ACCION_DATATABLE_CANDIDATOS %>",
			"params": {'<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>': '<%= plaza.getCodNum() %>'},
			"columns": [
				{'data': 'candidato.prsnif', 'filter': true, 'render': function(row) {
					var href = 'href="<%= ControladorUsuarioCandidato.URL_PATTERN + "?" + ControladorUsuarioCandidato.PARAM_ACCION + "=" + ControladorUsuarioCandidato.ACCION_SELECCIONAR_CANDIDATO + "&" + ControladorUsuarioCandidato.PARAM_CANDIDATO + "=" %>' + row.candidato.codNum + '"';
					return '<a class="bolsaempleo-link" ' + href + ' target="_blank" title="Ir al perfil del candidato ' + row.candidato.prsnif + '">' + row.candidato.prsnif + '</a>';
				}},
				{'data': 'candidato.apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
					return row.candidato.nombre + " " + row.candidato.apellido1 + " " + row.candidato.apellido2;
				}},
				{'data': 'puntuacion', 'filter': {'type': 'number'}},
				{'data': 'resultado', 'render': function(row) {
					if (row.resultado == true) {
						return "<div title='Aceptada' class='circle-true'></div>";
					} else if (row.resultado == false) {
						return "<div title='Rechazada' class='circle-false'></div>";
					}
				}},
				{'data': 'fechaResultado', 'filter': {'type': 'date'}},
				{'data': 'codNum', 'buttons': [{'label': 'Contratar', 'onClick': function(row) {
							var mensaje = 
								"<h2>Candidato " + row.candidato.prsnif + "</h2>"
								+ " <br/>"
								+ " <p></p>"
								+ " <p>Se enviará un correo al candidato con la confirmación del contrato <br/> y con la fecha y hora prevista de la cita que se establece en el formulario:</p>"
								+ " <br/>"
								+ " <div class='form-group-container col2'>"
								+ "     <div class='form-group-dialog'>"
								+ "         <label class='bold-label' for='contrato_fecha_cita'>Fecha cita: </label>"
								+ "         <input id='contrato_fecha_cita' name='<%= ControladorContratacion.PARAM_FECHA_CITA %>' placeholder='DD/MM/YYYY' required/>"
								+ "     </div>"
								+ "     <div class='form-group-dialog'>"
								+ "         <label class='bold-label' for='contrato_hora_cita'>Hora cita: </label><input id='contrato_hora_cita' name='horacita' placeholder='HH:MM:SS' required/>"
								+ "     </div>"
								+ " </div>"
								+ " <br/>";
							
							$('<div></div>').appendTo('body').html(mensaje).dialog({
								modal: true,
								title: "Contratar candidato",
								zIndex: 10000,
								autoOpen: true,
								width: 'auto',
								resizable: false,
								buttons: {
									Si: function() {
										var inputFecha = this.querySelector('#contrato_fecha_cita');
										var inputHora = this.querySelector('#contrato_hora_cita');
										
										if (inputFecha.value != '' && inputHora.value != '') {
											var params = {
													"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_CONTRATAR_CANDIDATO %>",
													"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>,
													"<%= ControladorContratacion.PARAM_CANDIDATO %>": row.candidato.codNum,
													"<%= ControladorContratacion.PARAM_FECHA_CITA %>": inputFecha.value,
													"<%= ControladorContratacion.PARAM_HORA_CITA %>": inputHora.value
											};
											Atis.sendForm("<%= request.getRequestURI() %>", params);
											$(this).dialog("close");
										} else {
											if (inputFecha.value == '') {
												inputFecha.setCustomValidity("La fecha de cita no puede estar vacía");
												inputFecha.reportValidity();
											}
											
											if (inputHora.value == '') {
												inputHora.setCustomValidity("La hora de cita no puede estar vacía");
												inputHora.reportValidity();
											}
										}
									},
									No: function() {
										$(this).dialog("close");
									}
								},
								close: function(event, ui) {
									$(this).remove();
								},
								open: function(event, ui) {
									$("#contrato_fecha_cita").datepicker();
									$("#contrato_fecha_cita").blur();
									
									$("#ui-datepicker-div").css("z-index", "9999");
								}
							});
						}, 'visible': function(row) {
							return row.resultado && row.plaza.estado == '<%= ModeloPlazaOfertada.PLAZA_ESTADO_CONTRATACION %>' && row.contratacion == null;
						}
					}, {'label': 'Preferencias', 'onClick': function(row) {
							var btn = this;
							btn.attr('disabled', true);
							var params = {
									'<%= ControladorContratacion.PARAM_ACCION %>': '<%= ControladorContratacion.ACCION_PREFERENCIAS_CANDIDATO %>',
									'<%= ControladorContratacion.PARAM_CANDIDATO %>': row.candidato.codNum
							}
							Atis.sendAjax("<%= request.getRequestURI() %>", params, function(data) {
								var message = 'En la siguiente tabla se muestra una lista de las plazas aceptadas por el candidato:<br/>';
								message += row.candidato.prsnif + ' - ' + row.candidato.nombre + ' ' + row.candidato.apellido1 + ' ' + row.candidato.apellido2 + '<br/>';
								message += '<table class="bluetable bolsaempleo" style="margin-top: 10px; width: 450px;">';
								message += '<tr>';
								message += '<th scope="col" style="width:50px">Id plaza</th>';
								message += '<th scope="col" style="width:100%">Área</th>';
								message += '<th scope="col" style="width:60px">Preferencia</th>';
								message += '</tr>';
								
								if (Array.isArray(data) && data.length > 0) {
									data.forEach(function(element) {
										message += '<tr>';
										message += '<td>' + element.plaza.codNum + '</td>';
										message += '<td>' + element.plaza.area.idAreaExterno + ' ' + element.plaza.area.descripcion + '</td>';
										message += '<td>' + element.preferencia + '</td>';
										message += '</tr>';
									});
								} else {
									message += '<tr>';
									message += '<td colSpan="3" style="width:100%">Sin resultados</td>';
									message += '</tr>';
								}
								
								message += '<tr>';
								message += '<th colSpan="3" style="width:100%"></th>';
								message += '</tr>';
								
								Atis.alertDialog("Plazas aceptadas por preferencia", message);
								btn.attr('disabled', false);
							}, function(data) {
								var response = JSON.parse(data.responseText);
								// alert en caso de error
								Atis.alertDialog("Error en la solicitud", response.descripcion);
								btn.attr('disabled', false);
							});
						}, 'visible': function(row) {
							return row.resultado;
						}
					}]
				}
			]
		});
<%	} %>

<%	if ((mostrarCandidatos || estadoCerrada) && personal) { %>
		$('#exportar_candidatos').on('click', function() {
			window.open("<%= request.getRequestURI() %>?<%= ControladorContratacion.PARAM_ACCION %>=<%= ControladorContratacion.ACCION_EXPORTAR_CANDIDATOS %>&<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>=<%= plaza.getCodNum() %>");
		});
<%	} %>

<%	if ((estadoContratacion || estadoCerrada) && personal) { %>

		var tableCandidatosContrato = new Atis.DataTable('#tableCandidatosContrato', {
			"ajax": { url: "<%= ControladorContratacion.URL_PATTERN_AJAX %>", async: false },
			"action": "<%= ControladorContratacion.ACCION_DATATABLE_CANDIDATOS_CONTRATO %>",
			"params": {'<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>': '<%= plaza.getCodNum() %>'},
			"pageSize": 10,
			"filterable": true,
			"title": 'Candidatos con contratación',
			"columns": [
				{'data': 'candidato.prsnif', 'filter': true, 'render': function(row) {
					var href = 'href="<%= ControladorUsuarioCandidato.URL_PATTERN + "?" + ControladorUsuarioCandidato.PARAM_ACCION + "=" + ControladorUsuarioCandidato.ACCION_SELECCIONAR_CANDIDATO + "&" + ControladorUsuarioCandidato.PARAM_CANDIDATO + "=" %>' + row.candidato.codNum + '"';
					return '<a class="bolsaempleo-link" ' + href + ' target="_blank" title="Ir al perfil del candidato ' + row.candidato.prsnif + '">' + row.candidato.prsnif + '</a>';
				}},
				{'data': 'candidato.apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
					return row.candidato.nombre + " " + row.candidato.apellido1 + " " + row.candidato.apellido2;
				}},
				{'data': 'contratacion.fechaCita', 'filter': {'type': 'date'}},
				{'data': 'contratacion.resultado', 'filter': true},
				{'data': 'contratacion.fechaResultado', 'filter': {'type': 'date'}},
				{'data': 'codNum', 'buttons': [{'label': 'Rechazar', 'onClick': function(row) {
						Atis.confirmDialog("Rechazar contratación", "Se rechazará la contratación del candidato " + row.candidato.prsnif + ".", {
							'Si': function() {
								var params = {
										"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_RECHAZAR_CONTRATACION_CANDIDATO %>",
										"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>,
										"<%= ControladorContratacion.PARAM_CANDIDATO %>": row.candidato.codNum
								};
								Atis.sendForm("<%= request.getRequestURI() %>", params);
								$(this).dialog("close");
							},
							'No': function() {
								$(this).dialog("close");
							}
						});
					}, 'visible': function(row) {
						return row.contratacion.resultado != '<%= ModeloContratacion.RESULTADO_RECHAZADA %>' && <%= !estadoCerrada %>;
					}
				}, {'label': 'Reenviar cita', 'onClick': function(row) {
						var mensaje = 
							"<h2>Candidato " + row.candidato.prsnif + "</h2>"
							+ " <br/>"
							+ " <p></p>"
							+ " <p>Se enviará un correo al candidato con la nueva cita para la confirmación del contrato <br/> y con la fecha y hora prevista de la cita que se establece en el formulario:</p>"
							+ " <br/>"
							+ " <div class='form-group-container col2'>"
							+ "     <div class='form-group-dialog'>"
							+ "         <label class='bold-label' for='contrato_fecha_cita'>Fecha cita: </label>"
							+ "         <input id='contrato_fecha_cita' name='<%= ControladorContratacion.PARAM_FECHA_CITA %>' placeholder='DD/MM/YYYY' required/>"
							+ "     </div>"
							+ "     <div class='form-group-dialog'>"
							+ "         <label class='bold-label' for='contrato_hora_cita'>Hora cita: </label><input id='contrato_hora_cita' name='horacita' placeholder='HH:MM:SS' required/>"
							+ "     </div>"
							+ " </div>"
							+ " <br/>";
						
						$('<div></div>').appendTo('body').html(mensaje).dialog({
							modal: true,
							title: "Reenviar cita",
							zIndex: 10000,
							autoOpen: true,
							width: 'auto',
							resizable: false,
							buttons: {
								Si: function() {
									var inputFecha = this.querySelector('#contrato_fecha_cita');
									var inputHora = this.querySelector('#contrato_hora_cita');
									
									if (inputFecha.value != '' && inputHora.value != '') {
										var params = {
												"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_REENVIAR_CITA_CONTRATACION %>",
												"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>,
												"<%= ControladorContratacion.PARAM_CANDIDATO %>": row.candidato.codNum,
												"<%= ControladorContratacion.PARAM_FECHA_CITA %>": inputFecha.value,
												"<%= ControladorContratacion.PARAM_HORA_CITA %>": inputHora.value
										};
										Atis.sendForm("<%= request.getRequestURI() %>", params);
										$(this).dialog("close");
									} else {
										if (inputFecha.value == '') {
											inputFecha.setCustomValidity("La fecha de cita no puede estar vacía");
											inputFecha.reportValidity();
										}
										
										if (inputHora.value == '') {
											inputHora.setCustomValidity("La hora de cita no puede estar vacía");
											inputHora.reportValidity();
										}
									}
								},
								No: function() {
									$(this).dialog("close");
								}
							},
							close: function(event, ui) {
								$(this).remove();
							},
							open: function(event, ui) {
								$("#contrato_fecha_cita").datepicker();
								$("#contrato_fecha_cita").blur();
								
								$("#ui-datepicker-div").css("z-index", "9999");
							}
						});
					}, 'visible': function(row) {
						return <%= !estadoCerrada %>;
					}
				}]}
			]
		});

<%	} %>

	
	$('#actualizar_plaza').submit(function(event) {
		$('#plaza_enviar').prop('disabled', true);
		$('#plaza_enviar').attr('value', 'Guardando plaza...');
		return true;
	});
	
<%	if (estadoCreacion && personal) { %>
		document.getElementById("plaza_estado_tramitacion").addEventListener("click", function(e) {
			e.preventDefault();
			Atis.confirmDialog("Cambiar estado de la plaza", "La plaza cambiará a estado de tramitación.", {
				'Si': function() {
					var params = {
							"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_PLAZA_TRAMITACION %>",
							"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>
					};
					Atis.sendForm("<%= request.getRequestURI() %>", params);
					$(this).dialog("close");
				},
				'No': function() {
					$(this).dialog("close");
				}
			});
		});
		
		document.getElementById("plaza_eliminar").addEventListener("click", function(e) {
			e.preventDefault();
			Atis.confirmDialog("Eliminar plaza", "¿Desea eliminar la plaza?", {
				'Si': function() {
					var params = {
							"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_ELIMINAR_PLAZA %>",
							"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>
					};
					Atis.sendForm("<%= request.getRequestURI() %>", params);
					$(this).dialog("close");
				},
				'No': function() {
					$(this).dialog("close");
				}
			});
		});
<%	} %>
	
<%	if (estadoTramitacion && personal) { %>
		document.getElementById("plaza_estado_abierta").addEventListener("click", function(e) {
			e.preventDefault();
			var enviar = true;
			var inputs = document.getElementById("actualizar_plaza").elements;
			
			for (i=0; i<inputs.length; i++) {
				var input = inputs[i];
				if (input.required && input.value.length == 0) {
					input.reportValidity();
					enviar = false;
				}
			}
			
			if (enviar) {
				var message = '<p>Una vez abierta la plaza ya no se podrá modificar y comenzará el período de aceptación de los<br/> candidatos.<br/>';
				message += 'En la fecha: <%= fechaFinOferta %> y hora: <%= horaFinOferta %> el estado de la plaza cambiará automáticamente a estado<br/> de contratación.</p><br/>';
				message += '<p>Se enviará un correo sobre la apertura de la plaza a los siguientes candidatos:</p>';
				message += '<table id="tablaCandidatosApertura" class="bluetable bolsaempleo" style="margin-top: 10px; width: 525px;">';
				message += '    <tr>';
				message += '        <th scope="col" style="width:65px">D.N.I</th>';
				message += '        <th scope="col" style="width:100%">Nombre</th>';
				message += '        <th scope="col" style="width:120px" class="center">Estado</th>';
				message += '        <th scope="col" style="width:80px" class="center">Disponible</th>';
				message += '        <th scope="col" style="width:55px" class="center">Contratos</th>';
				message += '    </tr>';
				message += '    <tbody>';
				message += '    </tbody>';
				message += '    <tfoot>';
				message += '        <tr>';
				message += '            <th colSpan="5" style="width:100%"></th>';
				message += '        </tr>';
				message += '    </tfoot>';
				message += '</table>';
				
				$('<div></div>').appendTo('body').html(message).dialog({
					modal: true,
					title: "Abrir plaza",
					zIndex: 10000,
					autoOpen: true,
					width: 'auto',
					resizable: false,
					buttons: {
						Si: function() {
							var params = {
									"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_PLAZA_ABIERTA %>",
									"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>
							};
							Atis.sendForm("<%= request.getRequestURI() %>", params);
						},
						No: function() {
							$(this).dialog("close");
						}
					},
					close: function(event, ui) {
						$(this).remove();
					},
					open: function(event, ui) {
						var table = new Atis.DataTable('#tablaCandidatosApertura', {
							"ajax": { url: "<%= ControladorContratacion.URL_PATTERN_AJAX %>" },
							"action": "<%= ControladorContratacion.ACCION_DATATABLE_CANDIDATOS_APERTURA %>",
							"params": {'<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>': '<%= plaza.getCodNum() %>'},
							"pageSize": 10,
							"columns": [
								{'data': 'prsnif', 'order': false, 'render': function(row) {
									var href = 'href="<%= ControladorUsuarioCandidato.URL_PATTERN + "?" + ControladorUsuarioCandidato.PARAM_ACCION + "=" + ControladorUsuarioCandidato.ACCION_SELECCIONAR_CANDIDATO + "&" + ControladorUsuarioCandidato.PARAM_CANDIDATO + "=" %>' + row.codNum + '"';
									return '<a class="bolsaempleo-link" ' + href + ' target="_blank" title="Ir al perfil del candidato ' + row.prsnif + '">' + row.prsnif + '</a>';
								}},
								{'data': 'nombre', 'order': false, 'render': function(row) {
									return row.nombre + " " + row.apellido1 + " " + row.apellido2;
								}},
								{'data': 'estado', 'order': false, 'render': function(row) {
									return row.codNumEstado != 0 ? row.estado : '<%= ModeloEstadoCandidato.ESTADO_DISPONIBLE %>';
								}},
								{'data': 'disponibilidad', 'order': false, 'render': function(row) {
									if(row.disponibilidad){
										return "<div title='Disponible para la plaza' class='circle-true'></div>";
									} else{
										return "<div title='No disponible para la plaza' class='circle-false'></div>";
									}
								}},
								{'data': 'contratos', 'order': false, 'class': 'center'}
							]
						});
					}
				});
			}
		});
<%	} %>

<%	if (plaza.getHorario() != null) { %>
		document.getElementById("plaza_descargar_horario").addEventListener("click", function() {
			window.open("<%=ControladorDescargaFicheros.URL_DESCARGA_FICHEROS%>"
					+ "<%="?a=" + (bean.getUsuarioLogeado().isDirectorDepartamento() ? ControladorDescargaFicheros.ACCION_DESCARGAR_HORARIO_DIRECTOR : ControladorDescargaFicheros.ACCION_DESCARGAR_HORARIO_PERSONAL) + "&" + ControladorDescargaFicheros.PARAM_PLAZA_OFERTADA + "=" + plaza.getCodNum()%>");
		});
<%	} %>

<%	if (plaza.getNri() != null && personal) { %>
		document.getElementById("plaza_descargar_nri").addEventListener("click", function() {
			window.open("<%=ControladorDescargaFicheros.URL_DESCARGA_FICHEROS%>"
					+ "<%="?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_NRI_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_PLAZA_OFERTADA + "=" + plaza.getCodNum()%>");
		});
<%	} %>

<%	if (estadoContratacion && personal) { %>
		document.getElementById("plaza_estado_cerrar").addEventListener("click", function() {
			var btn = $(this);
			btn.attr('disabled', true);
			var params = {
					"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_COMPROBAR_CONTRATACION_PLAZA %>",
					"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>
			}
			Atis.sendAjax("<%= request.getRequestURI() %>", params, function(data) {
				data = JSON.parse(data);
				var title = "";
				var message = "";
				var params = {
						"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_PLAZA_CERRADA %>",
						"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>
				};
				
				if (data.result == true) {
					title = "Cierre de la plaza";
					message = " <p>Se enviará un mensaje a los emails especificados a continuación con el candidato<br/>";
					message += "seleccionado para la plaza.</p><br/>"
					message += " <div class='form-group-container col2'>";
					message += "     <div class='form-group-dialog'>";
					message += "         <label class='bold-label' for='emails_cierre'>Emails destinatarios: </label>";
					message += "         <input id='emails_cierre' name='<%= ControladorContratacion.PARAM_EMAILS_CIERRE %>' value='" + data.emails + "' required/>";
					message += "     </div>";
					message += " </div>";
					message += " <br/>";
				} else {
					title = "Cierre de plaza desierta";
					message = "<p>No hay ningún candidato con contratación aceptada.<br/>";
					message += "Se cerrará la plaza sin contratación.</p>"
				}
				
				Atis.confirmDialog(title, message, {
					'Si': function() {
						if (data.result == true) {
							params["<%= ControladorContratacion.PARAM_EMAILS_CIERRE %>"] = this.querySelector('#emails_cierre').value;
						}
						
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
				});
				
				btn.attr('disabled', false);
			}, function(data) {
				var response = JSON.parse(data.responseText);
				// alert en caso de error
				Atis.alertDialog("Error en la solicitud", response.descripcion);
				btn.attr('disabled', false);
			});
			
		});
<%	} %>
	
});

</script>