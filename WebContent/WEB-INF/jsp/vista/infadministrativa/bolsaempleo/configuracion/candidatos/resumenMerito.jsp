<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaCandidatos bean = (VistaCandidatos) uvdatos.getVistas().get(VistaCandidatos.class.getName());
%>

<div class="bolsa-empleo">
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Resumen solicitud - <%= bean.getCandidato().getCodCuenta() %></h2>
	
	<h4>Convocatoria: <%= bean.getSolicitud().getConvocatoria().getDescripcion() %>&nbsp;(<%= bean.getSolicitud().getConvocatoria().getEstado() %>)</h4>
	<h4>Nombre: <%= bean.getCandidato().getNombre() + " " + bean.getCandidato().getPrimerApellido() + " " + bean.getCandidato().getSegundoApellido() %></h4>
	<h4>Nº de documento: <%= bean.getCandidato().getPrsNif() %></h4>
	<h4>Estado solicitud: <%= bean.getSolicitud().getEstado() %></h4>
	
	<% if (bean.getSolicitud().getExcluido()) { %>
		<h4>EXCLUIDA: <%= Formateador.formatoFecha(bean.getSolicitud().getFechaExclusion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) %> - <%= bean.getSolicitud().getRazonExclusion() %></h4>
	<% } %>
	
	<div class="row" style="margin-bottom: 20px">
		<button class="link-btn" id="solicitud_volver" style="float:left;max-height: 25px">Volver</button>
	</div>
	
	<h2>Mérito: <%= bean.getMeritoSolicitud().getMerito().getCodNum() %> - <%= bean.getMeritoSolicitud().getItem().getFullCode() %> - <%= bean.getMeritoSolicitud().getItem().getNombre() %></h2>
	<h4>Valor: <%= bean.getMeritoSolicitud().getValor() != 0 ? bean.getMeritoSolicitud().getValor() : bean.getMeritoSolicitud().getMerito().getValor()  %></h4>
	<h4>Descripción: <%= bean.getMeritoSolicitud().getMerito().getDescripcion() %></h4>
	
	<%	if (bean.getMeritoSolicitud().getMerito().getItemBaremacion().getAfinidad() != null) { %>
		<%	if (bean.getMeritoSolicitud().getValoraciones().size() > 0) { %>
			<table class="bluetable bolsaempleo">
				<caption>Afinidad</caption>
				<tr>
					<th scope="col">Tipo afinidad</th>
					<th scope="col">Valor</th>
				</tr>
				<%	if (bean.getMeritoSolicitud().getMerito().getItemBaremacion().getIndividualizado()) { %>
					<tr>
						<td><%= bean.getMeritoSolicitud().getValoraciones().get(0).getAfinidad().getCodigo() %></td>
						<td>
							<select id="afinidad_individualizado" data-valoracion="<%= bean.getMeritoSolicitud().getValoraciones().get(0).getCodNum() %>">
							<%
								for (Afinidad afinidad : bean.getListaAfinidades()) { 
									ItemBaremacion itemMerito = bean.getMeritoSolicitud().getItem() != null ? bean.getMeritoSolicitud().getItem() : bean.getMeritoSolicitud().getMerito().getItemBaremacion();
							%>
								<%	if (afinidad.getCodigo().equals(itemMerito.getAfinidad())) { %>
									<% 	if (bean.getMeritoSolicitud().getValoraciones().get(0).getAfinidad().getCodNum() == afinidad.getCodNum()) { %>
										<option value="<%= afinidad.getCodNum() %>" selected><%= afinidad.getModulacion() * 100 + "%: " + afinidad.getDescripcion() %></option>
									<%	} else { %>
										<option value="<%= afinidad.getCodNum() %>"><%= afinidad.getModulacion()* 100 + "%: " + afinidad.getDescripcion() %></option>
									<%	} %>
								<%	} %>
							<%	} %>
							</select>
						</td>
					</tr>
				<%	} else {
						for (MeritoSolicitudValoracion valoracion: bean.getMeritoSolicitud().getValoraciones()) { %>
							<tr>
								<td><%= valoracion.getAfinidad().getCodigo() + " " + valoracion.getAfinidad().getModulacion() * 100 + "%" %></td>
								<td><input id="afinidadValor<%= valoracion.getAfinidad().getCodNum() %>" value="<%= valoracion.getValor() %>" /></td>
							</tr>
					<%	}
				} %>
			</table>
			<button style="float:right; margin-bottom: 20px;" id="saveAfinidades">Guardar afinidades</button>
		<%	} %>
	<%	} %>
	
	<table class="bluetable bolsaempleo" id="tableHistorialMerito">
		<tr>
			<th scope="col" style="" title="Código historial">Cod. Cambio</th>
			<th scope="col" style="" title="Tipo de log">Tipo</th>
			<th scope="col" style="" title="Fecha de log">Fecha</th>
			<th scope="col" style="" title="Usuario que hace el cambio">Usuario</th>
			<th scope="col" style="" title="Detalle">Detalle</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" id="tableHistorialSolicitudBolsaMerito">
		<tr>
			<th scope="col" style="" title="Código historial">Cod. Cambio</th>
			<th scope="col" style="" title="Tipo de log">Tipo</th>
			<th scope="col" style="" title="Fecha de log">Fecha</th>
			<th scope="col" style="" title="Usuario que hace el cambio">Usuario</th>
			<th scope="col" style="" title="Detalle">Detalle</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" id="tableHistorialSolicitudBolsaMeritoValoracion">
		<tr>
			<th scope="col" style="" title="Código historial">Cod. Cambio</th>
			<th scope="col" style="" title="Tipo de log">Tipo</th>
			<th scope="col" style="" title="Fecha de log">Fecha</th>
			<th scope="col" style="" title="Usuario que hace el cambio">Usuario</th>
			<th scope="col" style="" title="Detalle">Detalle</th>
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
		document.getElementById("solicitud_volver").addEventListener("click", function() {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_VOLVER_SOLICITUD%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>',
				'<%=ControladorUsuarioCandidato.PARAM_SOLICITUD%>': '<%=bean.getSolicitud().getCodNum()%>'
			});
		});
		
		var buttonAfinidades = document.getElementById("saveAfinidades");
		if (buttonAfinidades) {
			buttonAfinidades.addEventListener("click", function() {
				Atis.confirmDialog(
					"Confirmar actualizar afinidades", 
					"¿Continuar actualizando afinidades del mérito?" 
						<%= bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA) ? "+ '<br/><br/><strong>La solicitud está cerrada!!!</strong>'" : "" %>, 
					{
					Si: function() {
						var valoracion = '';
						var valoracionAfinidad = '';
						
						if (document.getElementById("afinidad_individualizado")) {
							// individualizado
							valoracion = $('#afinidad_individualizado').data('valoracion');
							valoracionAfinidad = $('#afinidad_individualizado').val();
						} else {
							// no individualizado
							valoracionAfinidad = {};
							
							<% for (MeritoSolicitudValoracion valoracion: bean.getMeritoSolicitud().getValoraciones()) { %>
								valoracionAfinidad['<%= valoracion.getAfinidad().getCodNum() %>'] = Atis.redondearFloat($('#afinidadValor<%= valoracion.getAfinidad().getCodNum() %>').val());
							<% } %>
							
							valoracionAfinidad = Atis.object2Json(valoracionAfinidad);
							
							console.log("update afinidades", valoracion, valoracionAfinidad);
						}
						
						Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_EDITAR_AFINIDADES_MERITO%>',
							'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>',
							'<%=ControladorUsuarioCandidato.PARAM_SOLICITUD%>': '<%=bean.getSolicitud().getCodNum()%>',
							'<%=ControladorUsuarioCandidato.PARAM_MERITO_SOLICITUD%>': <%= bean.getMeritoSolicitud().getCodNum()%>,
							'<%=ControladorUsuarioCandidato.PARAM_MERITO_SOLICITUD_VALORACION %>': valoracion,
							'<%=ControladorUsuarioCandidato.PARAM_MERITO_SOLICITUD_VALORACION_AFINIDAD %>': valoracionAfinidad
						});
						$(this).dialog("close");
					},
					No: function() { 
						$(this).dialog("close"); 
					}
				});
			});
		}
		
		function renderUsuario(row) {
			return row.uidUsuario + (row.rolUsuario ? ' (' + row.rolUsuario + ')' : '');
		}
		
		var table_usuarios = new Atis.DataTable('#tableHistorialMerito', {
			"title": 'HISTORIAL DEL MERITO',
			"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
			"pageSize": 10,
			"filterable": false,
			"stateSave": false,
			"action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_HISTORIAL_MERITO %>",
			"params": {
				"<%= ControladorUsuarioCandidato.PARAM_MERITO %>": <%= bean.getMeritoSolicitud().getMerito().getCodNum() %>,
				"<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>": <%= bean.getCandidato().getCodNum() %>,
				"<%= ControladorUsuarioCandidato.PARAM_SOLICITUD %>": <%= bean.getSolicitud().getCodNum() %>
			},
			"columns": [
				{'data': 'codCambio', 'overflow': 'auto', 'order': false},
				{'data': 'log', 'overflow': 'auto', 'order': false},
				{'data': 'fechaLog', 'overflow': 'auto', 'order': false},
				{'data': 'uidUsuario', 'overflow': 'auto', 'order': false, 'render': renderUsuario},
				{'data': 'codCambio', 'overflow': 'auto', 'order': false, 'render': function(row) {
					return '<button class="detail" data-id="'+row.codCambio+'">Detalle</button>';
				}},
			],
		});
		
		var table_solicitud_bolsa_merito = new Atis.DataTable('#tableHistorialSolicitudBolsaMerito', {
			"title": 'HISTORIAL MERITO EN BOLSA',
			"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
			"pageSize": 10,
			"filterable": false,
			"stateSave": false,
			"action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_HISTORIAL_SOLICITUD_BOLSA_MERITO %>",
			"params": {
				"<%= ControladorUsuarioCandidato.PARAM_MERITO_SOLICITUD %>": <%= bean.getMeritoSolicitud().getCodNum() %>,
				"<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>": <%= bean.getCandidato().getCodNum() %>,
				"<%= ControladorUsuarioCandidato.PARAM_SOLICITUD %>": <%= bean.getSolicitud().getCodNum() %>
			},
			"columns": [
				{'data': 'codCambio', 'overflow': 'auto', 'order': false},
				{'data': 'log', 'overflow': 'auto', 'order': false},
				{'data': 'fechaLog', 'overflow': 'auto', 'order': false},
				{'data': 'uidUsuario', 'overflow': 'auto', 'order': false, 'render': renderUsuario},
				{'data': 'codCambio', 'overflow': 'auto', 'order': false, 'render': function(row) {
					return '<button class="detail" data-id="'+row.codCambio+'">Detalle</button>';
				}},
			],
		});
		
		var tableHistorialSolicitudBolsaMeritoValoracion = new Atis.DataTable('#tableHistorialSolicitudBolsaMeritoValoracion', {
			"title": 'HISTORIAL VALORACION MERITO EN BOLSA ',
			"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
			"pageSize": 10,
			"filterable": false,
			"stateSave": false,
			"action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_HISTORIAL_SOLICITUD_BOLSA_MERITO_VALORACION %>",
			"params": {
				"<%= ControladorUsuarioCandidato.PARAM_MERITO_SOLICITUD %>": <%= bean.getMeritoSolicitud().getCodNum() %>,
				"<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>": <%= bean.getCandidato().getCodNum() %>,
				"<%= ControladorUsuarioCandidato.PARAM_SOLICITUD %>": <%= bean.getSolicitud().getCodNum() %>
			},
			"columns": [
				{'data': 'codCambio', 'overflow': 'auto', 'order': false},
				{'data': 'log', 'overflow': 'auto', 'order': false},
				{'data': 'fechaLog', 'overflow': 'auto', 'order': false},
				{'data': 'uidUsuario', 'overflow': 'auto', 'order': false, 'render': renderUsuario},
				{'data': 'codCambio', 'overflow': 'auto', 'order': false, 'render': function(row) {
					return '<button class="detail" data-id="'+row.codCambio+'">Detalle</button>';
				}},
			],
		});
		
		function getRowHistorial(table, codCambio) {
			if (table.lastResponse && table.lastResponse.data) {
				for(var i=0; i<table.lastResponse.data.length; i++) {
					if (table.lastResponse.data[i].codCambio == codCambio) {
						return table.lastResponse.data[i];
					}
				}
			}
			return null;
		}
		
		function renderTable(body, title) {
			return  '<table class="bluetable" style="width: 70em;"><tr><th scope="col">Campo</th><th scope="col">Valor</th></tr>' +
					(title ? '<caption>' + title + '</caption>' : '') +
					'<tbody>' + 
						body +
					'</tbody>' +
					'</table>';
		}
		
		function renderTableHistorial(row) {
			return renderTable(
					renderItemLog('Código de cambio', row.codCambio) +
					renderItemLog('Tipo', row.log) +
					renderItemLog('Fecha', row.fechaLog) +
					renderItemLog('Usuario', renderUsuario(row)),
					'Log'
			);
		}
		
		function renderItemLog(title, value) {
			return '<tr><td>'+title+'</td><td>'+value+'</td>';
		}
		
		$('#tableHistorialMerito').on('click', '.detail', function() {
			var codCambio = $(this).data('id');
			var row = getRowHistorial(table_usuarios, codCambio);
			var html =
				renderTableHistorial(row) +
				renderTable(
					renderItemLog('Código mérito', row.codNum) +
					renderItemLog('Item mérito código', row.bepiteCodNum) +
					renderItemLog('Item mérito item', row.item.bloque.apartado.codigo + '.' + row.item.bloque.codigo + '.' + row.item.codigo) +
					renderItemLog('Item mérito afinidad', row.item.afinidad) +
					renderItemLog('Candidato código', row.bepusuCodNum) +
					renderItemLog('Candidato código', row.usuario.nombre + ' ' + row.usuario.apellido1 + ' ' + row.usuario.apellido2) +
					renderItemLog('Valor', row.valor) +
					renderItemLog('Descripción', row.descripcion ? row.descripcion : '') +
					renderItemLog('Observación', row.observacion ? row.observacion : ''),
					'Mérito'
				);
			console.log(row);
			Atis.alertDialog("Historial Mérito", html);
		});
		
		$('#tableHistorialSolicitudBolsaMerito').on('click', '.detail', function() {
			var codCambio = $(this).data('id');
			var row = getRowHistorial(table_solicitud_bolsa_merito, codCambio);
			var html =
				renderTableHistorial(row) +
				renderTable(
					renderItemLog('Código solicitud bolsa mérito', row.codNum) +
					renderItemLog('Código solicitud bolsa', row.bepsboCodNum) +
					renderItemLog('Excluido', row.flgExcluido) +
					renderItemLog('Validado', row.flgValidado) +
					renderItemLog('Observación candidato', row.observacionCandidato ? row.observacionCandidato : '') +
					renderItemLog('Valor', row.valor) +
					renderItemLog('Desglose', row.desglose ? row.desglose : '') +
					renderItemLog('Resultado', row.resultado) +
					renderItemLog('Código', row.codigo) +
					renderItemLog('Nombre', row.nombre),
					'Solicitud bolsa mérito'
				) +
				renderTable(
					renderItemLog('Código merito', row.bepmerCodNum) +
					renderItemLog('Item', row.merito.item.bloque.apartado.codigo + '.' + row.merito.item.bloque.codigo + '.' + row.merito.item.codigo) +
					renderItemLog('Descipción', row.merito.descripcion) + 
					renderItemLog('Observación', row.merito.observacion),
					'Mérito'
				) +
				renderTable(
					renderItemLog('Código item baremación', row.bepiteCodNum) +
					renderItemLog('Código item baremación', row.itemSolicitudBolsa.bloque.apartado.codigo + '.' 
							+ row.itemSolicitudBolsa.bloque.codigo + '.' 
							+ row.itemSolicitudBolsa.codigo),
					'Item'
				);
			console.log(row);
			Atis.alertDialog("Historial Solicitud Bolsa Mérito", html);
		});
		
		$('#tableHistorialSolicitudBolsaMeritoValoracion').on('click', '.detail', function() {
			var codCambio = $(this).data('id');
			var row = getRowHistorial(tableHistorialSolicitudBolsaMeritoValoracion, codCambio);
			var html = 
				renderTableHistorial(row) + 
				renderTable(
					renderItemLog('Código solicitud bolsa mérito valoración', row.codNum) +
					renderItemLog('Código solicitud bolsa mérito', row.bepsbmCodNum) +
					renderItemLog('Valor (no individualizado)', row.valor),
					'Valoración'
				) +
				renderTable(
					renderItemLog('Código afinidad', row.bepafiCodNum) +
					renderItemLog('Afinidad', row.afinidad.codigo) +
					renderItemLog('Afinidad modulación', (row.afinidad.modulacion * 100) + "%"),
					'Afinidad'
				);
			console.log(row);
			Atis.alertDialog("Historial Solicitud Bolsa Mérito Valoración", html);
		});
		
	});

</script>
