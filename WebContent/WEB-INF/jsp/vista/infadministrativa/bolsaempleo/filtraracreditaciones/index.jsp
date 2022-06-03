<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.stream.Collectors" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorFiltrarAcreditaciones"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFiltrarAcreditaciones" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFiltrarAcreditaciones bean = (VistaFiltrarAcreditaciones)uvdatos.getVistas().get(VistaFiltrarAcreditaciones.class.getName());
UsuarioBolsaEmpleo candidato = bean.getCandidato();
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Filtrado de candidatos por acreditación</h2>
	<p>Filtrado de candidatos por acreditaciones, para su posible exclusión.</p>
	
	<table class="bluetable bolsaempleo" id="tableCandidatosFAC">
		<tr>
			<th scope="col" style="width:12%">Documento</th>
			<th scope="col" style="width:58%">Candidato</th>
			<th scope="col" style="width:15%" title="Número de acreditaciones">Nº Acreditaciones</th>
			<th scope="col" style="width:15%">Nº Acr. Validadas</th>
		</tr>
		<tbody></tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<% if (candidato != null) { %>
		<table class="bluetable bolsaempleo" id="tableAcreditacionesCandidatoFAC">
			<tr>
				<th scope="col" style="width:8%">Validada</th>
				<th scope="col" style="width:10%">Id</th>
				<th scope="col" style="width:10%">Código</th>
				<th scope="col"	style="width:45%">Nombre</th>
				<th scope="col"	style="width:15%">Activa</th>
				<th scope="col"	style="width:12%"></th>
			</tr>
			<tbody></tbody>
			<tfoot>
				<tr>
					<th colspan="6" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	<% } %>
	
</div>
	
<script>
	$(document).ready(function() {
		var tableCandidatos = new Atis.DataTable('#tableCandidatosFAC', {
			"ajax": { url: "<%= ControladorFiltrarAcreditaciones.URL_PATTERN_AJAX %>", async: false },
			"action": "<%= ControladorFiltrarAcreditaciones.ACCION_DATATABLE_CANDIDATOS %>",
			"pageSize": 10,
			"filterable": true,
			"stateSave": true,
			"title": 'CANDIDATOS',
			"clickable": {'onClick': function(row) {
				var params = {
						'<%= ControladorFiltrarAcreditaciones.PARAM_ACCION %>': '<%= ControladorFiltrarAcreditaciones.ACCION_CANDIDATO_SELECCIONADO %>',
						'<%= ControladorFiltrarAcreditaciones.PARAM_CANDIDATO %>': row.codNum
				};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			}},
			<% if (candidato != null) { %> "selected": <%= candidato.getCodNum() %> ,<% } %>
			"columns": [
				{'data': 'prsnif', 'filter': true},
				{'data': 'apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
					return row.nombre + " " + row.apellido1 + " " + row.apellido2; 
				}},
				{'data': 'totalAcreditaciones', 'order': {'active': false}},
				{'data': 'totalAcreditacionesValidadas', 'order': {'active': false}},
			],
		});
		
		<% if (candidato != null) { %>
			var acreditaiconesValidadas = [<%= bean.getValidadas() != null ? bean.getValidadas().stream().map(t -> t.getCodNum().toString()).collect(Collectors.joining(",")) : "" %>];
		
			var table = new Atis.DataTable('#tableAcreditacionesCandidatoFAC', {
				"ajax": { url: "<%= ControladorFiltrarAcreditaciones.URL_PATTERN_AJAX %>" },
				"pageSize": 10,
				"action": "<%= ControladorFiltrarAcreditaciones.ACCION_DATATABLE_ACREDITACIONES_CANDIDATO %>",
				"params": {"<%=ControladorFiltrarAcreditaciones.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
				"selectable": {'all': false},
				"selected": acreditaiconesValidadas,
				"filterable": true,
				"title": 'ACREDITACIONES: <%=candidato.getNombre() + " " + candidato.getPrimerApellido() + " " + candidato.getSegundoApellido()%>',
				"columns": [
					{'data': 'codNum', 'selectable': {'onChange': function(row, checkbox) {
								var accion = checkbox.checked ? '<%= ControladorFiltrarAcreditaciones.ACCION_ACREDITACION_SELECCIONADA %>'
										: '<%= ControladorFiltrarAcreditaciones.ACCION_ACREDITACION_DESELECCIONADA %>';
								var params = {
									'a': accion,
									'<%= ControladorFiltrarAcreditaciones.PARAM_CANDIDATO %>': '<%= bean.getCandidato().getCodNum() %>',
									'<%= ControladorFiltrarAcreditaciones.PARAM_ACREDITACION %>': row.codNum,
								};
								
								Atis.sendAjax("<%= request.getRequestURI() %>", params, function(data) {
									tableCandidatos.refresh();
								}, function(data) {
									var response = JSON.parse(data.responseText);
									
									// alert en caso de error
									Atis.alertDialog("Error en la solicitud", response.descripcion);
									
									checkbox.checked = !checkbox.checked;
									checkbox.checked ? $(checkbox).parent().parent().addClass("selected") : $(checkbox).parent().parent().removeClass("selected");
								});
							}
						}
					},
					{'data': 'codNum', 'filter': {'type': 'number'}},
					{'data': 'codigo', 'filter': true, 'render': function(row) {
						return '<%= bean.getCodigoPadreMeritoPreferente() %>.' + row.meritoPreferente.codigo;
					}},
					{'data': 'meritoPreferente.nombre', 'filter': false, 'order': {'active': false}, 'render': function(row) {
						var merito = row.meritoPreferente.nombre + (row.meritoPreferenteOpcion ? ' (' + row.meritoPreferenteOpcion.nombre + ')' : ''); 
						return merito + (row.descripcion ? '<br/>Descripción: ' + row.descripcion : '');
					}},
					{'data': 'activo', 'filter': false, 'order': {'active': false}, 'render': function(row) {
		        		if (row.activo) {
		        			return "<div title='Activa' class='circle-true'></div>"; 
		        		} else {
		        			return "<div title='Inactiva' class='circle-false'></div>";
		        		}
	        		}},
					{'data': 'codnum', 'buttons': [
						{'label': 'Descargar', 'title': 'Descargar fichero del mérito', 'onClick': function(row) {
							window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
									+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_ACREDITACION_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_ACREDITACION %>=" + row.codNum);
						}},
					]}
				],
			});
			
			Atis.smoothScrollToAnchor("#tableAcreditacionesCandidatoFAC");
		<% } %>
		
	}); 
</script>
	
