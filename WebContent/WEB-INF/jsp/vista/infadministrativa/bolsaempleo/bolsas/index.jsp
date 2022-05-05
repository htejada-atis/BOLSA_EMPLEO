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
	%>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Estado de las bolsas</h2>
	
	<div class="titulo-bolsa-empleo">
		<div class="form-group-container col3">
			<div class="form-group">
					<p>Total de bolsas bloqueadas:
						<%= bloqueadas %> / <%= totales %>
					</p>
			</div>
			<div class="form-group">
					<p>Total de bolsas revisadas:
						<%= revisadas %> / <%= totales %>
					</p>
			</div>
			<div class="form-group">
					<p>Total de bolsas baremables:
						<%= baremables %> / <%= totales %>
					</p>
			</div>
		</div>
		
	</div>
	
	
	<table class="bluetable bolsaempleo" id="tableBolsasBOL">
		<tr>
			<th scope="col" style="width:15px"></th>
			<th scope="col" style="width:30px" title="Id de la convocatoria">Id</th>
			<th scope="col" style="width:100%" class="area">Area</th>
			<th scope="col" style="width:90px">Estado</th>
			<th scope="col" style="width:68px">Actualizada</th>
			<th scope="col" style="width:68px">Bloqueo</th>
			<th scope="col" style="width:68px">Desbloqueo</th>
			<th scope="col" style="width:68px">Baremación</th>
			<th scope="col" class="center" style="width:48px">Baremable</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="9" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>
	
<script>
$(document).ready(function() {
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
			{'data': 'estado', 'render': function(row) {
					return row.estado == '<%= ModeloBolsa.BOLSA_ESTADO_BAREMACION %>' ? 'VALIDACIÓN' : row.estado;
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
			{'data': 'fechaActualizacion', 'filter': {'type': 'date'}},
			{'data': 'fechaBloqueo', 'filter': {'type': 'date'}},
			{'data': 'fechaDesBloqueo', 'filter': {'type': 'date'}},
			{'data': 'fechaBaremacion', 'filter': {'type': 'date'}},
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
		]
	});
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de las bolsas', 'Seleccione al menos una bolsa para cambiar su estado.');
			return;
		}
		
		if (accion == 'baremar' && selected.length > 5) {
			Atis.alertDialog('Baremar bolsa', 'Sólo puede baremar 5 bolsas al mismo tiempo como máximo.');
			return;
		}
		
		var params = {
			'a': '<%=ControladorBolsas.ACCION_BOLSA%>', 
			'<%=ControladorBolsas.PARAM_ACCION_BOLSA%>': accion, 
			'<%=ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS%>': Atis.object2Json(selected)
		};
		Atis.sendForm("<%= request.getRequestURI() %>", params);
	}
	
}); 
</script>
