<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidarNoAfines"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidarNoAfines bean = (VistaValidarNoAfines)uvdatos.getVistas().get(VistaValidarNoAfines.class.getName());
Bolsa bolsa = bean.getBolsa();
MeritoSolicitud merito = bean.getMerito();
UsuarioBolsaEmpleo candidato = bean.getCandidato();
ItemBaremacion item = null;
Double valor = null;
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria() != null) {
			descripcion = bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
<%	if (merito == null) { %>
	
		<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
<%	} %>
	
	<h2>Validar meritos no sujetos afinidad</h2>
	<h3><%= EscapaHTML.escapa(descripcion) %></h3>
	<h4><%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h4>
	
	<table class="bluetable bolsaempleo" id="tableCandidatosVNA">
		<tr>
			<th scope="col" style="width:76px">D.N.I</th>
			<th scope="col" style="width:100%" class="nombre">Nombre</th>
			<th scope="col" style="width:76px" class="center">No validados</th>
			<th scope="col" style="width:76px" class="center">Validados</th>
			<th scope="col" style="width:76px" class="center">Excluidos</th>
			<th scope="col" style="width:76px" class="center">Total</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
<%	if (candidato != null) { %>
	
		<table class="bluetable bolsaempleo" id="tableMeritosVNA">
			<tr>
				<th scope="col" style="width:40px">Id</th>
				<th scope="col" style="width:40px">Estado</th>
				<th scope="col" style="width:20%">Código</th>
				<th scope="col" style="width:50%">Valor</th>
				<th scope="col" style="width:40px">Fichero</th>
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="5" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
		
	<%	if (merito != null) {
			item = merito.getItem() != null ? merito.getItem() : merito.getMerito().getItemBaremacion();
			valor = merito.getValor() != null && merito.getValor() != 0 ? merito.getValor() : merito.getMerito().getValor();
	%>
			<div id="anchor_modificar_merito" style="margin-bottom: 24px;"></div>
			
			<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
			
			<table class="bluetable bolsaempleo" id="tableValoresMeritoBolsasVNA">
				<tr>
					<th scope="col" style="width:77px">Convocatoria</th>
					<th scope="col" style="width:100%; min-width: 100px">Bolsa</th>
					<th scope="col" style="width:40px">Código</th>
					<th scope="col" style="width:40px">Valor</th>
					<th scope="col" style="width:40px">Excluido</th>
					<th scope="col" style="width:84px">Último evaluador</th>
					<th scope="col" style="width:70px">Discordancia</th>
				</tr>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="7" style="width:100%"></th>
					</tr>
				</tfoot>
			</table>
			
			<table class="bluetable bolsaempleo" id="tableBolsasCandidatoVNA">
				<tr>
					<th scope="col" style="width:45%">Bolsa</th>
					<th scope="col" style="width:40%">Estado</th>
					<th scope="col" style="width:15%">Propagar</th>
				</tr>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="3" style="width:100%"></th>
					</tr>
				</tfoot>
			</table>
			
			<h3>Evaluar mérito: <%= EscapaHTML.escapa(merito.getMerito().getCodNum() + " - " + merito.getItem().getFullCode() + " " 
				+ merito.getItem().getNombre()) %><br/>
				<%= EscapaHTML.escapa("Bolsa: " + bolsa.getArea().getDescripcion()) %><br/>
				<%= "Candidato: " %> 
				<a class="bolsaempleo-link"
					href="<%= ControladorUsuarioCandidato.URL_PATTERN 
					+ "?" + ControladorUsuarioCandidato.PARAM_ACCION + "=" + ControladorUsuarioCandidato.ACCION_SELECCIONAR_CANDIDATO 
					+ "&" + ControladorUsuarioCandidato.PARAM_CANDIDATO + "=" + candidato.getCodNum()%>">
					<%= EscapaHTML.escapa(candidato.getPrsNif() + " " + candidato.getNombre() + " " + candidato.getPrimerApellido() + " " + candidato.getSegundoApellido()) %></a>
			<br/>
			</h3>
			
			<form id="validar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>">
				<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_ACCION %>" id="accion_formulario" value="<%= ControladorValidarNoAfines.ACCION_VALIDAR_MERITO %>" />
				<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_BOLSA %>" value="<%= bolsa.getCodNum() %>" />
				<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>" value="<%= candidato.getCodNum() %>" />
				<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_MERITO %>" value="<%= merito.getMerito().getCodNum() %>" />
				<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_BOLSAS %>" value="[<%= bolsa.getCodNum() %>]" />
				
				<div class="form-group-container col2">
					<div class="form-group">
						<label class="bold-label" for="select_item">Categoría:</label>
						<select class="form-input-custom" id="select_item" name="<%= ControladorValidarNoAfines.PARAM_ITEM %>">
							<%
							for(ItemBaremacion it: bean.getItems()) {
							%>
							<%	if (it.getCodNum() == item.getCodNum()) { %>
									<option value="<%=it.getCodNum()%>" 
											data-unidades="<%= it.getUnidades() %>" 
											data-descripcion="<%= it.getDescripcion() %>" 
											selected><%=EscapaHTML.escapa(it.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + it.getBloqueBaremacion().getCodigo() + "." + it.getCodigo() + "-" + it.getNombre())%></option>
							<%	} else { %>
									<option value="<%=it.getCodNum()%>" 
											data-unidades="<%= it.getUnidades() %>" 
											data-descripcion="<%= it.getDescripcion() %>"><%=EscapaHTML.escapa(it.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + it.getBloqueBaremacion().getCodigo() + "." + it.getCodigo() + "-" + it.getNombre())%></option>
							<%	} %>
							<%
							}
							%>
						</select>
					</div>
					<div class="form-group">
						<div class="form-group">
							<label for="merito_valor" id="merito_valor_label" class="bold-label">Valor (<%= EscapaHTML.escapa(item.getUnidades()) %>):</label>
							<input class="form-input-custom" id="merito_valor" type="text" name="<%= ControladorValidarNoAfines.PARAM_VALOR %>" 
									value="<%= valor %>"
									data-unidades="<%= item.getUnidades() %>"
									<%= EscapaHTML.escapa(item.getUnidades()).equals(ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_SINO) ? "readonly" : "" %>
									required
									style="max-width: 180px;"/>
						</div>
					</div>
				</div>
				
				<div class="form-group-container col1">
					<div class="form-file">
						<label for="merito_archivo" class="bold-label">Fichero:</label>
						<button id="merito_descargar_fichero" class="btn icon icon-download" title="Descargar fichero del mérito" type="button">Descargar fichero del mérito</button>
					</div>
				</div>
				
				<div class="form-group-container col1">
					<div class="form-group">
						<label for="merito_descripcion" class="bold-label">Descripción:</label>
						<textarea class="form-input-custom" id="merito_descripcion" name="" disabled><%= EscapaHTML.escapa(merito.getMerito().getDescripcion()) %></textarea>
					</div>
				</div>
			
				<div class="form-group-container col1">
					<div class="form-group">
						<label for="merito_observacion_comision">Observación para la comisión:</label>
						<textarea class="form-input-custom" id="merito_observacion_comision" name="" rows="2" cols="50" disabled><%=EscapaHTML.escapa(merito.getMerito().getObservacion())%></textarea>
					</div>
				</div>
				
				<div class="form-group-container col1">
					<div class="form-group">
						<label for="merito_observacion_candidato">Observación para el candidato:</label>
						<textarea class="form-input-custom" id="merito_observacion_candidato" name="<%= ControladorValidarNoAfines.PARAM_OBSERVACION_CANDIDATO %>" rows="2" cols="50"><%=EscapaHTML.escapa(merito.getObservacionCandidato())%></textarea>
					</div>
				</div>
				
				<div class="form-btn">
					<input id="merito_aceptar" type="submit" name="<%= ControladorValidarNoAfines.PARAM_ACEPTAR_MERITO %>" value="Aceptar"/>
					<input id="merito_excluir" type="submit" name="<%= ControladorValidarNoAfines.PARAM_EXCLUIR_MERITO %>" value="Excluir"/>
				</div>
			</form>
			
			<br/>
			<div class="btns-by-steps">
				<button class="link-btn" id="validar_volver">
					 Volver
				</button>
				<button class="link-btn" id="validar_historial">
					 Ver historial de validación
				</button>
			</div>
			
	<%	} else { %>
			<p>Elija un mérito para cargar el formulario de edición</p>
	<%	} %>
<%	} %>
	
</div>

<script>
$(document).ready(function() {
	Atis.handleLinkEvents();
	
	var tableCandidatos = new Atis.DataTable('#tableCandidatosVNA', {
		"ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
		"pageSize": 20,
		"defaultOrderBy": 2,
		"defaultOrderDirection": 'desc',
		"stateSave": true,
		"filterable": true,
		"action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_CANDIDATOS %>",
		"title": 'LISTA DE USUARIOS',
		"dropdown": true,
		"params": {'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>'},
		"clickable": {'onClick': function(row) {
			var params = {
					'a': '<%= ControladorValidarNoAfines.ACCION_CANDIDATO_SELECCIONADO %>',
					'<%= ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
					'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': row.codNum
					};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}},
		<% if (candidato != null) { %> "selected": <%= candidato.getCodNum() %> ,<% } %>
		"columns": [
			{'data': 'prsnif', 'filter': true},
			{'data': 'apellido1', 'overflow': 'auto', 'filter': true, 'render': function(row) {
				return row.nombre + " " + row.apellido1 + " " + row.apellido2; 
			}},
			{'data': 'totalMeritosNoValidados', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosNoValidados) ? 0 : row.totalMeritosNoValidados; } },
			{'data': 'totalMeritosValidados', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosValidados) ? 0 : row.totalMeritosValidados; } },
			{'data': 'totalMeritosExcluidos', 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosExcluidos) ? 0 : row.totalMeritosExcluidos; } },
			{'data': 'totalMeritos', 'class': 'center'}
		]
	});
	
	<% if (candidato != null) { %>
	
		var tableMeritos = new Atis.DataTable('#tableMeritosVNA', {
			"ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
			"pageSize": 10,
			"filterable": true,
			"stateSave": true,
			"action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_MERITOS %>",
			"title": 'MÉRITOS PARA EL USUARIO: <%=EscapaHTML.escapa(candidato.getIdNif() + " - " + candidato.getNombre() + " " + candidato.getPrimerApellido() + " " + candidato.getSegundoApellido())%>',
			"dropdown": true,
			"params": {
				'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
				'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>'
				},
			"clickable": {'onClick': function(row) {
				var params = {
						'a': '<%= ControladorValidarNoAfines.ACCION_MERITO_SELECCIONADO %>',
						'<%= ControladorValidarNoAfines.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
						'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
						'<%= ControladorValidarNoAfines.PARAM_MERITO %>': row.codNum};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			}},
			<% if (merito != null) { %> "selected": <%= merito.getMerito().getCodNum() %> ,<% } %>
			"columns": [
				{'data': 'codNum', 'filter': {'type': 'number'}},
				{'data': 'codNum', 'order': false, 'class': 'center', 'render': function(row) {
						if (!row.excluido && !row.validado) {
							return "<div title='Mérito no evaluado' class='circle-neutral'></div>";
						} else {
							if (row.excluido) {
								return "<div title='Mérito excluido' class='circle-false'></div>";
							} else {
								return "<div title='Mérito validado' class='circle-true'></div>";
							}
						}
					}
				},
				{'data': 'item', 'filter': true, 'render': function(row) {
					return row.item.bloque.apartado.codigo + "." + row.item.bloque.codigo + "." + row.item.codigo;
				}},
				{'data': 'valor', 'filter': true, 'overflow': 'auto', 'render': function(row) {
					return row.valor + " (" + row.item.unidades + ")";
				}},
				{'data': 'codnum', 'buttons': [
					{'title': 'Descargar fichero del mérito', 'class': 'only-icon icon-download', 'onClick': function(row) {
						window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
								+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL %>&<%= ControladorDescargaFicheros.PARAM_MERITO %>=" + row.codNum);
					}},
				]}
			]
		});
		
		Atis.smoothScrollToAnchor("#tableCandidatosVNA");
		
		<% if (merito != null) { %>
			
			var tableBolsasCandidato = new Atis.DataTable('#tableBolsasCandidatoVNA', {
				"ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
				"pageSize": 10,
				"selectable": {'all': false},
				"action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS_CANDIDATO %>",
				"title": 'BOLSAS EN LAS QUE ESTÁ APUNTADO EL MÉRITO EN ESTA SOLICITUD',
				"dropdown": true,
				"stateSave": true,
				"params": {
					'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
					'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
					'<%= ControladorValidarNoAfines.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>'
					},
				<% if (bolsa != null) { %> "selected": <%= bolsa.getCodNum() %> ,<% } %>
				"columns": [
					{'data': 'codNum', 'render': function(row) {
						var href = '<%= request.getRequestURL() + "?" + ControladorValidarNoAfines.PARAM_ACCION + "=" + ControladorValidarNoAfines.ACCION_MERITO_SELECCIONADO 
							+ "&" + ControladorValidarNoAfines.PARAM_BOLSA + "=" %>' + row.codNum + '<%=
								  "&" + ControladorValidarNoAfines.PARAM_CANDIDATO + "=" + candidato.getCodNum()
								+ "&" + ControladorValidarNoAfines.PARAM_MERITO + "=" + merito.getMerito().getCodNum()%>';
						return "<a class='bolsaempleo-link' href='" + href + "'>" + row.area.idAreaExterno + " : " + row.area.descripcion + "</a>";
					}},
					{'data': 'codNum', 'order': false, 'render': function(row) {
						if (row.codNum == <%= bolsa.getCodNum() %>) {
							return "<div class='text-primary'>Baremación actual</div>";
						} else {
							return "<div class='text-success'>Inscrito actualmente</div>";
						}
					}},
					{'data': 'codNum', 'order': false, 'selectable': {
							'disabled': function(row) {
								return row.codNum == '<%= bolsa.getCodNum() %>' ? true : false;
							},
							'selected': function(row) {
								return row.codNum == '<%= bolsa.getCodNum() %>' || row.estado == '<%= ModeloBolsa.BOLSA_ESTADO_BAREMACION %>' ? true : false;
							}
						},
					}
				]
			});
			
			var tableValoresMeritoBolsas = new Atis.DataTable('#tableValoresMeritoBolsasVNA', {
				"ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
				"pageSize": 10,
				"action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_VALORES_MERITO_BOLSA %>",
				"title": 'HISTORIAL DE VALORACIONES DEL MÉRITO: <%= merito.getMerito().getCodNum() %>',
				"dropdown": true,
				"stateSave": true,
				"params": {
					'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
					'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
					'<%= ControladorValidarNoAfines.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>'
					},
				"columns": [
					{'data': 'convocatoria.fechaCierre', 'render': function(row) {
						//return '<div title="' + row.convocatoria.codNum + ' - ' + row.convocatoria.descripcion + '">' + row.convocatoria.fechaCierre + '</div>';
						return '<div title="' + row.convocatoria.codNum + ' - ' + row.convocatoria.descripcion + '">' + row.convocatoria.curso + '</div>';
					}},
					{'data': 'bolsa.codNum', 'render': function(row) {
						var name = row.bolsa.area.idAreaExterno + " : " + row.bolsa.area.descripcion;
						
						if (row.convocatoria.estado === '<%= ModeloConvocatoria.CONVOCATORIA_ESTADO_FINALIZADA %>') {
							return name;
						} else {
							var href = '<%= request.getRequestURL() + "?" + ControladorValidarNoAfines.PARAM_ACCION + "=" + ControladorValidarNoAfines.ACCION_MERITO_SELECCIONADO 
								+ "&" + ControladorValidarNoAfines.PARAM_BOLSA + "=" %>' + row.bolsa.codNum + '<%=
									  "&" + ControladorValidarNoAfines.PARAM_CANDIDATO + "=" + candidato.getCodNum()
									+ "&" + ControladorValidarNoAfines.PARAM_MERITO + "=" + merito.getMerito().getCodNum()%>';
							return "<a class='bolsaempleo-link' href='" + href + "'>" + name + "</a>";
						}
					}},
					{'data': 'item', 'render': function(row) {
						return row.meritoSolicitud.item != null ? row.meritoSolicitud.item.bloque.apartado.codigo + "." + row.meritoSolicitud.item.bloque.codigo 
								+ "." + row.meritoSolicitud.item.codigo : '';
					}},
					{'data': 'meritoSolicitud.valor', 'render': function(row) {
						return row.meritoSolicitud.valor != 0 ? row.meritoSolicitud.valor : '';
					}},
					{'data': 'meritoSolicitud.excluido', 'order': false, 'render': function(row) {
						return row.meritoSolicitud && row.meritoSolicitud.excluido ? 'SI' : 'NO';
					}},
					{'data': 'uidUsuario', 'order': false, 'render': function(row) {
						return row.uidUsuario != '<%= EscapaHTML.escapa(candidato.getCodCuenta()) %>' ? row.uidUsuario : '';
					}},
					{'data': 'codNum', 'order': false, 'render': function(row) {
						var valorDistinto = (row.meritoSolicitud.valor != 0 && row.meritoSolicitud.valor != '<%= valor %>');
						var excluidoDistinto = (row.meritoSolicitud.excluido != <%= merito.isExcluido() %>);
						var itemDistinto = (row.meritoSolicitud.item != null && row.meritoSolicitud.item.codNum != <%= item.getCodNum() %>);
						var discordancia = valorDistinto || excluidoDistinto || itemDistinto;
						var titleDiscordancia = valorDistinto ? "El valor no se corresponde con el valor del mérito en la bolsa actual" : excluidoDistinto 
								? "Excluido no se corresponde con el excluido del mérito en la bolsa actual" : itemDistinto ? "Ítem no se corresponde con el ítem del mérito" : "";
						return  discordancia ? "<div title='" + titleDiscordancia + "' class='circle-false'></div>" : "";
					}}
				]
			});
			
			var itemChanged = false;
			
			$("#validar_merito").on("submit", function() {
				var self = this;
				
				this.elements['<%= ControladorValidarNoAfines.PARAM_BOLSAS %>'].value = Atis.object2Json(tableBolsasCandidato.getCheckedItems());
				
				if (itemChanged) {
					var titulo = "¿Modificar categoría del mérito?";
					var mensaje = "Modificar la categoría borrará las evaluaciones del mérito en todas las bolsas en estado de 'Validación'.";
					
					Atis.confirmDialog(titulo, mensaje, {
						Si: function() {
							self.submit();
						  	$(this).dialog("close");
						},
						No: function() {
						  	$(this).dialog("close");
						}
					});
					return false;
				}
			});
			
			document.getElementById("merito_descargar_fichero").addEventListener("click", function() {
				window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
						+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_MERITO + "=" + merito.getMerito().getCodNum() %>");
			});
			
			var inputValor = document.getElementById("merito_valor");
			var labelValor = document.getElementById("merito_valor_label");
			
			document.getElementById("select_item").addEventListener("change", function() {
				itemChanged = this.value != <%= item.getCodNum() %>;
				
				var unidades = this.options[this.selectedIndex].getAttribute("data-unidades");
				if (unidades != inputValor.getAttribute("data-unidades")) {
					labelValor.innerHTML = "Valor (" + unidades + "):";
					inputValor.setAttribute("data-unidades", unidades);
					if (unidades == "<%= EscapaHTML.escapa(ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_SINO) %>") {
						inputValor.value = 1;
						inputValor.readOnly = true;
					} else {
						inputValor.value = <%= valor %>;
						inputValor.readOnly = false;
					}
				}
			});
			
			document.getElementById("validar_volver").addEventListener("click", function() {
				Atis.sendForm("<%= request.getRequestURI() %>", {'<%= ControladorValidarNoAfines.PARAM_ACCION %>': '<%= ControladorValidarNoAfines.ACCION_INDEX %>'});
			});
			
			document.getElementById("validar_historial").addEventListener("click", function() {
				var params = {
						'<%= ControladorValidarNoAfines.PARAM_ACCION %>': '<%= ControladorValidarNoAfines.ACCION_HISTORIAL_VALIDACION %>',
						'<%= ControladorValidarNoAfines.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
						'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
						'<%= ControladorValidarNoAfines.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>',
				}
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			});
			
			Atis.smoothScrollToAnchor("#anchor_modificar_merito");
		<% } %>
		
	<% } %>
	
});
</script>
