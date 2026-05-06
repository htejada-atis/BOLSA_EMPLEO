<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorBolsas"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEstadoBolsas"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaEstadoBolsas bean = (VistaEstadoBolsas) uvdatos.getVistas().get(VistaEstadoBolsas.class.getName());
%>

<div class='bolsa-empleo'>
	<%
		Integer bloqueadas = null;
		Integer revisadas = null;
		Integer baremables = null;
		Integer totales = null;

		bloqueadas = bean.getTotalBolsasBloqueadas();
		revisadas = bean.getTotalBolsasRevisadas();
		baremables = bean.getTotalBolsasBaremables();
		totales = bean.getTotalBolsas();

		String descripcion = "";
		if(bean.getConvocatoria() != null) {
			descripcion = bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Estado de las bolsas</h2>
	<h3><%= descripcion %><button class="link-btn" id="exportar" style="float: right;">Exportar</button></h3>

	<div class="titulo-bolsa-empleo">
		<div class="form-group-container col3">
			<div class="form-group" style="margin-bottom: 0"><p>Total bloqueadas: <%= bloqueadas %> / <%= totales %></p></div>
			<div class="form-group" style="margin-bottom: 0"><p>Total revisadas: <%= revisadas %> / <%= totales %></p></div>
			<div class="form-group" style="margin-bottom: 0"><p>Total baremables: <%= baremables %> / <%= totales %></p></div>
		</div>
	</div>

	<table class="bluetable bolsaempleo tablebolsas" id="tableBolsasBOL">
		<tr>
			<th scope="col" style="width:15px"></th>
			<th scope="col" style="width:30px" title="Id de la convocatoria">Id</th>
			<th scope="col" style="width:100%" class="area">Area</th>
			<th scope="col" style="width:80px">Estado</th>
			<th scope="col" style="width:140px">Fechas</th>
			<th scope="col" style="width:80px">Baremación</th>
			<th scope="col" class="center" style="width:48px">Baremable</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="8" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>
$(document).ready(function() {
	function formatDateWithoutTime(value) {
		if (!value) {
			return '';
		}
		var trimmed = value.toString().trim();
		var parts = trimmed.split(/\s+/);
		return parts.length > 1 ? parts[0] : trimmed;
	}

	var table = new Atis.DataTable('#tableBolsasBOL', {
		"ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/bolsas" },
		"selectable": true,
		"pageSize": 10,
		"pageSizeOptions": [10, 100, 200],
		"filterable": true,
		"action": "<%= ControladorBolsas.ACCION_DATATABLE %>",
		"defaultOrderBy": 2,
		"defaultOrderDirection": 'asc',
		"stateSave": true,
		"columns": [
			{'data': 'codNum', 'selectable': true},
			{'data': 'codNum', 'filter': {'type': 'number'}},
			{'data': 'area.descripcion', 'filter': true},
			{'data': 'estado',
				'render': function(row) {
					switch(row.estado) {
					case '<%= ModeloBolsa.BOLSA_ESTADO_BAREMACION %>':
						return 'VALIDACION';
					case '<%= ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA %>':
						return 'DESBLOQUE.';
					default:
						return row.estado;
					}
				},
				'filter': {'type': 'select', 'options': {
					'<%= ModeloBolsa.BOLSA_ESTADO_BLOQUEADA %>': 'Bloqueada',
					'<%= ModeloBolsa.BOLSA_ESTADO_REVISION %>': 'Revisión',
					'<%= ModeloBolsa.BOLSA_ESTADO_BAREMACION %>': 'Validación',
					'<%= ModeloBolsa.BOLSA_ESTADO_ALEGACIONES %>':'Alegaciones',
					'<%= ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA %>':'Desbloqueada'
					}
				}
			},
			{'data': 'fechaBloqueo', 'filter': false,
				'render': function(row) {
					var html = '';
					if (row.estado === '<%= ModeloBolsa.BOLSA_ESTADO_BLOQUEADA %>' && row.fechaBloqueo) {
						html += '<div style="font-size: 10px;"><strong>Bloqueo:</strong> ' + formatDateWithoutTime(row.fechaBloqueo) + '</div>';
					}
					if (row.estado === '<%= ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA %>' && row.fechaDesBloqueo) {
						html += '<div style="font-size: 10px;"><strong>Desbloqueo:</strong> ' + formatDateWithoutTime(row.fechaDesBloqueo) + '</div>';
					}
					if (row.estado === '<%= ModeloBolsa.BOLSA_ESTADO_ALEGACIONES %>' && row.fechaFinAlegaciones) {
						html += '<div style="font-size: 10px;"><strong>Fin aleg.:</strong> ' + row.fechaFinAlegaciones + '</div>';
					}
					if (row.fechaHabilitarContratos) {
						html += '<div style="font-size: 10px;"><strong>Contratación:</strong> ' + formatDateWithoutTime(row.fechaHabilitarContratos) + '</div>';
					}
					return html;
				}
			},
			{'data': 'fechaBaremacion', 'filter': false, 'render': function(row) {
				var fb = row.fechaBaremacion ? '<div style="font-size: 10px;"><strong>P:</strong> ' + formatDateWithoutTime(row.fechaBaremacion) + '</div>' : '';
				fb += row.fechaBaremacionDefinitiva ? '<div style="font-size: 10px;"><strong>D:</strong> ' + formatDateWithoutTime(row.fechaBaremacionDefinitiva) + '</div>' : '';
				return fb;
			}},
			{'data': 'baremable', 'filter': {'type': 'select', 'options': {'true': 'Baremable', 'false': 'No Baremable'} , 'optionDefault': 'true'}, 'render': function(row) {
				if(row.baremable) {
					return "<div title='Baremable' class='circle-true'></div>";
				} else {
					return "<div title='No Baremable' class='circle-false'></div>";
				}
			}},
		],
		"actions": [
			{'label': 'Bloquear', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_BLOQUEAR%>", selected); } },
			{'label': 'Revisión', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_REVISION%>", selected); } },
			{'label': 'Validación', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_BAREMACION%>", selected); } },
			{'label': 'Alegación', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_ALEGACION%>", selected); } },
			{'label': 'Desbloquear', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_DESBLOQUEAR%>", selected); } },
			{'label': 'Baremar', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_BAREMAR%>", selected); } },
			{'label': 'Contratación', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_CONTRATACION%>", selected); } },
		]
	});

	function bloquearAcciones() {
		$('#tableBolsasBOL').find('.actions').find('.btn').prop('disabled', true);
		return true;
	}

	function desbloquearAcciones() {
		$('#tableBolsasBOL').find('.actions').find('.btn').prop('disabled', false);
		return true;
	}

	function enviaAccion(accion, selected) {
		bloquearAcciones();

		if (selected.length == 0) {
			Atis.alertDialog('Estado de las bolsas', 'Seleccione al menos una bolsa para cambiar su estado.', desbloquearAcciones, desbloquearAcciones);
			return;
		}

		if (accion == 'alegacion') {
			if (selected.length > 1) {
				Atis.alertDialog('Alegaciones', 'Sólo puede seleccionar 1 bolsa al mismo tiempo para fijar la fecha de alegaciones.', desbloquearAcciones, desbloquearAcciones);
				return;
			}
			var $dialog = $('<div title="Alegaciones"><p>Seleccione la fecha y hora límite para presentar alegaciones:</p>'
				+ '<p><label for="fechaFinAlegaciones">Fecha fin alegaciones: </label>'
				+ '<input type="text" id="fechaFinAlegaciones" name="fechaFinAlegaciones" style="width:140px" />'
				+ '<input type="text" id="horaFinAlegaciones" name="horaFinAlegaciones" style="width:60px;margin-left:10px" placeholder="HH:mm" maxlength="5" />'
				+ '</p></div>');

			$dialog.dialog({
				modal: true,
				width: 450,
				close: function() { desbloquearAcciones(); $(this).dialog("destroy").remove(); },
				open: function() {
					$('#fechaFinAlegaciones').datepicker({ dateFormat: 'dd/mm/yy', minDate: 0 });
					$('#horaFinAlegaciones').val('23:59').focus();
				},
				buttons: {
					"Aceptar": function() {
						var fecha = $('#fechaFinAlegaciones').val();
						var hora = $('#horaFinAlegaciones').val();
						if (!fecha || !hora) {
							Atis.alertDialog('Alegaciones', 'Debe indicar fecha y hora límite.', null, null);
							return;
						}
						if (!/^\d{2}:\d{2}$/.test(hora)) {
							Atis.alertDialog('Alegaciones', 'El formato de hora debe ser HH:mm', null, null);
							return;
						}
						var paramsAlegacion = {
							'<%= ControladorBolsas.PARAM_ACCION %>': '<%= ControladorBolsas.ACCION_BOLSA %>',
							'<%= ControladorBolsas.PARAM_ACCION_BOLSA %>': '<%= ControladorBolsas.ACCION_BOLSAS_ALEGACION %>',
							'<%= ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS %>': Atis.object2Json(selected),
						'<%= ControladorBolsas.PARAM_FECHA_FIN_ALEGACIONES %>': fecha + ' ' + hora
						};
						Atis.sendForm("<%= request.getRequestURI() %>", paramsAlegacion);
						desbloquearAcciones();
						$(this).dialog("close");
					},
					"Cancelar": function() {
						desbloquearAcciones();
						$(this).dialog("close");
					}
				}
			});

			return;
		}

		if (accion == 'baremar') {
			if (selected.length > 1) {
				Atis.alertDialog('Baremar bolsa', 'Sólo puede baremar 1 bolsas al mismo tiempo como máximo.', desbloquearAcciones, desbloquearAcciones);
				return;
			}

			var paramsBaremar = {
				'<%= ControladorBolsas.PARAM_ACCION %>': '<%= ControladorBolsas.ACCION_BOLSA%>',
				'<%= ControladorBolsas.PARAM_ACCION_BOLSA %>': '<%= ControladorBolsas.ACCION_BOLSAS_BAREMAR %>',
				'<%= ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS %>': Atis.object2Json(selected)
			}

			Atis.confirmDialog("Baremar bolsa", "Seleccione baremación provisional o baremación definitiva.<br/><br/>Si se barema de forma provisional, la fecha de baremación definitiva se pondrá en blanco.", {
				Provisional: function(row) {
					paramsBaremar['<%= ControladorBolsas.PARAM_ACCION_TIPO_BAREMACION %>'] = '<%= ControladorBolsas.ACCION_BOLSAS_BAREMAR_PROVISIONAL %>';
					Atis.sendForm("<%= request.getRequestURI() %>", paramsBaremar);
					desbloquearAcciones();
					$(this).dialog("close");
				},
				Definitiva: function() {
					paramsBaremar['<%= ControladorBolsas.PARAM_ACCION_TIPO_BAREMACION %>'] = '<%= ControladorBolsas.ACCION_BOLSAS_BAREMAR_DEFINITIVA %>';
					Atis.sendForm("<%= request.getRequestURI() %>", paramsBaremar);
					desbloquearAcciones();
					$(this).dialog("close");
				},
				CANCELAR: function() {
					desbloquearAcciones();
					$(this).dialog("close");
				}
			}, desbloquearAcciones);

			return;
		}

		var params = {
			'<%= ControladorBolsas.PARAM_ACCION %>': '<%=ControladorBolsas.ACCION_BOLSA%>',
			'<%=ControladorBolsas.PARAM_ACCION_BOLSA%>': accion,
			'<%=ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS%>': Atis.object2Json(selected)
		};
		Atis.sendForm("<%= request.getRequestURI() %>", params);
	}

	document.getElementById('exportar').addEventListener('click', function() {
		window.open('<%= request.getRequestURI() %>?<%= ControladorBolsas.PARAM_ACCION %>=<%= ControladorBolsas.ACCION_EXPORTAR %>');
	});

});
</script>
