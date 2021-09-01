<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorPlazasOfertadas" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlazasOfertadas" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="java.util.Map.Entry" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaPlazasOfertadas bean = (VistaPlazasOfertadas) uvdatos.getVistas().get(VistaPlazasOfertadas.class.getName());
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Plazas ofertadas</h2>
	</div>
	
	<table class="bluetable bolsaempleo" id="tablePlazasOfertadas">
		<tr>
			<th scope="col" style="width:30px">Id</th>
			<th scope="col" style="width:100%">Área</th>
			<th scope="col" style="width:95px">Estado</th>
			<th scope="col" style="width:100px">Fecha fin oferta</th>
			<th scope="col" style="width:100px" class="center">Confirmación oferta</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
<%	if (bean.getListaOfertasCandidatos().size() > 0) { %>
		<table class="bluetable bolsaempleo" id="tableOfertasPreferentes">
			<caption>Plazas aceptadas por preferencia</caption>
			<tr>
				<th scope="col" style="width:30px">Id</th>
				<th scope="col" style="width:100%">Área</th>
				<th scope="col" style="width:95px">Estado</th>
				<th scope="col" style="width:100px" class="center">Contrato</th>
				<th scope="col" style="width:100px" class="center">Confirmación de contrato</th>
				<th scope="col" style="width:100px">Preferencia</th>
			</tr>
			<tbody>
			<%	for (OfertaCandidato ofertaCandidato: bean.getListaOfertasCandidatos()) { %>
					<tr class="oferta_row clickable" data-id="<%= ofertaCandidato.getCodNum() %>">
						<td><%= ofertaCandidato.getPlaza().getCodNum() %></td>
						<td><%= ofertaCandidato.getPlaza().getArea().getIdAreaExterno() + " " + ofertaCandidato.getPlaza().getArea().getDescripcion() %></td>
						<td><%= ofertaCandidato.getPlaza().getEstado() %></td>
						<td class="center"><%= ofertaCandidato.getContratacion() != null ? "<div title='Aceptada' class='circle-true'></div>" : "<div title='Rechazada' class='circle-false'></div>" %></td>
						<td class="center"><%= ofertaCandidato.getContratacion() != null ? ofertaCandidato.getContratacion().getResultado().equals("ACEPTADA") ? "<div title='Aceptada' class='circle-true'></div>" : "<div title='Rechazada' class='circle-false'></div>" : "" %></td>
						<td>
							<input class="oferta_preferencia" type="number" value="<%= ofertaCandidato.getPreferencia() != 0 ? ofertaCandidato.getPreferencia() : "" %>" style="width: 40px"/>
						</td>
					</tr>
			<%	} %>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="6" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
		
		<button id="plaza_preferencia" style="float:right; margin-left: 12px;">Guardar preferencia</button>
<%	} %>
	
</div>
	
<script>
$(document).ready(function() {
	
	var estadosPlaza = {};
	<%	for (Entry<String, String> est: ModeloPlazaOfertada.ESTADOS.entrySet()) { %>
			estadosPlaza["<%= est.getKey() %>"] = "<%= est.getValue() %>";
	<%	} %>
	
	var table = new Atis.DataTable('#tablePlazasOfertadas', {
		"ajax": { url: "<%= ControladorPlazasOfertadas.URL_PATTERN_AJAX %>" },
		"pageSize": 10,
		"filterable": true,
		"title": 'Plazas ofertadas',
		"action": "<%= ControladorPlazasOfertadas.ACCION_DATATABLE_PLAZAS_OFERTADAS %>",
		"clickable": {'onClick': function(row) {
			var params = {
					'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_SELECCIONAR_PLAZA_OFERTADA %>',
					'<%= ControladorPlazasOfertadas.PARAM_PLAZA_OFERTADA %>': row.plaza.codNum};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}},
		"columns": [
			{'data': 'plaza.codNum', 'filter': {'type': 'number'}},
			{'data': 'plaza.area.idAreaExterno', 'filter': true, 'render': function(row) {
				return row.plaza.area.idAreaExterno + ' ' + row.plaza.area.descripcion;
			}},
			{'data': 'plaza.estado', 'filter': {'type': 'select', 'options': estadosPlaza}},
			{'data': 'plaza.fechaFinOferta', 'filter': {'type': 'date'}},
			{'data': 'resultado', 'render': function(row) {
				if (row.resultado == true) {
					return "<div title='Aceptada' class='circle-true'></div>";
				} else if (row.resultado == false) {
					return "<div title='Rechazada' class='circle-false'></div>";
				}
			}}
		]
	});
	
<%	if (bean.getListaOfertasCandidatos().size() > 0) { %>
		document.getElementById("plaza_preferencia").addEventListener("click", function(event) {
			var ofertasPreferencia = {};
			$("#tableOfertasPreferentes > tbody > .oferta_row").each(function() {
				ofertasPreferencia[$(this).data("id")] = parseInt($(this).find("input").val());
			});
			
			var params = {
					'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_GUARDAR_PREFERENCIAS %>',
					'<%= ControladorPlazasOfertadas.PARAM_OFERTAS_PREFERENCIAS %>': Atis.object2Json(ofertasPreferencia)};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		});
		
		$("#tableOfertasPreferentes > tbody > .oferta_row").each(function() {
			$(this).on("click", function() {
				var params = {
						'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_SELECCIONAR_CONTRATO %>',
						'<%= ControladorPlazasOfertadas.PARAM_PLAZA_OFERTADA %>': row.plaza.codNum};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			});
		});
<%	} %>
	
});
</script>