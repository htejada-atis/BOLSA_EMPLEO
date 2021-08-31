<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
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
boolean tablaCandidatos = !plaza.getEstado().contains(ModeloPlazaOfertada.PLAZA_ESTADO_CREACION) && !plaza.getEstado().contains(ModeloPlazaOfertada.PLAZA_ESTADO_TRAMITACION)  && personal;
boolean editable = plaza.getEstado().contains(ModeloPlazaOfertada.PLAZA_ESTADO_CREACION) || (plaza.getEstado().contains(ModeloPlazaOfertada.PLAZA_ESTADO_TRAMITACION) && personal);

String area = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_AREA, plaza.getArea().getCodNum().toString());
String dedicacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DEDICACION, plaza.getDedicacion() != null ? plaza.getDedicacion().getCodNum().toString() : "");
String justificacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_JUSTIFICACION, EscapaHTML.escapa(plaza.getJustificacion()));
String duracionPrevista = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DURACION_PREVISTA, EscapaHTML.escapa(plaza.getDuracionPrevista()));
String cuatrimestre = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CUATRIMESTRE, plaza.getCuatrimestre());
String centroDestino = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CENTRO_DESTINO, EscapaHTML.escapa(plaza.getCentroDestino()));
String fechaFinOferta = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_FECHA_FIN_OFERTA, plaza.getFechaFinOferta() != null ? Formateador.formatoFecha(plaza.getFechaFinOferta(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "");
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Editar plaza ofertada</h2>
	
	<h4>Id: <%= plaza.getCodNum() %><br/>Estado: <%= plaza.getEstado() %><br/><%= plaza.getFechaAbierta() != null ? "Fecha abierta: " + Formateador.formatoFecha(plaza.getFechaAbierta(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "" %></h4>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_plaza" class="be-form" method="post" action="<%=request.getRequestURI()%>" enctype="multipart/form-data">
		<input type="hidden" name="<%=ControladorContratacion.PARAM_ACCION%>" id="accion_formulario"
				value="<%=ControladorContratacion.ACCION_EDITAR_PLAZA_OFERTADA%>" />
		<input type="hidden" name="<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>" id="plaza_id" 
				value="<%= plaza.getCodNum()%>" />
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_area" class="bold-label">Área:</label>
				<select id="plaza_area" name="<%=ControladorContratacion.PARAM_AREA%>" style="width:100%;" required <%= editable ? "" : "readonly disabled" %>>
					<option value="">----------------</option>
				<%	for(Area are: bean.getListaAreas()) { %>
						<option value="<%=are.getCodNum()%>" <%= Integer.parseInt(area) == are.getCodNum() ? "selected" : "" %>><%= are.getIdAreaExterno() + " " + are.getDescripcion() %></option>
				<%	} %>
				</select>
			</div>
			<div class="form-group">
				<label for="plaza_centro_destino" class="bold-label">Centro destino:</label>
				<select id="plaza_centro_destino" name="<%=ControladorContratacion.PARAM_CENTRO_DESTINO%>" style="width:100%;" required <%= editable ? "" : "readonly disabled" %>>
				<%	for (Entry<String, String> cen: ModeloPlazaOfertada.CENTROS_DESTINO.entrySet()) { %>
						<option value="<%= cen.getKey() %>" <%= centroDestino != null && centroDestino.equals(cen.getKey()) ? "selected" : ""%>><%= cen.getValue() %></option>
				<%	} %>
				</select>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_dedicacion">Dedicación: </label>
				<select id="plaza_dedicacion" name="<%=ControladorContratacion.PARAM_DEDICACION%>" style="width:100%;" <%= personal && editable ? "" : "readonly disabled" %>>
					<option value="">----------------</option>
				<%	for(Dedicacion ded: bean.getListaDedicaciones()) { %>
						<option value="<%=ded.getCodNum()%>" <%= !dedicacion.isEmpty() && Integer.parseInt(dedicacion) == ded.getCodNum() ? "selected" : "" %>><%= ded.getTexto() + " - Sueldo: " + ded.getSueldo() %></option>
				<%	} %>
				</select>
			</div>
			<div class="form-group">
				<label for="plaza_cuatrimestre">Cuatrimestre:</label>
				<select id="plaza_cuatrimestre" name="<%=ControladorContratacion.PARAM_CUATRIMESTRE%>" style="width:100%;" <%= personal && editable ? "" : "readonly disabled" %>>
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
				<textarea class="form-input-custom" id="plaza_justificacion" name="<%=ControladorContratacion.PARAM_JUSTIFICACION %>" rows="3" cols="60" required <%= editable ? "" : "readonly disabled" %>><%=justificacion%></textarea>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_duracion_prevista">Duración prevista:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_DURACION_PREVISTA%>" id="plaza_duracion_prevista" 
						value="<%=duracionPrevista%>" <%= personal && editable ? "" : "readonly disabled" %>/>
			</div>
			<div class="form-group">
				<label for="plaza_fecha_fin_oferta">Fecha fin oferta:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_FECHA_FIN_OFERTA%>" id="plaza_fecha_fin_oferta" autocomplete="off" 
						value="<%=fechaFinOferta%>" <%= personal && editable ? "" : "readonly disabled" %>/>
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
		<div class="form-group-container col2">
			<div class="form-group"></div>
			<div class="form-group">
			<%	if (editable) { %>
					<input id="plaza_enviar" type="submit" name="<%=ControladorContratacion.PARAM_ENVIAR%>" value="Guardar cambios" style="float:right;"/>
					<button id="plaza_estado_abierta" style="float:right; margin-right: 12px;" <%= plaza.getEstado().contains(ModeloPlazaOfertada.PLAZA_ESTADO_TRAMITACION) ? "" : "disabled" %>>Abrir plaza</button>
			<%	} %>
			</div>
		</div>
	</form>
	
<%	if (tablaCandidatos) { %>
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
		
<%	} %>
	
</div>

<script>

$(document).ready(function() {
	$("#plaza_fecha_fin_oferta").datepicker({
		onSelect: function(dateText, inst) {
			$("#plaza_fecha_fin_oferta").val(dateText + " 23:59:59");
		}
	});
	
<%	if (tablaCandidatos) { %>
		var tableOfertasCandidatos = new Atis.DataTable('#tableOfertasCandidatos', {
			"ajax": { url: "<%= ControladorContratacion.URL_PATTERN_AJAX %>" },
			"pageSize": 100,
			"filterable": true,
			"defaultOrderBy": 2,
			"defaultOrderDirection": 'desc',
			"title": 'Candidatos que han aceptado la plaza',
			"action": "<%= ControladorContratacion.ACCION_DATATABLE_CANDIDATOS %>",
			"params": {'<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>': '<%= plaza.getCodNum() %>'},
			"columns": [
				{'data': 'prsnif', 'filter': true},
				{'data': 'apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
					return row.nombre + " " + row.apellido1 + " " + row.apellido2;
				}},
				{'data': 'total', 'filter': {'type': 'number'}},
				{'data': 'codNum', 'buttons': [{'label': 'Contratar', 'onClick': function(row) {
							var mensaje = 
								"<h2>Candidato " + row.prsnif + "</h2>"
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
													"<%= ControladorContratacion.PARAM_CANDIDATO %>": row.codNum,
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
						}
					}]
				}
			]
		});
<%	} %>
	
	$('#actualizar_plaza').submit(function(event) {
		$('#plaza_enviar').prop('disabled', true);
		$('#plaza_enviar').attr('value', 'Guardando plaza...');
		return true;
	});
	
<%	if (editable) { %>
		document.getElementById("plaza_estado_abierta").addEventListener("click", function(e) {
			e.preventDefault();
			var inputFechaFin = document.getElementById("plaza_fecha_fin_oferta");
			console.log(inputFechaFin.value);
			if (inputFechaFin.value.length == 0) {
				inputFechaFin.setCustomValidity("La fecha fin de la oferta no puede estar vacía para abrir la plaza");
				inputFechaFin.reportValidity();
			} else {
				Atis.confirmDialog("¿Abrir plaza?", "Una vez abierta la plaza no se podrá editar y se cerrará automáticamente en la fecha fin de la oferta indicada.", {
					'Si': function(row) {
						var params = {
								"<%= ControladorContratacion.PARAM_ACCION %>": "<%= ControladorContratacion.ACCION_ABRIR_PLAZA %>",
								"<%= ControladorContratacion.PARAM_PLAZA_OFERTADA %>": <%= bean.getPlazaOfertada().getCodNum() %>,
								"<%= ControladorContratacion.PARAM_FECHA_FIN_OFERTA %>": inputFechaFin.value
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
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
	
<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
	<%	if (plaza.getNri() != null) { %>
			document.getElementById("plaza_descargar_nri").addEventListener("click", function() {
				window.open("<%=ControladorDescargaFicheros.URL_DESCARGA_FICHEROS%>"
						+ "<%="?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_HORARIO_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_PLAZA_OFERTADA + "=" + plaza.getCodNum()%>");
			});
	<%	} %>
<%	} %>
	
});

</script>