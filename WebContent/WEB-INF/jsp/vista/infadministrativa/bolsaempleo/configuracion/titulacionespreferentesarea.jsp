<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionTitulacionesPreferentesArea"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaTitulacionesArea"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulacionesArea bean = (VistaTitulacionesArea) uvdatos.getVistas().get(VistaTitulacionesArea.class.getName());
%>

<div class="bolsa-empleo">
	
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } else {%>
	
	<h2>Titulaciones preferentes por área</h2>
	
	<div class="form-select">
		<label>Área</label>
		<select id="select_area">
			<option value="0">Elija el área</option>
			<%
			for(Area area: bean.getAreas()) {
			%>
    			<option value="<%=area.getCodNum()%>"><%=area.getDescripcion()%></option>
    		<%
    		}
    		%>
		</select>
	
	</div>
	
	<div id="tablas_titulaciones">
		<table class="bluetable bolsaempleo" id="table_titulaciones_preferentes_area">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col" style="width:10%" title="Id de la titulación">Id</th>
				<th scope="col"	style="width:70%">Nombre</th>
			</tr>
			<tbody>		
			</tbody>
			<tfoot>
				<tr>
					<th colspan="3" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	
	    <table class="bluetable bolsaempleo" id="table_titulaciones">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col" style="width:10%" title="Id de la titulación">Id</th>
				<th scope="col"	style="width:85%">Nombre</th>
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colspan="3" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	</div>
	<% } %>
</div>

<script>

	$(document).ready(function() {
		
		var table_titulaciones_area;
		var table_titulaciones;
		
		function inicializarTablas(id, title) {
			table_titulaciones_area = new Atis.DataTable('#table_titulaciones_preferentes_area', {
			    "ajax": { url: "<%=ControladorGestionTitulacionesPreferentesArea.URL_PATTERN_AJAX%>", async: false },
			    "params": {"<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>": id},
			    "selectable": true,
			    "filterable": true,
			    "pageSize": 5,
			    "title": title,
			    "action": "<%=ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES_PREFERENTES_AREA%>",
			    "columns": [
			    	{'data': 'codNum', 'filterable': true},
			    	{'data': 'codNum', 'filter': {'type': 'number'}},
			        {'data': 'nombre', 'class': 'overflow-auto', 'filter': {'type': 'text'}},
			    ],
			    "actions": [
			    	{'label': 'Eliminar', 'title': 'Eliminar titulación del área', 'onClick': function(selected) {
			    		var params = {
			    				'a': '<%=ControladorGestionTitulacionesPreferentesArea.ACCION_ELIMINAR_TITULACION_AREA%>',
			    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_TITULACIONES%>': JSON.stringify(selected),
			    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>': id
			    				};
		        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    	} },
			    ]
			});
			
			table_titulaciones = new Atis.DataTable('#table_titulaciones', {
			    "ajax": { url: "<%=ControladorGestionTitulacionesPreferentesArea.URL_PATTERN_AJAX%>", async: false },
			    "params": {"<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>": id},
			    "selectable": true,
			    "pageSize": 5,
			    "title": 'Titulaciones Disponibles',
			    "action": "<%=ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES%>",
			    "columns": [
			    	{'data': 'codNum', 'selectable': true},
			    	{'data': 'codNum', 'filter': {'type': 'number'}},
			        {'data': 'nombre', 'class': 'overflow-auto', 'filter': {'type': 'text'}},
			    ],
			    "actions": [
			    	{'label': 'Incluir', 'title': 'Incluir titulación en el área', 'onClick': function(selected) {
			    		if(selected.length) {
			    			var params = {
				    				'a': '<%=ControladorGestionTitulacionesPreferentesArea.ACCION_INCLUIR_TITULACION_AREA%>',
				    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_TITULACIONES%>': JSON.stringify(selected),
				    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>': id
				    				};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    		}
			    	} },
			    ]
			});
		}
		
		function cargarTablas(id, title) {
			table_titulaciones.setParam("<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>", id);
			table_titulaciones.refresh();
			table_titulaciones_area.setParam("<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>", id);
			table_titulaciones_area.setTitle(title);
			table_titulaciones_area.refresh();
		}
		
		document.getElementById("tablas_titulaciones").style.visibility = "hidden";
		
		document.getElementById("select_area").onchange = function () {
			if(this.value != 0) {
				var title_titulaciones_area = "Titulaciones Preferentes al Área: " + $(this).children("option").filter(":selected").text();
				if(document.getElementById("tablas_titulaciones").style.visibility == "hidden") {
					document.getElementById("tablas_titulaciones").style.visibility = "visible";
					inicializarTablas(this.value, title_titulaciones_area);
				} else {
					cargarTablas(this.value, title_titulaciones_area);
				}
			}
		}
		
		<% if(bean.getArea() != null) { %>
			document.getElementById("select_area").value = "<%= bean.getArea().getCodNum() %>";
			document.getElementById("tablas_titulaciones").style.visibility = "visible";
			inicializarTablas("<%= bean.getArea().getCodNum() %>");
		<%} %>
		
	});
	
</script>
