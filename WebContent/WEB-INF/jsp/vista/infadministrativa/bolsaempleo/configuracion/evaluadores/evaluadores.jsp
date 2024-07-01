<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionEvaluadores"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEvaluadores"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaEvaluadores bean = (VistaEvaluadores) uvdatos.getVistas().get(VistaEvaluadores.class.getName());
Departamento departamento = bean.getDepartamento();
Area area = bean.getArea();

boolean showUsuarios = departamento != null;
boolean showExportar = departamento == null || departamento.getCodNum() == 0;
%>

<div class="bolsa-empleo evaluadores-listar">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Evaluadores de un departamento</h2>
	
	<div class="">
		<div class="form-group-container col2">
			<div class="form-group">   
				<div class="form-check-custom w-64">
					<label>Departamento</label>
					<select id="select_departamento">
						<option value="0">Elija el departamento</option>
						<% for(Departamento dep: bean.getDepartamentos()) { %>
							<option value="<%=dep.getCodNum()%>" <%= departamento != null && departamento.getCodNum().equals(dep.getCodNum()) ? "selected=\"selected\"" : "" %>><%= dep.getDescripcion() %></option>
						<% } %>
					</select>
				</div>
			</div>
			<div class="form-group-container-offset col2 gap-5"  id="nuevo_evaluador_cont" style="<%= !showUsuarios ? "display: none" : "" %>">
				<div class="form-group" style="width:80%">
					<label for="nombre_evaluador">Cuenta TIC evaluador <i class="tooltip">(?)<span>Introduzca la cuenta TIC sin @ujaen.es</span></i></label>
					<input class="form-input-custom" type="text" name="<%= ControladorGestionEvaluadores.PARAM_NOMBRE_EVALUADOR %>" id="nombre_evaluador" value=""/>
				</div>
				<div class="form-group" style="width:20%;">
					<label style="visibility: hidden">.</label>
					<button class="link-btn" id="nuevo_evaluador" title="Añadir evaluador">Buscar</button>
				</div>
			</div>
			
			<div class="form-group-container-offset col2 gap-5" id="exportar_div" style="<%= !showExportar ? "display: none" : "" %>">
				<div class="form-group">
					<label style="visibility: hidden">.</label>
					<button class="link-btn" id="exportar">Exportar evaluadores</button>
				</div>
			</div>
		</div>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableAreasEVA" style="<%= departamento == null ? "visibility: hidden" : "" %>">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:10%" title="Id de la area">Id</th>
			<th scope="col" style="width:20%" class="codigo" title="Código de area">Código</th>
			<th scope="col" style="width:60%">Area</th>
			<th scope="col" style="width:15%">Nº Evaluadores</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" id="tableEvaluadoresAreaEVA" style="<%= area == null ? "visibility: hidden" : "" %>">
		<tr>
			<th scope="col" class="dni" style="width:25%">D.N.I</th>
			<th scope="col"	style="width:55%">Nombre</th>
			<th scope="col" class="center" style="width:10%">Activo</th>
			<th scope="col" style="width:11%"></th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>

	$(document).ready(function() {
		document.getElementById("select_departamento").onchange = function () {
			if(this.value != 0) {
				var params = {
					'<%= ControladorGestionEvaluadores.PARAM_ACCION %>': '<%= ControladorGestionEvaluadores.ACCION_SELECCIONAR_DEPARTAMENTO %>', 
					'<%= ControladorGestionEvaluadores.PARAM_DEPARTAMENTO %>': this.value
				};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			} else {
				document.getElementById("nuevo_evaluador_cont").style.display = "none";
				document.getElementById("tableAreasEVA").style.display = "none";
				document.getElementById("exportar_div").style.display = "grid";
			}
		}
		
		$('#exportar').on('click', function() {
			window.open("<%= request.getRequestURI() + "?" + ControladorGestionEvaluadores.PARAM_ACCION + "=" + ControladorGestionEvaluadores.ACCION_EXPORTAR %>");
		});
		
		<% if (departamento != null) { %>
			var tableAreas = new Atis.DataTable('#tableAreasEVA', {
				"ajax": { url: "<%=ControladorGestionEvaluadores.URL_PATTERN_AJAX%>", async: false },
				"pageSize": 10,
				"action": "<%= ControladorGestionEvaluadores.ACCION_DATATABLE_AREAS %>",
				"params": {"<%=ControladorGestionEvaluadores.PARAM_DEPARTAMENTO%>": '<%= departamento.getCodNum() %>'},
				"filterable": true,
				"selectable": true,
				"selectedAll": true,
				"stateSave": true,
				"defaultOrderBy": 2,
				"title": "ÁREAS DEL DEPARTAMENTO: <%= departamento.getDescripcion() %>",
				"clickable": {'onClick': function(row) {
					var params = {
							'<%= ControladorGestionEvaluadores.PARAM_ACCION %>': '<%=ControladorGestionEvaluadores.ACCION_SELECCIONAR_AREA%>',
							'<%= ControladorGestionEvaluadores.PARAM_AREA %>': row.codNum,
							'<%= ControladorGestionEvaluadores.PARAM_DEPARTAMENTO %>': '<%= departamento.getCodNum() %>'
					};
					Atis.sendForm("<%= request.getRequestURI() %>", params);
				}},
				<% if (bean.getArea() != null) { %> "selected": <%= bean.getArea().getCodNum() %> ,<% } %>
				"columns": [
					{'data': 'codNum', 'selectable': true},
					{'data': 'codNum', 'filter': {'type': 'number'}},
					{'data': 'idAreaExterno', 'filter': true},
					{'data': 'descripcion', 'filter': true},
					{'data': 'numeroEvaluadores', 'order': true}
				]
			});
			
			document.getElementById("nuevo_evaluador").addEventListener("click", function(event) {
				event.preventDefault();
				var params = {
						'<%= ControladorGestionEvaluadores.PARAM_ACCION %>': '<%= ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES %>',
						'<%=ControladorGestionEvaluadores.PARAM_NOMBRE_EVALUADOR%>': document.getElementById("nombre_evaluador").value,
						'<%=ControladorGestionEvaluadores.PARAM_AREAS%>': Atis.object2Json(tableAreas.getCheckedItems()),
						'<%=ControladorGestionEvaluadores.PARAM_DEPARTAMENTO%>': '<%= departamento.getCodNum() %>',
						'<%=ControladorGestionEvaluadores.PARAM_AREA%>': '<%= area != null ? area.getCodNum() : "" %>',
				}
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			});
		<% } %>
		
		<% if (area != null) { %>
			var tableEvaluadores = new Atis.DataTable('#tableEvaluadoresAreaEVA', {
				"ajax": { url: "<%=ControladorGestionEvaluadores.URL_PATTERN_AJAX%>", async: false },
				"params": {"<%=ControladorGestionEvaluadores.PARAM_AREA%>": "<%= area.getCodNum() %>"},
				"pageSize": 10,
				"title": "EVALUADORES DEL ÁREA: <%= area.getDescripcion() %>",
				"filterable": true,
				"defaultOrderBy": 0,
				"defaultOrderDirection": 'asc',
				"action": "<%=ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES%>",
				"columns": [
					{'data': 'prsnif', 'filter': true},
					{'data': 'apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
						return row.nombre + " " + row.apellido1 + " " + row.apellido2;
					}},
					{'data': 'activo', 'filter': {'type': 'select', 'options':{'true': 'Activo', 'false': 'Inactivo'}, 'optionDefault': 'true'}, 'order': {'active': false}, 'render': function(row) {
						if (row.activo) {
							return "<div title='Activo' class='circle-true'></div>";
						} else {
							return "<div title='Desactivado' class='circle-false'></div>";
						}
					}},
					{'data': 'codnum', 'buttons': [{
						'label': function(row) { return row.activo ? "Borrar" : "Restaurar"; },
						'class': function(row) { return row.activo ? "btn-borrar" : "btn-restaurar"; },
						'title':  function(row) { return row.activo ? "Desactivar evaluador" : "Activar evaluador"; }, 'onClick': function(row) {
							var mensaje = "¿Desea borrar el evaluador seleccionado?";
							var titulo = "Borrar evaluador";
							
							if(!row.activo) {
								titulo = "Restaurar evaluador";
								mensaje = "¿Desea restaurar el evaluador seleccionado?";
							}
							
							Atis.confirmDialog(titulo, mensaje, {
								Si: function() {
									var params = {
										'<%= ControladorGestionEvaluadores.PARAM_ACCION %>': '<%= ControladorGestionEvaluadores.ACCION_ELIMINAR_EVALUADOR %>',
										'<%= ControladorGestionEvaluadores.PARAM_USUARIO %>': row.codNum,
										'<%= ControladorGestionEvaluadores.PARAM_AREA %>': '<%= area.getCodNum() %>',
										'<%= ControladorGestionEvaluadores.PARAM_ACTIVO %>': !row.activo,
										'<%= ControladorGestionEvaluadores.PARAM_DEPARTAMENTO %>': '<%= departamento.getCodNum() %>'
									};
									Atis.sendForm("<%= request.getRequestURI() %>", params);
									$(this).dialog("close");
								},
								No: function() {
									$(this).dialog("close");
								}
							});
						}
					}]}
				]
			});
		<% } %>
	});
	
</script>
