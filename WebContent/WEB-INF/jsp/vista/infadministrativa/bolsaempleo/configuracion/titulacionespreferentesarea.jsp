<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionTitulacionesPreferentesArea"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulacionesArea"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulacionesArea bean = (VistaTitulacionesArea) uvdatos.getVistas().get(VistaTitulacionesArea.class.getName());
Area area = bean.getArea();
%>

<div class="bolsa-empleo titulacionespreferentes">
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Titulaciones preferentes por área</h2>
	
	<div class="form-select">
		<label>Área</label>
		<select id="select_area">
			<option value="0">Elija el área</option>
			<% for(Area a: bean.getAreas()) { %>
    			<option value="<%=a.getCodNum()%>" <%= area != null && area.getCodNum().equals(a.getCodNum()) ? "selected=\"selected\"" : "" %>><%=a.getDescripcion()%></option>
    		<% } %>
		</select>
	</div>
	
	<div id="tablas_titulaciones" style="<%= area == null ? "visibility: hidden" : "" %>">
		<table class="bluetable bolsaempleo" id="table_titulaciones_preferentes_area">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col"	class="nombre" style="width:100%">Nombre</th>
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
				<th scope="col"	class="nombre" style="width:100%">Nombre</th>
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
</div>

<script>

	$(document).ready(function() {
		
		document.getElementById("select_area").onchange = function () {
			if(this.value != 0) {
				var params = {
	    			'<%= ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION %>': '<%= ControladorGestionTitulacionesPreferentesArea.ACCION_SELECCIONAR_AREA %>', 
	    			'<%= ControladorGestionTitulacionesPreferentesArea.PARAM_AREA %>': this.value 
	    		};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);        		
			} else {
				document.getElementById("tablas_titulaciones").style.visibility = "hidden";
			}			
		}
		
		<% if (area != null) { %>
			table_titulaciones_area = new Atis.DataTable('#table_titulaciones_preferentes_area', {
			    "ajax": { url: "<%=ControladorGestionTitulacionesPreferentesArea.URL_PATTERN_AJAX%>", async: false },
			    "params": {"<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>": '<%= area.getCodNum() %>'},
			    "selectable": true,
			    "filterable": true,
			    "pageSize": 5,
			    "title": "Titulaciones Preferentes al Área: <%= area.getDescripcion() %>",
			    "action": "<%=ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES_PREFERENTES_AREA%>",
			    "columns": [
			    	{'data': 'codNum', 'selectable': true},
			        {'data': 'nombre', 'class': 'overflow-auto', 'filter': {'type': 'text'}},
			    ],
			    "actions": [
			    	{'label': 'Eliminar', 'title': 'Eliminar titulación del área', 'onClick': function(selected) {
			    		if(selected.length) {
				    		var idArea = this.getParam("<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>");
				    		var params = {
			    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION%>': '<%=ControladorGestionTitulacionesPreferentesArea.ACCION_ELIMINAR_TITULACION_AREA%>',
			    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_TITULACIONES%>': JSON.stringify(selected),
			    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>': idArea
		    				};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    		}
			    	} },
			    ]
			});
			
			table_titulaciones = new Atis.DataTable('#table_titulaciones', {
			    "ajax": { url: "<%=ControladorGestionTitulacionesPreferentesArea.URL_PATTERN_AJAX%>", async: false },
			    "params": {"<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>": '<%= area.getCodNum() %>'},
			    "selectable": true,
			    "filterable": true,
			    "pageSize": 5,
			    "title": 'Titulaciones Disponibles',
			    "action": "<%=ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES%>",
			    "columns": [
			    	{'data': 'codNum', 'selectable': true},
			        {'data': 'nombre', 'class': 'overflow-auto', 'filter': {'type': 'text'}},
			    ],
			    "actions": [
			    	{'label': 'Incluir', 'title': 'Incluir titulación en el área', 'onClick': function(selected) {
			    		if(selected.length) {
			    			var idArea = this.getParam("<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>");
			    			var params = {
			    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION%>': '<%=ControladorGestionTitulacionesPreferentesArea.ACCION_INCLUIR_TITULACION_AREA%>',
			    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_TITULACIONES%>': JSON.stringify(selected),
			    				'<%=ControladorGestionTitulacionesPreferentesArea.PARAM_AREA%>': idArea
				    		};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    		}
			    	} },
			    ]
			});
		<% } %>
	});
</script>
